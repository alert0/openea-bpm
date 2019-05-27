package org.openbpm.bpm.act.img;

import org.openbpm.base.core.util.ThreadMapUtil;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D.Double;
import java.util.Map;
import org.activiti.bpmn.model.AssociationDirection;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;

public class BpmProcessDiagramCanvas extends DefaultProcessDiagramCanvas {
    public BpmProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType, String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {
        super(width, height, minX, minY, imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);
        LABEL_COLOR = Color.BLACK;
        LABEL_FONT = null;
    }

    public void drawHighLight(int x, int y, int width, int height, Paint paint) {
        Paint originalPaint = this.g.getPaint();
        Stroke originalStroke = this.g.getStroke();
        this.g.setPaint(paint);
        this.g.setStroke(THICK_TASK_BORDER_STROKE);
        this.g.draw(new Double((double) x, (double) y, (double) width, (double) height, 20.0d, 20.0d));
        this.g.setPaint(originalPaint);
        this.g.setStroke(originalStroke);
    }

    public void drawSequenceflow(int[] xPoints, int[] yPoints, boolean drawConditionalIndicator, boolean isDefault, boolean highLighted, Paint paint, double scaleFactor) {
        AssociationDirection associationDirection = AssociationDirection.ONE;
        boolean conditional = drawConditionalIndicator;
        Paint originalPaint = this.g.getPaint();
        Stroke originalStroke = this.g.getStroke();
        this.g.setPaint(CONNECTION_COLOR);
        if ("sequenceFlow".equals("association")) {
            this.g.setStroke(ASSOCIATION_STROKE);
        } else if (highLighted) {
            this.g.setPaint(paint);
            this.g.setStroke(HIGHLIGHT_FLOW_STROKE);
        }
        for (int i = 1; i < xPoints.length; i++) {
            this.g.draw(new Line2D.Double((double) Integer.valueOf(xPoints[i - 1]).intValue(), (double) Integer.valueOf(yPoints[i - 1]).intValue(), (double) Integer.valueOf(xPoints[i]).intValue(), (double) Integer.valueOf(yPoints[i]).intValue()));
        }
        if (isDefault) {
            drawDefaultSequenceFlowIndicator(new Line2D.Double((double) xPoints[0], (double) yPoints[0], (double) xPoints[1], (double) yPoints[1]), scaleFactor);
        }
        if (conditional) {
            drawConditionalSequenceFlowIndicator(new Line2D.Double((double) xPoints[0], (double) yPoints[0], (double) xPoints[1], (double) yPoints[1]), scaleFactor);
        }
        if (associationDirection.equals(AssociationDirection.ONE) || associationDirection.equals(AssociationDirection.BOTH)) {
            drawArrowHead(new Line2D.Double((double) xPoints[xPoints.length - 2], (double) yPoints[xPoints.length - 2], (double) xPoints[xPoints.length - 1], (double) yPoints[xPoints.length - 1]), scaleFactor);
        }
        if (associationDirection.equals(AssociationDirection.BOTH)) {
            drawArrowHead(new Line2D.Double((double) xPoints[1], (double) yPoints[1], (double) xPoints[0], (double) yPoints[0]), scaleFactor);
        }
        this.g.setPaint(originalPaint);
        this.g.setStroke(originalStroke);
    }

    public void drawExclusiveGateway(GraphicInfo graphicInfo, double scaleFactor) {
        Paint paint = this.g.getPaint();
        this.g.setPaint((Paint) ((Map) ThreadMapUtil.get("DefaultInstHistImgService_gateMap")).getOrDefault(((FlowNode) ThreadMapUtil.get("BpmProcessDiagramGenerator_flowNode")).getId(), paint));
        BpmProcessDiagramCanvas.super.drawExclusiveGateway(graphicInfo, scaleFactor);
        this.g.setPaint(paint);
    }
}
