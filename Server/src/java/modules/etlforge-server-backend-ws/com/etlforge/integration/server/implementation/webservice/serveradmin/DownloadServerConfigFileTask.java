package com.etlforge.integration.server.implementation.webservice.serveradmin;

import java.io.File;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.util.zip.ZipUtils;

final class DownloadServerConfigFileTask extends Task<String> {
   public DownloadServerConfigFileTask(String var1, String var2) {
      super("downloadServerConfigurationFile", var1, var2);
   }

   protected String runTask() throws Exception {
      File var1 = ServerConfig.getServerConfigFile();
      if (var1.exists()) {
         return ZipUtils.deflateAndBase64EncodeFileContents(var1);
      } else {
         throw new ServerConfigurationException("The configuration file " + var1.getAbsolutePath() + " does not exist");
      }
   }
}
