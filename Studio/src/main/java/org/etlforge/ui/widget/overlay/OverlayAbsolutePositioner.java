package org.etlforge.ui.widget.overlay;

import java.awt.Point;
import javax.swing.JComponent;

public final class OverlayAbsolutePositioner implements OverlayPositioner {
   private final Point pos;

   public OverlayAbsolutePositioner(Point var1) {
      this.pos = new Point(var1);
   }

   @Override
   public Point getPosition(JComponent var1, JComponent var2) {
      return new Point(this.pos);
   }
}
