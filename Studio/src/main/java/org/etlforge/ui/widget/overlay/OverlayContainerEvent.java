package org.etlforge.ui.widget.overlay;

import com.google.common.base.Preconditions;
import java.util.EventObject;
import javax.swing.JComponent;

public class OverlayContainerEvent extends EventObject {
   private final JComponent overlay;

   public OverlayContainerEvent(OverlayContainer var1, JComponent var2) {
      super(var1);
      Preconditions.checkNotNull(var2, "overlay");
      this.overlay = var2;
   }

   public OverlayContainer getSource() {
      return (OverlayContainer)super.getSource();
   }

   public JComponent getOverlay() {
      return this.overlay;
   }
}
