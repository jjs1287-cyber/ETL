package org.etlforge.ui.widget.overlay;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JComponent;
import org.etlforge.lang.Intervals;

public final class OverlayBottomPositioner implements OverlayPositioner {
   private int xMargin;
   private int yMargin;

   public OverlayBottomPositioner() {
      this(0, 0);
   }

   public OverlayBottomPositioner(int var1, int var2) {
      this.xMargin = var1;
      this.yMargin = var2;
   }

   @Override
   public Point getPosition(JComponent var1, JComponent var2) {
      Dimension var3 = var2.getSize();
      int var4 = this.xMargin;
      int var5 = Intervals.valueWithin(var1.getHeight() - var3.height - this.yMargin, 0, var1.getHeight());
      return new Point(var4, var5);
   }

   public void setXMargin(int var1) {
      this.xMargin = var1;
   }

   public void setYMargin(int var1) {
      this.yMargin = var1;
   }
}
