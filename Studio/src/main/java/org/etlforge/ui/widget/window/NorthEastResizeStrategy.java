package org.etlforge.ui.widget.window;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;

public class NorthEastResizeStrategy extends AbstractMouseResizeStrategy {
   public NorthEastResizeStrategy(Window var1) {
      super(var1, MouseResizeHotSpot.NORTHEAST_CORNER);
   }

   @Override
   public void updateSize(Point var1) {
      Rectangle var2 = this.getBounds();
      int var3 = var2.x;
      int var4 = var1.y;
      int var5 = var1.x - var3;
      int var6 = var2.y + var2.height - var4;
      this.setBounds(var3, var4, var5, var6);
   }
}
