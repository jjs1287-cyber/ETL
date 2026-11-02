package com.etlforge.integration.client.ui.interchange.project.backup;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.util.time.DateUtils;

final class RendererUtils {
   public static String display(DayOfBackup var0) {
      Date var1 = var0.getDate();
      if (var1.before(new Date())) {
         int var2 = DateUtils.getDaysAgo(var1);
         switch (var2) {
            case 0:
               return UiResourceBundle.getString("Today");
            case 1:
               return UiResourceBundle.getString("Yesterday");
            case 2:
            case 3:
               return MessageFormat.format(UiResourceBundle.getString("NDaysAgo"), var2);
         }
      }

      return DateFormat.getDateInstance().format(var1);
   }

   public static String tooltip(DayOfBackup var0) {
      return "Backups created on " + DateFormat.getDateInstance().format(var0.getDate());
   }

   public static String display(AvailableBackup var0) {
      return var0.getLocation().getProjectName() + " [Created at " + DateFormat.getTimeInstance(3).format(var0.getTimestamp()) + "]";
   }

   public static String tooltip(AvailableBackup var0) {
      return "<html>Created at: "
         + DateFormat.getDateTimeInstance().format(var0.getTimestamp())
         + "<br>Location: "
         + var0.getLocation().getLocationPath()
         + "</html>";
   }

   private RendererUtils() {
   }
}
