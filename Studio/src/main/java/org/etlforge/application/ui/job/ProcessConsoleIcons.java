package org.etlforge.application.ui.job;

import javax.swing.Icon;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;

public final class ProcessConsoleIcons {
   public static final Icon PROCESS_ON_16 = get("ProcessConsoleOn.16");
   public static final Icon PROCESS_ON_24 = get("ProcessConsoleOn.24");
   public static final Icon PROCESS_OFF_16 = get("ProcessConsoleOff.16");
   public static final Icon PROCESS_OFF_24 = get("ProcessConsoleOff.24");
   public static final Icon STOP = get("Stop.16");
   public static final Icon INFO = CommonIcons.INFO_16;
   public static final Icon DELETE = CommonIcons.DELETE_16;

   private static Icon get(String var0) {
      return ApplicationResources.ICONS.getIcon(var0);
   }

   private ProcessConsoleIcons() {
   }
}
