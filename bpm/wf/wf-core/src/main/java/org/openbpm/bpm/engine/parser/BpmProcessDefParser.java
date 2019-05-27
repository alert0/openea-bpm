package org.openbpm.bpm.engine.parser;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bpm.engine.parser.FlowConfigConstants.GLOBAL;
import org.openbpm.bpm.engine.parser.FlowConfigConstants.NODE;
import org.openbpm.bpm.engine.parser.flow.AbsFlowParse;
import org.openbpm.bpm.engine.parser.node.AbsNodeParse;
import java.util.ArrayList;
import java.util.List;

public class BpmProcessDefParser {
    private static List<AbsNodeParse> baseNodeParsers;
    private static List<AbsFlowParse> flowParsers;

    public static void processDefParser(DefaultBpmProcessDef bpmProcessDef, JSONObject bpmDefSetting) {
        JSONObject flowConf = bpmDefSetting.getJSONObject(GLOBAL.FLOW);
        for (AbsFlowParse flowParser : getFlowParsers()) {
            flowParser.parse(bpmProcessDef, flowConf);
        }
        JSONObject nodeMap = bpmDefSetting.getJSONObject(NODE.NODE_MAP);
        for (BpmNodeDef nodeDef : bpmProcessDef.getBpmnNodeDefs()) {
            JSONObject nodeConfig = nodeMap.getJSONObject(nodeDef.getNodeId());
            for (AbsNodeParse nodeParser : getNodeParsers()) {
                if (nodeParser.isSupport(nodeDef)) {
                    nodeParser.parse(nodeDef, nodeConfig);
                }
            }
        }
    }

    private static List<AbsFlowParse> getFlowParsers() {
        if (CollectionUtil.isNotEmpty(flowParsers)) {
            return flowParsers;
        }
        flowParsers = new ArrayList(AppUtil.getImplInstance(AbsFlowParse.class).values());
        return flowParsers;
    }

    private static List<AbsNodeParse> getNodeParsers() {
        if (CollectionUtil.isNotEmpty(baseNodeParsers)) {
            return baseNodeParsers;
        }
        baseNodeParsers = new ArrayList(AppUtil.getImplInstance(AbsNodeParse.class).values());
        return baseNodeParsers;
    }
}
