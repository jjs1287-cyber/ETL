package org.etlforge.ui.graphics;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D.Float;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class PointUtils {
   public static Line2D getMinimumDistance(Collection<? extends Point2D> var0, Collection<? extends Point2D> var1) {
      Point2D var2 = null;
      Point2D var3 = null;
      double var4 = Double.MAX_VALUE;

      for (Point2D var7 : var0) {
         for (Point2D var9 : var1) {
            double var10 = var7.distance(var9);
            if (var10 < var4) {
               var2 = var7;
               var3 = var9;
               var4 = var10;
            }
         }
      }

      return new Float(var2, var3);
   }

   public static Point translate(Point var0, int var1, int var2) {
      Point var3 = new Point(var0);
      var3.translate(var1, var2);
      return var3;
   }

   public static Set<Point2D> translatePoints(Collection<? extends Point2D> var0, Point2D var1) {
      HashSet var2 = new HashSet();

      for (Point2D var4 : var0) {
         var2.add(new java.awt.geom.Point2D.Double(var4.getX() + var1.getX(), var4.getY() + var1.getY()));
      }

      return var2;
   }

   public static Point midPoint(Point var0, Point var1) {
      return new Point(var0.x + (var1.x - var0.x) / 2, var0.y + (var1.y - var0.y) / 2);
   }

   private PointUtils() {
   }
}
