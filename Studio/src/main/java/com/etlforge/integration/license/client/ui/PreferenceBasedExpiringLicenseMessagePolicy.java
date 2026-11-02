package com.etlforge.integration.license.client.ui;

import com.etlforge.integration.license.ExpiringLicenseMessagePolicy;
import com.etlforge.integration.license.LicenseInfo;
import com.etlforge.integration.license.client.ui.prefs.ExpirationWarningFrequencyPreference;
import com.etlforge.integration.license.client.ui.prefs.ExpirationWarningTresholdPreference;
import com.etlforge.integration.license.client.ui.prefs.ShowExpirationWarningOnEachLoginPreference;
import com.etlforge.integration.license.client.ui.prefs.ShowTrialInfoOnLoginPreference;
import com.etlforge.integration.license.client.ui.prefs.WarnWhenLicenseIsAboutToExpirePreference;
import java.util.Date;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerPreferences;
import org.etlforge.util.preference.KongaPreferences;
import org.etlforge.util.time.DateUtils;

public final class PreferenceBasedExpiringLicenseMessagePolicy implements ExpiringLicenseMessagePolicy {
   private static final String TIME_OF_LAST_WARNING_PREF = "LastLicenseExpirationWarning";

   @Override
   public boolean shouldMessageBeShown(LicenseInfo var1) {
      return var1.isTrial() ? this.handleTrialLicense() : this.handleCommercialLicense(var1);
   }

   private boolean handleTrialLicense() {
      return ShowTrialInfoOnLoginPreference.get();
   }

   private boolean handleCommercialLicense(LicenseInfo var1) {
      boolean var2 = this.calculateValueForCommercialLicense(var1);
      if (var2) {
         this.updateTimeOfLastWarning(var1);
      }

      return var2;
   }

   private boolean calculateValueForCommercialLicense(LicenseInfo var1) {
      if (this.areWarningsDisabled()) {
         return false;
      } else if (this.isNumberOfRemainingDaysOutsideOfTreshold(var1)) {
         return false;
      } else {
         return ShowExpirationWarningOnEachLoginPreference.get() ? true : this.isItTimeForNewWarning(var1);
      }
   }

   private boolean isNumberOfRemainingDaysOutsideOfTreshold(LicenseInfo var1) {
      return var1.getRemainingDays() > ExpirationWarningTresholdPreference.get();
   }

   private boolean areWarningsDisabled() {
      return !WarnWhenLicenseIsAboutToExpirePreference.get();
   }

   private boolean isItTimeForNewWarning(LicenseInfo var1) {
      long var2 = this.getTimeOfLastWarning(var1);
      if (var2 <= 0L) {
         return true;
      }

      Date var4 = new Date();
      Date var5 = new Date(var2);
      return var4.before(var5) ? true : DateUtils.getDaysAgo(var5) >= ExpirationWarningFrequencyPreference.get();
   }

   private long getTimeOfLastWarning(LicenseInfo var1) {
      KongaPreferences var2 = ServerPreferences.forServer(var1.getServerInfo());
      return var2.getLong("LastLicenseExpirationWarning", 0L);
   }

   private void updateTimeOfLastWarning(LicenseInfo var1) {
      this.setTimeOfLastWarning(var1.getServerInfo(), System.currentTimeMillis());
   }

   void setTimeOfLastWarning(ServerInfo var1, long var2) {
      KongaPreferences var4 = ServerPreferences.forServer(var1);
      var4.putLong("LastLicenseExpirationWarning", var2);
   }
}
