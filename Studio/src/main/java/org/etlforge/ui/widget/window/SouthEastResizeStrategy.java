package org.etlforge.ui.widget.window;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;

public class SouthEastResizeStrategy extends AbstractMouseResizeStrategy {
   public SouthEastResizeStrategy(Window var1) {
      super(var1, MouseResizeHotSpot.SOUTHEAST_CORNER);
   }

   @Override
   public void updateSize(Point var1) {
      Rectangle var2 = this.getBounds();
      int var3 = var1.x - var2.x;
      int var4 = var1.y - var2.y;
      this.setSize(var3, var4);
   }
}
