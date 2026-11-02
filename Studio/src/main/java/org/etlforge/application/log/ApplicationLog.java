package org.etlforge.application.log;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.appdir.LogDirectory;
import org.etlforge.util.SysOutHandler;

public final class ApplicationLog {
   public static final String FILE_EXTENSION = ".log";
   private static final Map<String, Logger> createdLoggers = Maps.newHashMap();
   private static final Map<Logger, FileHandler> startupHandlers = Maps.newHashMap();
   private static Logger mainLogger;
   private static LogDirectory folder;
   private static File appDirLocation;
   private static FileHandler logDump;
   private static boolean fileLoggingEnabled = true;

   public static synchronized Logger getMainLogger() {
      if (mainLogger == null) {
         mainLogger = createMainLogger();
      }

      return mainLogger;
   }

   public static synchronized void loadPreconfiguredLoggers() {
      try {
         PreConfiguredLoggersLoader.load();
      } catch (Throwable var1) {
         var1.printStackTrace();
      }
   }

   public static synchronized void setFileLoggingEnabled(boolean var0) {
      fileLoggingEnabled = var0;
   }

   public static synchronized void setFolder(LogDirectory var0) {
      folder = var0;

      for (Entry var2 : startupHandlers.entrySet()) {
         FileHandler var3 = (FileHandler)var2.getValue();
         var3.close();
         Logger var4 = (Logger)var2.getKey();
         var4.removeHandler(var3);
         configureHandlers(var4);
      }

      startupHandlers.clear();
   }

   public static synchronized void setAppDirLocation(File var0) {
      appDirLocation = var0;
   }

   public static synchronized Collection<Logger> getExistingLoggers() {
      return Lists.newArrayList(createdLoggers.values());
   }

   public static synchronized Logger getLogger(String var0, boolean var1) {
      return getLogger(var0, Level.INFO, var1);
   }

   private static Logger getLogger(String var0, Level var1, boolean var2) {
      Logger var3 = Logger.getLogger(var0);
      if (!createdLoggers.containsKey(var0)) {
         var3.setUseParentHandlers(false);
         if (WriteLogMessagesToConsoleProperty.ENABLED) {
            var3.addHandler(new SysOutHandler());
         }

         if (var2 && fileLoggingEnabled) {
            configureHandlers(var3);
         }

         Level var4 = readSavedLogLevel(var3, var1);
         var3.setLevel(var4);
         createdLoggers.put(var0, var3);
      }

      return var3;
   }

   private static Level readSavedLogLevel(Logger var0, Level var1) {
      if (folder != null) {
         Level var2 = folder.getSavedLogLevel(var0);
         if (var2 != null) {
            return var2;
         }
      }

      return var1;
   }

   public static synchronized Logger getLogger(Class<?> var0, boolean var1) {
      return getLogger(var0.getName(), var1);
   }

   public static synchronized Logger getLogger(Class<?> var0, Level var1, boolean var2) {
      return getLogger(var0.getName(), var1, var2);
   }

   private static Logger createMainLogger() {
      String var0 = "org.etlforge.integration.client";
      Logger var1 = Logger.getLogger(var0);
      var1.setUseParentHandlers(false);
      if (WriteLogMessagesToConsoleProperty.ENABLED) {
         var1.addHandler(new SysOutHandler());
      }

      var1.setLevel(Level.ALL);
      configureHandlers(var1);
      createdLoggers.put(var0, var1);
      return var1;
   }

   private static synchronized void configureHandlers(Logger var0) {
      try {
         FileHandler var1 = getFileHandler(var0);
         var1.setFormatter(new MilliSecondFormatter());
         var0.addHandler(var1);
      } catch (Exception var2) {
         var2.printStackTrace();
         var0.log(Level.SEVERE, "Unable to create file handler", var2);
      }
   }

   private static FileHandler getFileHandler(Logger var0) throws SecurityException, IOException {
      FileHandler var1 = null;
      if (folder != null) {
         var1 = new FileHandler(getFileHandlerPattern(var0.getName(), folder.getSessionLogFolder()), true);
      } else {
         if (logDump == null) {
            String var2 = getTemporaryLogFilePattern(var0);
            logDump = new FileHandler(var2);
         }

         var1 = logDump;
         startupHandlers.put(var0, var1);
      }

      setFileHandlerEncoding(var1);
      return var1;
   }

   private static String getTemporaryLogFilePattern(Logger var0) {
      if (appDirLocation == null) {
         String var1 = getFileHandlerPattern(var0.getName(), new File(SystemUtils.USER_DIR));
         File var2 = new File(var1);
         var2.deleteOnExit();
         return var1;
      } else {
         return getFileHandlerPattern("log_dump", appDirLocation);
      }
   }

   private static String getFileHandlerPattern(String var0, File var1) {
      String var2 = var1.getAbsolutePath();
      var2 = var2 + File.separator;
      var2 = var2 + var0;
      return var2 + ".log";
   }

   private static void setFileHandlerEncoding(FileHandler var0) {
      try {
         var0.setEncoding(Charsets.UTF_8.name());
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   private ApplicationLog() {
   }
}
