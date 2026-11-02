package org.etlforge.ui.widget.window;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;

public class NorthEdgeResizeStrategy extends AbstractMouseResizeStrategy {
   public NorthEdgeResizeStrategy(Window var1) {
      super(var1, MouseResizeHotSpot.NORTH_EDGE);
   }

   @Override
   public void updateSize(Point var1) {
      Rectangle var2 = this.getBounds();
      int var3 = var2.x;
      int var4 = var1.y;
      int var5 = var2.width;
      int var6 = var2.y + var2.height - var4;
      this.setBounds(var3, var4, var5, var6);
   }
}
