package org.etlforge.ui.graphics;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;

public final class LineUtils {
   public static Line2D findShortest(Collection<? extends Line2D> var0) {
      double var1 = Double.MAX_VALUE;
      Line2D var3 = null;

      for (Line2D var5 : var0) {
         double var6 = getLengthSquared(var5);
         if (var6 < var1) {
            var1 = var6;
            var3 = var5;
         }
      }

      return var3;
   }

   public static Point2D getMidPoint(Line2D var0) {
      double var1 = var0.getX1();
      double var3 = var0.getY1();
      return new java.awt.geom.Point2D.Double(var1 + (var0.getX2() - var1) / 2.0, var3 + (var0.getY2() - var3) / 2.0);
   }

   private static double getLengthSquared(Line2D var0) {
      double var1 = var0.getX1() - var0.getX2();
      double var3 = var0.getY1() - var0.getY2();
      return var1 * var1 + var3 * var3;
   }

   private LineUtils() {
   }
}
