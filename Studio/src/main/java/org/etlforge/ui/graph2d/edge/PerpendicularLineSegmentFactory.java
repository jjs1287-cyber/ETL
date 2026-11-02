package org.etlforge.ui.graph2d.edge;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graphics.RectangleEdge;

public class PerpendicularLineSegmentFactory implements LineSegmentFactory {
   private final Graph2D graph;
   private final Object from;
   private final Object to;
   private final int minSegmentLength;
   private SingleLineSegmentFactory singleLineFactory;
   private Point2D startPoint;
   private Point2D endPoint;
   private double dx;
   private double dy;
   private Rectangle startBounds;
   private Rectangle endBounds;
   private RectangleEdge startEdge;
   private RectangleEdge endEdge;

   public PerpendicularLineSegmentFactory(Graph2D var1, GraphEdge var2) {
      this(var1, var2.from(), var2.to());
   }

   public PerpendicularLineSegmentFactory(Graph2D var1, Object var2, Object var3) {
      this.graph = var1;
      this.from = var2;
      this.to = var3;
      this.minSegmentLength = 5;
   }

   @Override
   public Line2D[] createSegments(Point2D var1, Point2D var2) {
      this.startPoint = var1;
      this.endPoint = var2;
      if (this.isOneSegmentEnough()) {
         return this.createOneSegment();
      } else {
         return this.isTwoSegmentsEnough() ? this.createTwoSegments() : this.createThreeSegments();
      }
   }

   private boolean isOneSegmentEnough() {
      return this.isShortestSegmentTooShort() || this.isNodeBoundsMissing() || this.isNodeEdgeUndetermined();
   }

   private boolean isShortestSegmentTooShort() {
      this.dx = Math.abs(this.startPoint.getX() - this.endPoint.getX());
      if (this.dx < this.minSegmentLength) {
         return true;
      }

      this.dy = Math.abs(this.startPoint.getY() - this.endPoint.getY());
      return this.dy < this.minSegmentLength;
   }

   private boolean isNodeBoundsMissing() {
      this.startBounds = this.graph.getLayout().getBounds(this.from);
      if (this.startBounds == null) {
         return true;
      }

      this.endBounds = this.graph.getLayout().getBounds(this.to);
      return this.endBounds == null;
   }

   private boolean isNodeEdgeUndetermined() {
      this.startEdge = RectangleEdge.getEdgeForPoint2D(this.startBounds, this.startPoint, 1.0);
      if (this.startEdge == null) {
         return true;
      }

      this.endEdge = RectangleEdge.getEdgeForPoint2D(this.endBounds, this.endPoint, 1.0);
      return this.endEdge == null || this.endEdge == this.startEdge;
   }

   private Line2D[] createOneSegment() {
      if (this.singleLineFactory == null) {
         this.singleLineFactory = new SingleLineSegmentFactory();
      }

      return this.singleLineFactory.createSegments(this.startPoint, this.endPoint);
   }

   private boolean isTwoSegmentsEnough() {
      return !this.startEdge.isParallelTo(this.endEdge);
   }

   private Line2D[] createTwoSegments() {
      Point2D var1 = null;
      switch (this.startEdge) {
         case TOP:
         case BOTTOM:
            var1 = new Double(this.startPoint.getX(), this.endPoint.getY());
            break;
         case LEFT:
         case RIGHT:
            var1 = new Double(this.endPoint.getX(), this.startPoint.getY());
            break;
         default:
            throw new RuntimeException("Unexpected edge: " + this.startEdge);
      }

      return new Line2D[]{new java.awt.geom.Line2D.Double(this.startPoint, var1), new java.awt.geom.Line2D.Double(var1, this.endPoint)};
   }

   private Line2D[] createThreeSegments() {
      Point2D[] var1 = new Point2D[2];
      switch (this.startEdge) {
         case TOP:
            this.makeMidSegmentHorizontal(var1, -1);
            break;
         case BOTTOM:
            this.makeMidSegmentHorizontal(var1, 1);
            break;
         case LEFT:
            this.makeMidSegmentVertical(var1, -1);
            break;
         case RIGHT:
            this.makeMidSegmentVertical(var1, 1);
      }

      return new Line2D[]{
         new java.awt.geom.Line2D.Double(this.startPoint, var1[0]),
         new java.awt.geom.Line2D.Double(var1[0], var1[1]),
         new java.awt.geom.Line2D.Double(var1[1], this.endPoint)
      };
   }

   private void makeMidSegmentVertical(Point2D[] var1, int var2) {
      double var3 = this.startPoint.getX() + var2 * (this.dx / 2.0);
      var1[0] = new Double(var3, this.startPoint.getY());
      var1[1] = new Double(var3, this.endPoint.getY());
   }

   private void makeMidSegmentHorizontal(Point2D[] var1, int var2) {
      double var3 = this.startPoint.getY() + var2 * (this.dy / 2.0);
      var1[0] = new Double(this.startPoint.getX(), var3);
      var1[1] = new Double(this.endPoint.getX(), var3);
   }
}
