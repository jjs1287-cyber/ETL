package org.etlforge.ui.graph2d;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.EnumSet;
import java.util.HashSet;
import org.etlforge.ui.graphics.PointUtils;
import org.etlforge.ui.graphics.RectangleEdge;

public class TwoRectangles {
   private final Rectangle r1;
   private final Rectangle r2;

   public TwoRectangles(Rectangle var1, Rectangle var2) {
      this.r1 = new Rectangle(var1);
      this.r2 = new Rectangle(var2);
   }

   public Line2D getConnectingLine() {
      HashSet var1 = new HashSet();
      HashSet var2 = new HashSet();

      for (RectangleEdge var6 : RectangleEdge.values()) {
         for (RectangleEdge var10 : RectangleEdge.values()) {
            var1.add(this.getMidPoint(var6.getLine(this.r1)));
            var2.add(this.getMidPoint(var10.getLine(this.r2)));
         }
      }

      return PointUtils.getMinimumDistance(var1, var2);
   }

   public EnumSet<RelativePosition> getRelativePosition() {
      EnumSet var1 = EnumSet.noneOf(RelativePosition.class);
      RelativePosition var2 = this.getHorizontalRelativePosition();
      if (var2 != null) {
         var1.add(var2);
      }

      RelativePosition var3 = this.getVerticalRelativePosition();
      if (var3 != null) {
         var1.add(var3);
      }

      return var1;
   }

   private RelativePosition getHorizontalRelativePosition() {
      double var1 = this.r1.getCenterX();
      double var3 = this.r2.getCenterX();
      if (var1 < var3) {
         return RelativePosition.LEFT;
      } else {
         return var1 > var3 ? RelativePosition.RIGHT : null;
      }
   }

   private RelativePosition getVerticalRelativePosition() {
      double var1 = this.r1.getCenterY();
      double var3 = this.r2.getCenterY();
      if (var1 < var3) {
         return RelativePosition.ABOVE;
      } else {
         return var1 > var3 ? RelativePosition.BELOW : null;
      }
   }

   private Point2D getMidPoint(Line2D var1) {
      Point2D var2 = var1.getP1();
      Point2D var3 = var1.getP2();
      double var4 = (var2.getX() + var3.getX()) / 2.0;
      double var6 = (var2.getY() + var3.getY()) / 2.0;
      return new Double(var4, var6);
   }
}
