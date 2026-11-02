package org.etlforge.ui.widget.overlay;

import com.google.common.base.Preconditions;
import java.awt.Point;
import javax.swing.JComponent;

public final class CombinationOverlayPositioner implements OverlayPositioner {
   private final OverlayPositioner xProvider;
   private final OverlayPositioner yProvider;

   public CombinationOverlayPositioner(OverlayPositioner var1, OverlayPositioner var2) {
      Preconditions.checkNotNull(var1, "xProvider");
      Preconditions.checkNotNull(var2, "yProvider");
      this.xProvider = var1;
      this.yProvider = var2;
   }

   @Override
   public Point getPosition(JComponent var1, JComponent var2) {
      int var3 = this.xProvider.getPosition(var1, var2).x;
      int var4 = this.yProvider.getPosition(var1, var2).y;
      return new Point(var3, var4);
   }
}
