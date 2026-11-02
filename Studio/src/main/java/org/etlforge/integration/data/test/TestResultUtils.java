package org.etlforge.integration.data.test;

import java.util.Date;

public final class TestResultUtils {
   public static boolean isValidDate(Date var0, DetailedTestResultFilter var1) {
      Date var2 = var1.startDate();
      Date var3 = var1.endDate();
      if (var2 == null && var3 == null) {
         return true;
      } else {
         return var2 != null && var2.after(var0) ? false : var3 == null || !var3.before(var0);
      }
   }

   private TestResultUtils() {
   }
}
