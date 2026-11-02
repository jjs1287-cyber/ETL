package org.etlforge.integration.server.log;

import java.io.File;
import java.io.FileFilter;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.exception.ServerConfigurationException;

public class ServerLogDirectory {
   private static final FileFilter LOG_FILTER = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         return var1.isFile() && var1.getName().toLowerCase().endsWith(".log");
      }
   };
   private final File root = new File(ServerConfig.getServerHome(), "log");

   public ServerLogDirectory() throws ServerConfigurationException {
   }

   public File[] getLogFiles() {
      return getLogFiles(this.root);
   }

   public File[] getPluginLogFiles() {
      File var1 = new File(this.root, "plugin");
      return getLogFiles(var1);
   }

   private static File[] getLogFiles(File var0) {
      return var0.exists() && var0.isDirectory() ? var0.listFiles(LOG_FILTER) : new File[0];
   }
}
