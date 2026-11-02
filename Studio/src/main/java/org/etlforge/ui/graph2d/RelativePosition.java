package org.etlforge.ui.graph2d;

import java.awt.Rectangle;

public enum RelativePosition {
   ABOVE,
   RIGHT,
   BELOW,
   LEFT;

   public RelativePosition opposite() {
      switch (this) {
         case ABOVE:
            return BELOW;
         case RIGHT:
            return LEFT;
         case BELOW:
            return ABOVE;
         case LEFT:
            return RIGHT;
         default:
            throw new RuntimeException();
      }
   }

   public boolean isTrue(Rectangle var1, Rectangle var2) {
      if (this.isVertical()) {
         double var3 = var1.getCenterY() - var2.getCenterY();
         if (var3 < 0.0) {
            return this == ABOVE;
         }

         if (var3 > 0.0) {
            return this == BELOW;
         }
      } else {
         double var5 = var1.getCenterX() - var2.getCenterX();
         if (var5 < 0.0) {
            return this == LEFT;
         }

         if (var5 > 0.0) {
            return this == RIGHT;
         }
      }

      return false;
   }

   private boolean isVertical() {
      return this == ABOVE || this == BELOW;
   }
}
