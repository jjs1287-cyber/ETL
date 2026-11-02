package com.etlforge.integration.server.implementation.webservice.license;

import com.etlforge.integration.server.license.EzLicLicenseChecker;
import com.etlforge.integration.server.license.KeyParser;
import com.etlforge.integration.server.license.LicenseFolder;
import com.etlforge.integration.server.license.MaskedServerGuidKeyBaseGenerator;
import com.etlforge.integration.server.license.ServerLicenseException;
import com.etlforge.integration.server.license.db.DbBasedLicenseActivator;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.sql.Connection;
import org.etlforge.integration.server.ServerGuidLookup;
import org.etlforge.integration.server.ServerUiBackendLog;
import org.etlforge.integration.server.db.ServerGuidDatabaseLookup;
import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.util.zip.ZipUtils;

final class LicenseActivatorWsImpl {
   private final ServerGuidLookup guidLookup;
   private String error;

   public LicenseActivatorWsImpl(Connection var1) {
      this.guidLookup = new ServerGuidDatabaseLookup(var1);
      this.error = "";
   }

   public WsKeyValue getServerGuid() throws ServerConfigurationException {
      return new WsKeyValue("server-guid", this.guidLookup.lookupGuid().toString());
   }

   public WsKeyValue getError() {
      return new WsKeyValue("error", this.error);
   }

   public void activate(String var1) throws Exception {
      try {
         KeyParser var2 = new KeyParser();
         String var3 = var2.parseCompressedData(var1);
         this.checkLicense(var3);
         this.activateLicense(var3);
         var2.saveLicenseToDisk();
         this.removeOldCookieFiles();
      } catch (ServerLicenseException var4) {
         this.error = this.compressLicenseActivationError(var4);
      }
   }

   private void checkLicense(String var1) throws Exception {
      MaskedServerGuidKeyBaseGenerator var2 = new MaskedServerGuidKeyBaseGenerator(this.guidLookup);
      EzLicLicenseChecker var3 = new EzLicLicenseChecker();
      var3.checkLicenseKey(var1, var2.getKeyBase());
   }

   private void activateLicense(String var1) throws Exception {
      DbBasedLicenseActivator var2 = new DbBasedLicenseActivator();
      var2.activateLicense(var1);
   }

   private void removeOldCookieFiles() throws ServerConfigurationException {
      LicenseFolder var1 = new LicenseFolder();
      var1.deleteFiles(new FileFilter() {
         @Override
         public boolean accept(File var1) {
            return var1.getName().toLowerCase().startsWith(".ez");
         }
      });
   }

   private String compressLicenseActivationError(ServerLicenseException var1) {
      ServerUiBackendLog.LOG.throwing(this.getClass().getName(), "activateLicense", var1);

      try {
         return ZipUtils.deflateAndBase64EncodeString(var1.getMessage());
      } catch (IOException var3) {
         return "The license was not activated due to an error on the server side. The error message was lost in the transfer to the client. Please contact ETLForge support.";
      }
   }
}
