package org.etlforge.ui.graphics;

import com.google.common.base.Predicate;
import java.awt.Point;
import java.awt.Rectangle;

public final class RectanglePredicates {
   public static Predicate<Rectangle> contains(final Rectangle var0) {
      return new Predicate<Rectangle>() {
         public boolean apply(Rectangle var1) {
            return var1.contains(var0);
         }
      };
   }

   public static Predicate<Rectangle> contains(final Point var0) {
      return new Predicate<Rectangle>() {
         public boolean apply(Rectangle var1) {
            return var1.contains(var0);
         }
      };
   }

   public static Predicate<Rectangle> intersects(final Rectangle var0) {
      return new Predicate<Rectangle>() {
         public boolean apply(Rectangle var1) {
            return var1.intersects(var0);
         }
      };
   }

   private RectanglePredicates() {
   }
}
