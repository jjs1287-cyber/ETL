package com.etlforge.integration.license;

public interface LicenseMessenger {
   void showLicenseExpiredMessage(LicenseInfo var1);

   void showTrialLicenseMessage(LicenseInfo var1);

   void showCommercialLicenseAlmostExpiredMessage(LicenseInfo var1);
}
