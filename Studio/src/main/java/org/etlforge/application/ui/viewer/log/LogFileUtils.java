package org.etlforge.application.ui.viewer.log;

import java.io.File;
import org.apache.commons.lang.StringUtils;

public final class LogFileUtils {
   public static String getDisplayName(File var0) {
      String var1 = var0.getName();
      if (StringUtils.endsWithIgnoreCase(var1, ".log")) {
         String var2 = StringUtils.removeEndIgnoreCase(var0.getName(), ".log");
         if (var2.indexOf(46) >= 0) {
            var2 = StringUtils.substringAfterLast(var2, ".");
         }

         return var2;
      } else {
         return var1;
      }
   }

   private LogFileUtils() {
   }
}
