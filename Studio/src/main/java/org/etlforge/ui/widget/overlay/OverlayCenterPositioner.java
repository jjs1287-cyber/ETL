package org.etlforge.ui.widget.overlay;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JComponent;

public final class OverlayCenterPositioner implements OverlayPositioner {
   @Override
   public Point getPosition(JComponent var1, JComponent var2) {
      Dimension var3 = var2.getSize();
      int var4 = Math.max((var1.getWidth() - var3.width) / 2, 0);
      int var5 = Math.max((var1.getHeight() - var3.height) / 2, 0);
      return new Point(var4, var5);
   }
}
