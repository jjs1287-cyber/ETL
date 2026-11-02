package org.etlforge.ui.widget.overlay;

import java.awt.Dimension;
import javax.swing.JComponent;

public interface OverlayContainer {
   String BLOCKING_PROPERTY = "konga.overlay.blocking";
   String FIXED_SIZE_PROPERTY = "konga.overlay.fixedSize";
   String FIXED_POSITION_PROPERTY = "konga.overlay.fixedPosition";
   String ANIMATING_PROPERTY = "konga.overlay.animating";
   String TRANSIENT_PROPERTY = "konga.overlay.transient";
   String FILL_HORIZONTALLY_PROPERTY = "konga.overlay.fillHorizontally";
   String RESIZABLE = "konga.overlay.resizable";
   String FILL_VERTICALLY_PROPERTY = "konga.overlay.fillVertically";

   void addOverlay(JComponent var1, OverlayPositioner var2);

   void removeOverlay(JComponent var1);

   Dimension getSize();

   void addListener(OverlayContainerListener var1);

   void removeListener(OverlayContainerListener var1);
}
