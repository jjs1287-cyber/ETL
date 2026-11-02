package org.etlforge.ui.widget.window;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;

public class SouthEdgeResizeStrategy extends AbstractMouseResizeStrategy {
   public SouthEdgeResizeStrategy(Window var1) {
      super(var1, MouseResizeHotSpot.SOUTH_EDGE);
   }

   @Override
   public void updateSize(Point var1) {
      Rectangle var2 = this.getBounds();
      int var3 = var1.y - var2.y;
      this.setSize(var2.width, var3);
   }
}
