package org.etlforge.ui.graphics;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.Serializable;
import java.util.Comparator;

public enum RectangleComparators implements Comparator<Rectangle> {
   X {
      public int compare(Rectangle var1, Rectangle var2) {
         return Ints.compare(var1.x, var2.x);
      }
   },
   Y {
      public int compare(Rectangle var1, Rectangle var2) {
         return Ints.compare(var1.y, var2.y);
      }
   },
   WIDTH {
      public int compare(Rectangle var1, Rectangle var2) {
         return Ints.compare(var1.width, var2.width);
      }
   },
   HEIGHT {
      public int compare(Rectangle var1, Rectangle var2) {
         return Ints.compare(var1.height, var2.height);
      }
   },
   AREA {
      public int compare(Rectangle var1, Rectangle var2) {
         return Longs.compare((long)var1.width * var1.height, (long)var2.width * var2.height);
      }
   };

   RectangleComparators() {
   }

   public static final class CenterPointDistanceSorter implements Comparator<Rectangle>, Serializable {
      private static final long serialVersionUID = 1L;
      private final Point2D referencePoint;

      public CenterPointDistanceSorter(Rectangle var1) {
         this.referencePoint = getCenterPoint(var1);
      }

      public int compare(Rectangle var1, Rectangle var2) {
         double var3 = this.referencePoint.distance(getCenterPoint(var1));
         double var5 = this.referencePoint.distance(getCenterPoint(var2));
         return Doubles.compare(var3, var5);
      }

      private static Point2D getCenterPoint(Rectangle var0) {
         return new Double(var0.getCenterX(), var0.getCenterY());
      }
   }
}
