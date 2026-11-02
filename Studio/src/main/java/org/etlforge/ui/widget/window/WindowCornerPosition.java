package org.etlforge.ui.widget.window;

import com.google.common.base.Preconditions;
import java.awt.Point;
import java.awt.Window;
import org.etlforge.ui.graphics.RectangleCorner;

public final class WindowCornerPosition {
   private final RectangleCorner corner;
   private final Point locationOnScreen;

   public WindowCornerPosition(RectangleCorner var1, Point var2) {
      Preconditions.checkNotNull(var1, "corner");
      this.corner = var1;
      this.locationOnScreen = (Point)var2.clone();
   }

   public void position(Window var1) {
      switch (this.corner) {
         case UPPER_LEFT:
            var1.setLocation(this.locationOnScreen);
            break;
         case UPPER_RIGHT:
            int var5 = this.locationOnScreen.x - var1.getWidth();
            var1.setLocation(var5, this.locationOnScreen.y);
            break;
         case LOWER_LEFT:
            int var4 = this.locationOnScreen.y - var1.getHeight();
            var1.setLocation(this.locationOnScreen.x, var4);
            break;
         case LOWER_RIGHT:
            int var2 = this.locationOnScreen.x - var1.getWidth();
            int var3 = this.locationOnScreen.y - var1.getHeight();
            var1.setLocation(var2, var3);
      }
   }

   @Override
   public String toString() {
      return this.corner.toString() + " [" + this.locationOnScreen + "]";
   }
}
