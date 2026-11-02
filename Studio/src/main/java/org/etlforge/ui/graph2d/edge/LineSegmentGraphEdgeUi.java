package org.etlforge.ui.graph2d.edge;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D.Double;
import javax.swing.Icon;
import org.etlforge.ui.graph.edge.AbstractGraphEdgeUi;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.dnd.DropStatus;
import org.etlforge.ui.graphics.LineUtils;
import org.etlforge.ui.graphics.RectangleUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.popup.PopupMenuUtils;

public class LineSegmentGraphEdgeUi extends AbstractGraphEdgeUi implements GraphEdgeUi2D {
   private static final boolean FILL_ARROW = true;
   private static final float START_POINT_SIZE = 3.0F;
   private static final double EPSILON = 4.0;
   private final Graph2D graph;
   private final GraphEdge edge;
   private Line2D[] lines;
   private Point2D startPoint;
   private Point2D endPoint;
   private LineSegmentGraphEdgeUi.ArrowHead arrowHead;
   private Color color;
   private Icon icon;
   private Rectangle iconBounds;
   private String toolTip;
   private String iconToolTip;
   private int lineWidth;
   private LineSegmentFactory segmentFactory;

   public LineSegmentGraphEdgeUi(Graph2D var1, GraphEdge var2) {
      this(var1, var2, new Point(), new Point());
   }

   public LineSegmentGraphEdgeUi(Graph2D var1, GraphEdge var2, Point2D var3, Point2D var4) {
      this.graph = var1;
      this.edge = var2;
      this.startPoint = (Point2D)var3.clone();
      this.endPoint = (Point2D)var4.clone();
      this.color = javax.swing.UIManager.getColor("Label.foreground");
      this.lineWidth = 1;
      this.arrowHead = new LineSegmentGraphEdgeUi.ArrowHead(4);
      this.setLineSegmentFactory(new SingleLineSegmentFactory());
   }

   public void setLineSegmentFactory(LineSegmentFactory var1) {
      this.segmentFactory = var1;
      this.update();
   }

   protected final Graph2D getGraph() {
      return this.graph;
   }

   @Override
   public GraphEdge getEdge() {
      return this.edge;
   }

   @Override
   public void paint(Graphics2D var1) {
      Color var2 = var1.getColor();
      RenderingHints var3 = var1.getRenderingHints();
      UiUtils.makeAntiAliased(var1);
      var1.setColor(this.color);
      this.drawLine(var1);
      this.drawStartPoint(var1);
      this.arrowHead.fill(var1);
      var1.setRenderingHints(var3);
      this.drawIcon(var1);
      var1.setColor(var2);
   }

   private void drawLine(Graphics2D var1) {
      Stroke var2 = var1.getStroke();
      var1.setStroke(new BasicStroke(this.lineWidth));

      for (Line2D var6 : this.lines) {
         var1.draw(var6);
      }

      var1.setStroke(var2);
   }

   private void drawStartPoint(Graphics2D var1) {
      Double var2 = new Double(this.startPoint.getX() - 3.0, this.startPoint.getY() - 3.0, 6.0, 6.0);
      var1.fill(var2);
   }

   private void drawIcon(Graphics2D var1) {
      if (this.icon != null) {
         if (this.iconBounds == null) {
            this.updateIconBounds();
         }

         this.icon.paintIcon(null, var1, this.iconBounds.x + 2, this.iconBounds.y + 2);
      }
   }

   @Override
   public void setStartPoint(Point2D var1) {
      if (!var1.equals(this.startPoint)) {
         this.startPoint = (Point2D)var1.clone();
         this.update();
      }
   }

   @Override
   public Point2D getStartPoint() {
      return (Point2D)this.startPoint.clone();
   }

   @Override
   public void setEndPoint(Point2D var1) {
      if (!var1.equals(this.endPoint)) {
         this.endPoint = (Point2D)var1.clone();
         this.update();
      }
   }

   @Override
   public Point2D getEndPoint() {
      return (Point2D)this.endPoint.clone();
   }

   @Override
   public boolean containsPoint(Point2D var1) {
      return this.isPointCloseEnoughToLine(var1) || this.isPointCloseEnoughToIcon(var1);
   }

   private boolean isPointCloseEnoughToLine(Point2D var1) {
      for (Line2D var5 : this.lines) {
         if (var5.ptSegDist(var1) <= 4.0) {
            return true;
         }
      }

      return false;
   }

   private boolean isPointCloseEnoughToIcon(Point2D var1) {
      if (this.iconBounds != null) {
         Rectangle var2 = RectangleUtils.pad(this.iconBounds, 4, 4);
         if (var2.contains(var1)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public String getToolTipText(MouseEvent var1) {
      return this.iconBounds != null && this.iconBounds.contains(var1.getPoint()) ? this.iconToolTip : this.toolTip;
   }

   @Override
   public boolean handleMouseEvent(MouseEvent var1) {
      if (PopupMenuUtils.isPopupTrigger(var1)) {
         Point var2 = var1.getPoint();
         if (this.iconBounds != null && this.iconBounds.contains(var1.getPoint())) {
            this.graph.handleEdgeContextMenuRequest(this, var2.x, var2.y);
            return true;
         }
      }

      return false;
   }

   @Override
   public void setDropStatus(DropStatus var1) {
   }

   public void setColor(Color var1) {
      this.color = var1;
   }

   public void setIcon(Icon var1) {
      this.icon = var1;
      this.updateIconBounds();
   }

   public void setToolTip(String var1) {
      this.toolTip = var1;
   }

   public void setIconToolTip(String var1) {
      this.iconToolTip = var1;
   }

   public void setLineWidth(int var1) {
      this.lineWidth = var1;
   }

   public void setArrowSize(int var1) {
      this.arrowHead = new LineSegmentGraphEdgeUi.ArrowHead(var1);
   }

   public boolean isWithinIcon(Point var1) {
      return this.iconBounds != null && this.iconBounds.contains(var1);
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder(this.getClass().getSimpleName());
      var1.append("[from: ").append(this.startPoint);
      var1.append("  to: ").append(this.endPoint).append("]");
      return var1.toString();
   }

   private void update() {
      this.updateLines();
      this.updateArrowHead();
      this.updateIconBounds();
   }

   private void updateLines() {
      this.lines = this.segmentFactory.createSegments(this.startPoint, this.endPoint);
   }

   private void updateIconBounds() {
      if (this.icon != null) {
         Point2D var1 = this.getIconCenter();
         int var2 = this.icon.getIconWidth();
         int var3 = this.icon.getIconWidth();
         int var4 = (int)(var1.getX() - var2 / 2);
         int var5 = (int)(var1.getY() - var3 / 2);
         this.iconBounds = new Rectangle(var4 - 2, var5 - 2, var2 + 4, var3 + 4);
      } else {
         this.iconBounds = null;
      }
   }

   private Point2D getIconCenter() {
      return this.lines.length % 2 == 0 ? this.getCenterCorner() : this.getCenterOfCenterLine();
   }

   private Point2D getCenterCorner() {
      assert this.lines.length % 2 == 0;
      Line2D var1 = this.lines[this.lines.length / 2];
      return var1.getP1();
   }

   private Point2D getCenterOfCenterLine() {
      assert this.lines.length % 2 == 1;
      Line2D var1 = this.lines[this.lines.length / 2];
      return LineUtils.getMidPoint(var1);
   }

   private void updateArrowHead() {
      this.arrowHead.update(this.getLastLine());
   }

   private Line2D getLastLine() {
      return this.lines[this.lines.length - 1];
   }

   private static class ArrowHead {
      private final int arrowLength;
      private final int arrowWidth;
      private Point2D tip;
      private Point2D base1;
      private Point2D base2;

      public ArrowHead(int var1) {
         this.arrowLength = 2 * var1;
         this.arrowWidth = var1;
      }

      public void update(Line2D var1) {
         Point2D var2 = var1.getP1();
         this.tip = var1.getP2();
         double var3 = this.tip.distance(var2);
         double var5 = (this.tip.getX() - var2.getX()) / var3;
         double var7 = (this.tip.getY() - var2.getY()) / var3;
         double var9 = this.tip.getX() - this.arrowLength * var5;
         double var11 = this.tip.getY() - this.arrowLength * var7;
         this.base1 = new java.awt.geom.Point2D.Double(var9 - this.arrowWidth * var7 + 0.5, var11 + this.arrowWidth * var5 + 0.5);
         this.base2 = new java.awt.geom.Point2D.Double(var9 + this.arrowWidth * var7 + 0.5, var11 - this.arrowWidth * var5 + 0.5);
      }

      public void fill(Graphics2D var1) {
         if (this.tip != null) {
            java.awt.geom.Path2D.Double var2 = new java.awt.geom.Path2D.Double();
            var2.moveTo(this.tip.getX(), this.tip.getY());
            var2.lineTo(this.base1.getX(), this.base1.getY());
            var2.lineTo(this.base2.getX(), this.base2.getY());
            var2.lineTo(this.tip.getX(), this.tip.getY());
            var1.fill(var2);
         }
      }

      public void draw(Graphics2D var1) {
         if (this.tip != null) {
            var1.draw(new java.awt.geom.Line2D.Double(this.tip, this.base1));
            var1.draw(new java.awt.geom.Line2D.Double(this.tip, this.base2));
            var1.draw(new java.awt.geom.Line2D.Double(this.base1, this.base2));
         }
      }
   }
}

