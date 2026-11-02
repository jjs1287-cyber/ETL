package com.etlforge.integration.server.implementation.webservice.serveradmin;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.services.ServerRestarter;
import org.etlforge.util.zip.ZipUtils;

final class UploadServerConfigFileTask extends Task<Void> {
   private final String deflatedEncodedString;

   public UploadServerConfigFileTask(String var1, String var2, String var3) {
      super("uploadServerConfigurationFile", var1, var2);
      this.deflatedEncodedString = var3;
   }

   protected Void runTask() throws Exception {
      String var1 = unpackServerConfigurationFileFromClient(this.deflatedEncodedString);
      UploadServerConfigFileTask.ConfigFileUpdater var2 = new UploadServerConfigFileTask.ConfigFileUpdater(ServerConfig.getServerConfigFile(), var1);
      var2.doUpdate();
      return null;
   }

   private static String unpackServerConfigurationFileFromClient(String var0) throws Exception {
      String var1 = ZipUtils.base64DecodeAndInflateString(var0);
      if (SystemUtils.IS_OS_WINDOWS && var1.indexOf(SystemUtils.LINE_SEPARATOR) >= 0) {
         return var1;
      }

      if (!SystemUtils.LINE_SEPARATOR.equals("\n")) {
         var1 = var1.replaceAll("\\n", SystemUtils.LINE_SEPARATOR);
      }

      return var1;
   }

   private static class ConfigFileUpdater {
      private final File configFile;
      private final String newContents;
      private final String oldContents;

      public ConfigFileUpdater(File var1, String var2) {
         this.configFile = var1;
         this.newContents = var2;
         this.oldContents = this.readCurrentContents();
      }

      public void doUpdate() throws Exception {
         ServerConfig.replace(this.newContents);
         this.restartServer();
      }

      private String readCurrentContents() {
         if (this.configFile.exists()) {
            try {
               return FileUtils.readFileToString(this.configFile, null);
            } catch (IOException var2) {
            }
         }

         return null;
      }

      private void restartServer() {
         Thread var1 = new Thread() {
            @Override
            public void run() {
               try {
                  Thread.sleep(1000L);
                  ServerRestarter var1x = ServerRestarter.getRestarter();
                  var1x.restartServer();
               } catch (Exception var2) {
                  ConfigFileUpdater.this.restoreBackup();
               }
            }
         };
         var1.start();
      }

      private void restoreBackup() {
         if (this.oldContents != null) {
            try {
               ServerConfig.replace(this.oldContents);
            } catch (Exception var2) {
            }
         }
      }
   }
}
