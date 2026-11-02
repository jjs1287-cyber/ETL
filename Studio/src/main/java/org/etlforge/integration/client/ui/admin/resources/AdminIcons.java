package org.etlforge.integration.client.ui.admin.resources;

import javax.swing.Icon;
import org.etlforge.ui.util.ImageLoader;

public final class AdminIcons {
   public static final Icon JMS_CONSOLE_16 = getIcon("JmsEngineConsole16.gif");
   public static final Icon JMS_CONSOLE_32 = getIcon("SystemConsole32.png");
   public static final Icon SCHEDULE_CONSOLE_16 = getIcon("ScheduleConsole16.png");
   public static final Icon SCHEDULE_CONSOLE_24 = getIcon("ScheduleConsole24.png");
   public static final Icon SCHEDULE_CONSOLE_32 = getIcon("ScheduleConsole32.png");
   public static final Icon SYSTEM_CONSOLE_16 = getIcon("SystemConsole16.png");
   public static final Icon SYSTEM_CONSOLE_24 = getIcon("SystemConsole24.png");
   public static final Icon SYSTEM_CONSOLE_32 = getIcon("SystemConsole32.png");
   public static final Icon USER_CONSOLE_16 = getIcon("UserConsole16.png");
   public static final Icon USER_CONSOLE_24 = getIcon("UserConsole24.png");
   public static final Icon USER_CONSOLE_32 = getIcon("UserConsole32.png");
   public static final Icon PROJECT_CONSOLE_16 = getIcon("ProjectConsole16.png");
   public static final Icon PROJECT_CONSOLE_24 = getIcon("ProjectConsole24.png");
   public static final Icon PROJECT_CONSOLE_32 = getIcon("ProjectConsole32.png");
   public static final Icon STATUS_16 = getIcon("Status16.png");
   public static final Icon STATUS_32 = getIcon("Status32.png");
   public static final Icon ACTIONS_32 = getIcon("Actions32.gif");
   private static final String IMAGE_PACKAGE = "org.etlforge.integration.client.ui.admin.resources.icons";

   private static Icon getIcon(String var0) {
      return ImageLoader.getImageIcon("org.etlforge.integration.client.ui.admin.resources.icons", var0);
   }

   private AdminIcons() {
   }
}
