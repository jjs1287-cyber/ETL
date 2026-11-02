package com.etlforge.integration.server.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.io.Folder;

public final class InstalledRtfLicenseAgreements implements LicenseAgreements {
   private Folder folder;

   @Override
   public InputStream getEvaluationLicense() throws ServerConfigurationException, IOException {
      return this.openLicense("evaluation.rtf");
   }

   private InputStream openLicense(String var1) throws FileNotFoundException, ServerConfigurationException {
      return new FileInputStream(this.getFolder().getFile(var1));
   }

   @Override
   public InputStream getPerpetualLicense() throws ServerConfigurationException, IOException {
      return this.openLicense("perpetual.rtf");
   }

   @Override
   public InputStream getSubscriptionLicense() throws ServerConfigurationException, IOException {
      return this.openLicense("subscription.rtf");
   }

   private Folder getFolder() throws ServerConfigurationException {
      if (this.folder == null) {
         File var1 = ServerConfig.getServerHomeDir();
         this.folder = new Folder(var1, "Resources", "Licenses");
      }

      return this.folder;
   }
}
