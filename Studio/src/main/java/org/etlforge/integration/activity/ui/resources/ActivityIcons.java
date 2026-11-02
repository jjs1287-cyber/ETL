package org.etlforge.integration.activity.ui.resources;

import javax.swing.Icon;
import org.etlforge.ui.util.ImageLoader;

public final class ActivityIcons {
   private static final String IMAGE_PACKAGE = "org.etlforge.integration.activity.ui.resources.icons";
   public static final Icon HISTORY_16 = getIcon("History16.png");
   public static final Icon HISTORY_24 = getIcon("History24.png");
   public static final Icon HISTORY_32 = getIcon("History32.png");
   public static final Icon LOG_16 = getIcon("Logs16.png");
   public static final Icon LOG_24 = getIcon("Logs24.png");
   public static final Icon LOG_32 = getIcon("Logs32.png");
   public static final Icon SYSTEM_LOG_24 = getIcon("SystemLogs24.png");
   public static final Icon SYSTEM_LOG_32 = getIcon("SystemLogs32.png");
   public static final Icon QUEUE_16 = getIcon("Queue16.png");
   public static final Icon QUEUE_24 = getIcon("Queue24.png");
   public static final Icon QUEUE_32 = getIcon("Queue32.png");

   private static Icon getIcon(String var0) {
      return ImageLoader.getImageIcon("org.etlforge.integration.activity.ui.resources.icons", var0);
   }

   private ActivityIcons() {
   }
}
