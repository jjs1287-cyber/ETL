package org.etlforge.ui.widget.overlay;

import java.util.EventListener;

public interface OverlayContainerListener extends EventListener {
   void overlayAdded(OverlayContainerEvent var1);

   void overlayRemoved(OverlayContainerEvent var1);

   abstract class Adapter implements OverlayContainerListener {
      @Override
      public void overlayAdded(OverlayContainerEvent var1) {
      }

      @Override
      public void overlayRemoved(OverlayContainerEvent var1) {
      }
   }
}
