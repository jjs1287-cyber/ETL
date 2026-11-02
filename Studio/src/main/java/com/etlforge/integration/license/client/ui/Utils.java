package com.etlforge.integration.license.client.ui;

import com.etlforge.integration.license.LicenseInfo;
import com.etlforge.integration.license.client.ui.prefs.ExpirationWarningTresholdPreference;

final class Utils {
   public static boolean isExpirationDateWithinWarningTreshold(LicenseInfo var0) {
      return isExpirationDateWithinWarningTreshold(var0.getRemainingDays());
   }

   public static boolean isExpirationDateWithinWarningTreshold(int var0) {
      return var0 < ExpirationWarningTresholdPreference.get();
   }

   private Utils() {
   }
}
