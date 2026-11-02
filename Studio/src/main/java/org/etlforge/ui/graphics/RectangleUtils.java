package org.etlforge.ui.graphics;

import java.awt.Point;
import java.awt.Rectangle;

public final class RectangleUtils {
   public static Rectangle pad(Rectangle var0, int var1, int var2) {
      Rectangle var3 = new Rectangle(var0);
      return padMe(var3, var1, var2);
   }

   public static Rectangle padMe(Rectangle var0, int var1, int var2) {
      var0.x -= var1;
      var0.y -= var2;
      var0.width += 2 * var2;
      var0.height += 2 * var2;
      return var0;
   }

   public static Rectangle union(Iterable<? extends Rectangle> var0) {
      Rectangle var1 = null;

      for (Rectangle var3 : var0) {
         if (var1 == null) {
            var1 = new Rectangle(var3);
         } else {
            var1.add(var3);
         }
      }

      return var1;
   }

   public static Rectangle translate(Rectangle var0, int var1, int var2) {
      Rectangle var3 = new Rectangle(var0);
      var3.translate(var1, var2);
      return var3;
   }

   public static Point getCenterPoint(Rectangle var0) {
      return new Point((int)var0.getCenterX(), (int)var0.getCenterY());
   }

   public static Rectangle center(Rectangle var0, Point var1) {
      Rectangle var2 = new Rectangle(var0);
      centerMe(var2, var1);
      return var2;
   }

   public static void centerMe(Rectangle var0, Point var1) {
      Point var2 = getCenterPoint(var0);
      var0.translate(var1.x - var2.x, var1.y - var2.y);
   }

   public static String toString(Rectangle var0) {
      return var0.x + "," + var0.y + "," + var0.width + "," + var0.height;
   }

   public static Rectangle fromString(String var0) {
      String[] var1 = var0.split(",");
      if (var1.length != 4) {
         throw new IllegalArgumentException("Invalid input string: " + var0);
      }

      try {
         return new Rectangle(Integer.parseInt(var1[0]), Integer.parseInt(var1[1]), Integer.parseInt(var1[2]), Integer.parseInt(var1[3]));
      } catch (NumberFormatException var3) {
         throw new IllegalArgumentException("Invalid input string: " + var0);
      }
   }

   private RectangleUtils() {
   }
}
