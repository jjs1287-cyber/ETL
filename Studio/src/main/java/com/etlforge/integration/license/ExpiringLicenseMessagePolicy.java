package com.etlforge.integration.license;

public interface ExpiringLicenseMessagePolicy {
   ExpiringLicenseMessagePolicy ALWAYS_SHOW_MESSAGE = new ExpiringLicenseMessagePolicy() {
      @Override
      public boolean shouldMessageBeShown(LicenseInfo var1) {
         return true;
      }
   };
   ExpiringLicenseMessagePolicy NEVER_SHOW_MESSAGE = new ExpiringLicenseMessagePolicy() {
      @Override
      public boolean shouldMessageBeShown(LicenseInfo var1) {
         return false;
      }
   };

   boolean shouldMessageBeShown(LicenseInfo var1);
}
