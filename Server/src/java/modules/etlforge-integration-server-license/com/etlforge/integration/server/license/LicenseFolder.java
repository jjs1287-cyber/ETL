package com.etlforge.integration.server.license;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.etlforge.integration.server.ServerDebugLog;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.io.Folder;

public final class LicenseFolder {
   private final Folder folder = new Folder(ServerConfig.getServerHome(), ".lm");

   public LicenseFolder() throws ServerConfigurationException {
   }

   public String getAbsolutePath() {
      return this.folder.getAbsolutePath();
   }

   public void writeLicenseXmlFile(String var1) throws IOException {
      this.writeLicenseFile(var1, "license.xml");
   }

   public void writeLicenseFile(String var1, String var2) throws IOException {
      this.folder.writeFileInUTF8(var2, var1);
   }

   public void deleteFiles(FileFilter var1) {
      for (File var4 : this.folder.deleteFiles(var1)) {
         ServerDebugLog.LOG.info("Failed to remove the file " + var4.getName() + " from the license folder.");
      }
   }
}
