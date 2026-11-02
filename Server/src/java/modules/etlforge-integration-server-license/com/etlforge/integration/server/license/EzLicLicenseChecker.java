package com.etlforge.integration.server.license;

import com.vs.ezlicrun.EzLicExceptionBase;
import com.vs.ezlicrun.EzLicExceptionExpired;
import com.vs.ezlicrun.EzLicExceptionInvalidHostUserNet;
import com.vs.ezlicrun.EzLicExceptionMalformedKey;
import com.vs.ezlicrun.EzLicenseInfo;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.server.exception.ServerConfigurationException;

public final class EzLicLicenseChecker implements LicenseChecker {
   private static final String VERIFY_LICENSE_MSG = "Please verify that you used the correct license key, as provided by ETLForge for this server. If the problem remains, please contact ETLForge support.";
   private final EzLicenseInfo info = new EzLicenseInfo();
   private final String hiddenFolderPath;
   private String serverNodeName;

   public EzLicLicenseChecker() throws ServerConfigurationException {
      this(new LicenseFolder().getAbsolutePath());
   }

   public EzLicLicenseChecker(String var1) {
      this.hiddenFolderPath = var1;
   }

   @Override
   public LicenseInfo checkLicenseKey(String var1, String var2) throws ServerLicenseException {
      this.serverNodeName = var2;

      try {
         this.checkLicenseImpl(var1, this.serverNodeName);
      } catch (EzLicExceptionInvalidHostUserNet var4) {
         this.tryNotNodeLockedLicense(var1);
      } catch (EzLicExceptionMalformedKey var5) {
         throw this.malformedLicense();
      } catch (EzLicExceptionExpired var6) {
         throw this.expiredLicense();
      } catch (EzLicExceptionBase var7) {
         throw this.generalLicenseError(var7);
      }

      return new LicenseInfo(this.info.getExpirationDate(), this.info.getOptions());
   }

   private void tryNotNodeLockedLicense(String var1) throws ServerLicenseException {
      try {
         this.checkLicenseImpl(var1, "");
      } catch (EzLicExceptionBase var3) {
         this.attemptToGetTheInstallationIdForTheLicense(var1);
      }
   }

   private void attemptToGetTheInstallationIdForTheLicense(String var1) throws ServerLicenseException {
      try {
         this.checkLicenseImpl(var1, null);
      } catch (EzLicExceptionBase var4) {
         String var3 = this.getInvalidUserMessage();
         throw new ServerLicenseException(var3);
      }
   }

   private void checkLicenseImpl(String var1, String var2) throws EzLicExceptionBase {
      this.info
         .checkLicenseKey(
            var1, null, null, 0, 0L, 0L, 0, 0L, var2, 0L, "ETLForge Integration Server", "bLFJFMdcYrVqd9NvbuxKV+FAFNtuY9A1", this.hiddenFolderPath, true
         );
   }

   private String getInvalidUserMessage() {
      String var1 = this.info.getUserHostNetName();
      String var2 = "The uploaded license is not compatible with this ETLForge server. ";
      if (StringUtils.isNotEmpty(var1)) {
         var2 = var2
            + "The license was generated for the ETLForge server with the following installation ID:\n\n"
            + var1
            + "\n\nThis ETLForge server has installation ID:\n\n"
            + this.serverNodeName
            + "\n\n";
      }

      return var2
         + "Please verify that you used the correct license key, as provided by ETLForge for this server. If the problem remains, please contact ETLForge support.";
   }

   private ServerLicenseException malformedLicense() {
      return new ServerLicenseException(
         "The uploaded license is invalid. Please verify that you used the correct license key, as provided by ETLForge for this server. If the problem remains, please contact ETLForge support."
      );
   }

   private ServerLicenseException expiredLicense() {
      return new ServerLicenseException(
         "The uploaded license has expired. Please verify that you used the correct license key, as provided by ETLForge for this server. If the problem remains, please contact ETLForge support."
      );
   }

   private ServerLicenseException generalLicenseError(EzLicExceptionBase var1) {
      return new ServerLicenseException(
         "There is a problem with the license: "
            + var1.getMessage()
            + "Please verify that you used the correct license key, as provided by ETLForge for this server. If the problem remains, please contact ETLForge support."
      );
   }
}
