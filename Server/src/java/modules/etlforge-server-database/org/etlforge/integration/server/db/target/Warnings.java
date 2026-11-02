package org.etlforge.integration.server.db.target;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import java.util.Map;

final class Warnings {
   public static int DBTARGET_WARNING_AUTO_NEXTVAL_FAILED = 1;
   private final Map<Integer, String> warnings = Maps.newHashMap();

   public void addWarning(int var1, String var2) {
      if (!this.hasWarned(var1)) {
         this.warnings.put(var1, var2);
      }
   }

   public boolean hasWarned(int var1) {
      return this.warnings.containsKey(var1);
   }

   public void collect(Optional<StringBuilder> var1) {
      if (var1.isPresent()) {
         StringBuilder var2 = (StringBuilder)var1.get();

         for (String var4 : this.warnings.values()) {
            var2.append(var4);
            var2.append("\n");
         }
      }
   }
}
