package org.etlforge.ui.graphics;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;

public enum RectangleCorner {
   UPPER_LEFT,
   UPPER_RIGHT,
   LOWER_RIGHT,
   LOWER_LEFT;

   public Point getLocation(Rectangle var1) {
      switch (this) {
         case UPPER_LEFT:
            return var1.getLocation();
         case UPPER_RIGHT:
            return new Point(var1.x + var1.width, var1.y);
         case LOWER_RIGHT:
            return new Point(var1.x + var1.width, var1.y + var1.height);
         case LOWER_LEFT:
            return new Point(var1.x, var1.y + var1.height);
         default:
            throwMissingCase("getLocation(Rectangle)");
            return null;
      }
   }

   public Cursor getResizeCursor() {
      switch (this) {
         case UPPER_LEFT:
            return Cursor.getPredefinedCursor(6);
         case UPPER_RIGHT:
            return Cursor.getPredefinedCursor(7);
         case LOWER_RIGHT:
            return Cursor.getPredefinedCursor(5);
         case LOWER_LEFT:
            return Cursor.getPredefinedCursor(4);
         default:
            throwMissingCase("getResizeCursor()");
            return null;
      }
   }

   public static RectangleCorner getCorner(Rectangle var0, Point var1, int var2) {
      for (RectangleCorner var6 : values()) {
         Point var7 = var6.getLocation(var0);
         if (var1.distance(var7) <= var2) {
            return var6;
         }
      }

      return null;
   }

   private static void throwMissingCase(String var0) {
      throw new RuntimeException("Missing case in the method " + var0 + " in the class " + RectangleCorner.class.getName());
   }
}
