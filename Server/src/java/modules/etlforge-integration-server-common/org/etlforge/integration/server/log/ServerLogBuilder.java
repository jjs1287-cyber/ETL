package org.etlforge.integration.server.log;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.etlforge.integration.server.ServerFolders;
import org.etlforge.integration.server.conf.ServerConfig;

public class ServerLogBuilder {
   private final Class<?> owner;
   private Level level;
   private int limit;
   private int fileCount;
   private File logDir;

   public ServerLogBuilder(Class<?> var1) {
      this.owner = var1;
      this.level = Level.OFF;
      this.limit = 0;
      this.fileCount = 1;
      this.logDir = new File(ServerFolders.SERVER_HOME, "log");
   }

   public ServerLogBuilder useConfiguredLevel(String var1, String var2, Level var3) {
      this.level = getLevelFromConfig(var1, var2, var3);
      return this;
   }

   public ServerLogBuilder setLevel(Level var1) {
      this.level = var1;
      return this;
   }

   public ServerLogBuilder setLogDir(File var1) {
      this.logDir = var1;
      return this;
   }

   public ServerLogBuilder setFileLimit(int var1) {
      this.limit = var1;
      return this;
   }

   public ServerLogBuilder setFileCount(int var1) {
      this.fileCount = var1;
      return this;
   }

   public Logger createLogger() {
      Logger var1 = Logger.getLogger(this.owner.getSimpleName());
      var1.setUseParentHandlers(false);
      var1.setLevel(this.level);
      this.configureHandlers(var1);
      return var1;
   }

   private void configureHandlers(Logger var1) {
      try {
         FileHandler var2 = this.getFileHandler(var1);
         var2.setFormatter(new SimpleFormatter());
         var1.addHandler(var2);
      } catch (SecurityException var3) {
         var1.log(Level.SEVERE, "Unable to create file handler", var3);
      } catch (IOException var4) {
         var1.log(Level.SEVERE, "Unable to create file handler", var4);
      }
   }

   private FileHandler getFileHandler(Logger var1) throws SecurityException, IOException {
      Object var2 = null;
      return new FileHandler(this.getFileHandlerPattern(var1, this.logDir), this.limit, this.fileCount);
   }

   private String getFileHandlerPattern(Logger var1, File var2) {
      String var3 = var2.getAbsolutePath();
      var3 = var3 + File.separator;
      var3 = var3 + var1.getName();
      return var3 + "%g.log";
   }

   public static Level getLevelFromConfig(String var0, String var1, Level var2) {
      try {
         String var3 = ServerConfig.getOption(var0, var1, null);
         if (var3 != null) {
            return Level.parse(var3);
         }
      } catch (IllegalArgumentException var4) {
         var4.printStackTrace();
      }

      return var2;
   }
}
