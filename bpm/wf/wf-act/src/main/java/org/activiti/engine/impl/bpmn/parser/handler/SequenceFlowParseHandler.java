package org.activiti.engine.impl.bpmn.parser.handler;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.el.GroovyCondition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.apache.commons.lang3.StringUtils;

public class SequenceFlowParseHandler extends AbstractBpmnParseHandler<SequenceFlow> {
    public static final String PROPERTYNAME_CONDITION = "condition";
    public static final String PROPERTYNAME_CONDITION_TEXT = "conditionText";

    public Class<? extends BaseElement> getHandledType() {
        return SequenceFlow.class;
    }

    /* access modifiers changed from: protected */
    public void executeParse(BpmnParse bpmnParse, SequenceFlow sequenceFlow) {
        Expression skipExpression;
        ScopeImpl scope = bpmnParse.getCurrentScope();
        ActivityImpl sourceActivity = scope.findActivity(sequenceFlow.getSourceRef());
        ActivityImpl destinationActivity = scope.findActivity(sequenceFlow.getTargetRef());
        if (StringUtils.isNotEmpty(sequenceFlow.getSkipExpression())) {
            skipExpression = bpmnParse.getExpressionManager().createExpression(sequenceFlow.getSkipExpression());
        } else {
            skipExpression = null;
        }
        TransitionImpl transition = sourceActivity.createOutgoingTransition(sequenceFlow.getId(), skipExpression);
        bpmnParse.getSequenceFlows().put(sequenceFlow.getId(), transition);
        transition.setProperty("name", sequenceFlow.getName());
        transition.setProperty("documentation", sequenceFlow.getDocumentation());
        transition.setDestination(destinationActivity);
        if (StringUtils.isNotEmpty(sequenceFlow.getConditionExpression())) {
            Condition expressionCondition = new GroovyCondition(sequenceFlow.getConditionExpression());
            transition.setProperty(PROPERTYNAME_CONDITION_TEXT, sequenceFlow.getConditionExpression());
            transition.setProperty(PROPERTYNAME_CONDITION, expressionCondition);
        }
        createExecutionListenersOnTransition(bpmnParse, sequenceFlow.getExecutionListeners(), transition);
    }
}
