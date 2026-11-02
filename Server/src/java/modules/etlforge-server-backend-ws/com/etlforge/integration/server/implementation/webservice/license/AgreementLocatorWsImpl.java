package com.etlforge.integration.server.implementation.webservice.license;

import com.etlforge.integration.server.license.EzLicLicenseChecker;
import com.etlforge.integration.server.license.InstalledRtfLicenseAgreements;
import com.etlforge.integration.server.license.KeyParser;
import com.etlforge.integration.server.license.LicenseAgreementLocator;
import com.etlforge.integration.server.license.LicenseChecker;
import com.etlforge.integration.server.license.LicenseKeyBaseGenerator;
import com.etlforge.integration.server.license.MaskedServerGuidKeyBaseGenerator;
import java.io.InputStream;
import java.sql.Connection;
import java.util.UUID;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.db.ServerGuidDatabaseLookup;
import org.etlforge.io.Folder;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.util.zip.ZipUtils;

final class AgreementLocatorWsImpl {
   private final LicenseKeyBaseGenerator generator;
   private Folder tempFolder;

   public AgreementLocatorWsImpl(Connection var1) {
      ServerGuidDatabaseLookup var2 = new ServerGuidDatabaseLookup(var1);
      this.generator = new MaskedServerGuidKeyBaseGenerator(var2);
   }

   public WsKeyValue[] locateAndZipLicense(String var1) {
      String var2 = "";
      String var3 = "";

      try {
         var2 = this.doWork(var1);
      } catch (Exception var5) {
         var3 = var5.getMessage();
      }

      return new WsKeyValue[]{new WsKeyValue("agreement", var2), new WsKeyValue("error", compressError(var3))};
   }

   private static String compressError(String var0) {
      try {
         return ZipUtils.deflateAndBase64EncodeString(var0);
      } catch (Exception var2) {
         var2.printStackTrace();
         return "An error occurred on the server, but the error message was lost in the transfer to the client. Please contact ETLForge support.";
      }
   }

   private String doWork(String var1) throws Exception {
      String var2 = this.unpackLicenseKey(var1);
      InputStream var3 = null;
      this.tempFolder = this.createTempFolder();

      try {
         LicenseAgreementLocator var4 = this.createAgreementLocator();
         var3 = var4.locateLicenseAgreement(var2, this.generator.getKeyBase());
         return ZipUtils.deflateAndBase64EncodeInputStream(var3);
      } finally {
         KongaIoUtils.close(var3);
         this.tempFolder.delete();
      }
   }

   private String unpackLicenseKey(String var1) throws Exception {
      KeyParser var2 = new KeyParser();
      return var2.parseCompressedData(var1);
   }

   private LicenseAgreementLocator createAgreementLocator() {
      LicenseChecker var1 = this.createLicenseChecker();
      InstalledRtfLicenseAgreements var2 = new InstalledRtfLicenseAgreements();
      return new LicenseAgreementLocator(var1, var2);
   }

   private LicenseChecker createLicenseChecker() {
      return new EzLicLicenseChecker(this.tempFolder.getAbsolutePath());
   }

   private Folder createTempFolder() throws Exception {
      Folder var1 = new Folder(ServerConfig.getDataInterchangeTempDir(), UUID.randomUUID().toString());
      boolean var2 = var1.createOnDisk();
      if (!var2) {
         throw new Exception(
            "Failed to create a temporary folder needed for checking the license. This is a bug. The folder that needed to be created had the path \""
               + var1.getAbsolutePath()
               + "\"."
         );
      } else {
         return var1;
      }
   }
}
