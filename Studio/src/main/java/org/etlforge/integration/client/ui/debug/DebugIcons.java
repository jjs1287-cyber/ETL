package org.etlforge.integration.client.ui.debug;

import javax.swing.Icon;
import org.etlforge.ui.util.ImageLoader;

public final class DebugIcons {
   public static final Icon START_16 = getIcon("Play16.png");
   public static final Icon START_24 = getIcon("Play24.png");
   public static final Icon CONTINUE_16 = getIcon("Continue16.gif");
   public static final Icon CONTINUE_32 = getIcon("Continue24.gif");
   public static final Icon PAUSE_16 = getIcon("Pause16.png");
   public static final Icon PAUSE_24 = getIcon("Pause24.png");
   public static final Icon STOP_16 = getIcon("Stop16.png");
   public static final Icon STOP_24 = getIcon("Stop24.png");
   public static final Icon STEP_16 = getIcon("Step16.png");
   public static final Icon STEP_24 = getIcon("Step24.png");
   public static final Icon STEP_32 = getIcon("Step32.png");
   private static final String IMAGE_PACKAGE = "org.etlforge.integration.client.ui.debug.resources.icons";

   private static Icon getIcon(String var0) {
      return ImageLoader.getImageIcon("org.etlforge.integration.client.ui.debug.resources.icons", var0);
   }

   private DebugIcons() {
   }
}
