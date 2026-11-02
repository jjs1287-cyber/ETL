package org.etlforge.integration.activity.ui.log;

import java.util.EnumSet;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.collections.Collections2;
import org.etlforge.functors.function.KongaFunctions;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.system.KeyValue;

final class LogStatusFilterPreference {
   private static final String KEY = "StatusFilter";
   private final KeyValue<String, String> storage;

   public LogStatusFilterPreference(KeyValue<String, String> var1) {
      this.storage = var1;
   }

   public void store(EnumSet<LogStatus> var1) {
      this.storage.set("StatusFilter", Collections2.join(var1, KongaFunctions.enumName(), ","));
   }

   public EnumSet<LogStatus> restore() {
      EnumSet var1 = EnumSet.noneOf(LogStatus.class);
      String var2 = this.storage.get("StatusFilter", null);
      if (var2 != null) {
         for (String var6 : var2.split(",")) {
            try {
               var1.add(LogStatus.valueOf(var6));
            } catch (Exception var8) {
               ErrorLog.log(this.getClass(), null, var8);
            }
         }
      }

      return var1.isEmpty() ? LogStatus.completed() : var1;
   }
}
