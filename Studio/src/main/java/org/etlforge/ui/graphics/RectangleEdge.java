package org.etlforge.ui.graphics;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D.Float;
import java.awt.geom.Rectangle2D.Double;

public enum RectangleEdge {
   TOP,
   RIGHT,
   BOTTOM,
   LEFT;

   public Line2D getLine(Rectangle var1) {
      Point2D var2 = null;
      Point2D var3 = null;
      switch (this) {
         case TOP:
            var2 = new Point(var1.x, var1.y);
            var3 = new Point(var1.x + var1.width, var1.y);
            break;
         case RIGHT:
            var2 = new Point(var1.x + var1.width, var1.y);
            var3 = new Point(var1.x + var1.width, var1.y + var1.height);
            break;
         case BOTTOM:
            var2 = new Point(var1.x, var1.y + var1.height);
            var3 = new Point(var1.x + var1.width, var1.y + var1.height);
            break;
         case LEFT:
            var2 = new Point(var1.x, var1.y);
            var3 = new Point(var1.x, var1.y + var1.height);
            break;
         default:
            throw new RuntimeException("Unexpected edge: " + this);
      }

      return new Float(var2, var3);
   }

   public Rectangle getBox(Rectangle var1, int var2) {
      int var3 = 0;
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;
      switch (this) {
         case TOP:
            var3 = var1.x;
            var4 = var1.y - var2;
            var5 = var1.width;
            var6 = 2 * var2;
            break;
         case RIGHT:
            var3 = var1.x + var1.width - var2;
            var4 = var1.y;
            var5 = 2 * var2;
            var6 = var1.height;
            break;
         case BOTTOM:
            var3 = var1.x;
            var4 = var1.y + var1.height - var2;
            var5 = var1.width;
            var6 = 2 * var2;
            break;
         case LEFT:
            var3 = var1.x - var2;
            var4 = var1.y;
            var5 = 2 * var2;
            var6 = var1.height;
            break;
         default:
            throw new RuntimeException("Unexpected edge: " + this);
      }

      return new Rectangle(var3, var4, var5, var6);
   }

   public Rectangle2D getBox2D(Rectangle2D var1, double var2) {
      double var4 = 0.0;
      double var6 = 0.0;
      double var8 = 0.0;
      double var10 = 0.0;
      switch (this) {
         case TOP:
            var4 = var1.getX();
            var6 = var1.getY() - var2;
            var8 = var1.getWidth();
            var10 = 2.0 * var2;
            break;
         case RIGHT:
            var4 = var1.getX() + var1.getWidth() - var2;
            var6 = var1.getY();
            var8 = 2.0 * var2;
            var10 = var1.getHeight();
            break;
         case BOTTOM:
            var4 = var1.getX();
            var6 = var1.getY() + var1.getHeight() - var2;
            var8 = var1.getWidth();
            var10 = 2.0 * var2;
            break;
         case LEFT:
            var4 = var1.getX() - var2;
            var6 = var1.getY();
            var8 = 2.0 * var2;
            var10 = var1.getHeight();
            break;
         default:
            throw new RuntimeException("Unexpected edge: " + this);
      }

      return new Double(var4, var6, var8, var10);
   }

   public boolean isOnEdge(Rectangle var1, Point var2, int var3) {
      return this.getBox(var1, var3).contains(var2);
   }

   public boolean isOnEdge2D(Rectangle2D var1, Point2D var2, double var3) {
      return this.getBox2D(var1, var3).contains(var2);
   }

   public Cursor getResizeCursor() {
      switch (this) {
         case TOP:
            return Cursor.getPredefinedCursor(8);
         case RIGHT:
            return Cursor.getPredefinedCursor(11);
         case BOTTOM:
            return Cursor.getPredefinedCursor(9);
         case LEFT:
            return Cursor.getPredefinedCursor(10);
         default:
            throw new RuntimeException("Unexpected edge: " + this);
      }
   }

   public static RectangleEdge getEdgeForPoint(Rectangle var0, Point var1, int var2) {
      for (RectangleEdge var6 : values()) {
         if (var6.isOnEdge(var0, var1, var2)) {
            return var6;
         }
      }

      return null;
   }

   public static RectangleEdge getEdgeForPoint2D(Rectangle2D var0, Point2D var1, double var2) {
      for (RectangleEdge var7 : values()) {
         if (var7.isOnEdge2D(var0, var1, var2)) {
            return var7;
         }
      }

      return null;
   }

   public boolean isParallelTo(RectangleEdge var1) {
      switch (this) {
         case TOP:
         case BOTTOM:
            return var1 == TOP || var1 == BOTTOM;
         case RIGHT:
         case LEFT:
            return var1 == LEFT || var1 == RIGHT;
         default:
            throw new RuntimeException("Unexpected edge: " + this);
      }
   }
}
