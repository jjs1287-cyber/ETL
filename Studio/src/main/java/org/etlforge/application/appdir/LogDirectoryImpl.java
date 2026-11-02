package org.etlforge.application.appdir;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.DebugMode;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.errorlog.ErrorLogFilePersistor;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.log.ApplicationLogHistoryPreference;
import org.etlforge.application.log.LogFileRepository;
import org.etlforge.io.MultiplePrintStream;
import org.etlforge.util.properties.PropertiesReader;
import org.etlforge.util.properties.PropertiesWriter;

final class LogDirectoryImpl extends ApplicationDirectoryBase implements LogDirectory {
   private static final String NAME = "logs";
   static final FileFilter LOG_FILE_FILTER = new LogDirectoryImpl.LogFileFilter();
   static final String SYSTEM_OUT_NAME = "system.out";
   static final String SYSTEM_ERR_NAME = "system.err";
   private static final String LEVELS_STORAGE = "loglevels.properties";
   private static final long AGE_LIMIT = TimeUnit.DAYS.toMillis(ApplicationLogHistoryPreference.get());
   private final Date sessionTimestamp = new Date();
   private final ErrorLogFilePersistor errorLogFileWriter;
   private final File sessionLogLocation = this.createSessionLogLocation();
   private final Object sessionLock;
   private Properties storedLevels;

   LogDirectoryImpl(File var1) throws AppDirInitializationException {
      super(var1, "logs");
      this.errorLogFileWriter = new ErrorLogFilePersistor(this.sessionLogLocation);
      ErrorLog.setPersistor(this.errorLogFileWriter);
      this.sessionLock = new Object();
      new LogDirectoryImpl.SystemErrRedirector().redirect();
      new LogDirectoryImpl.SystemOutRedirector().redirect();
      new LogDirectoryImpl.FileCleanup().start();
   }

   private File createSessionLogLocation() {
      File var1 = new File(this.getLocation(), String.valueOf(this.sessionTimestamp.getTime()));

      try {
         FileUtils.forceMkdir(var1);
      } catch (IOException var3) {
         ErrorLog.log(ApplicationLog.class, "Failed to create the log folder: " + var3.getMessage(), var3);
      }

      return var1;
   }

   @Override
   public File getSessionErrorLog() {
      return this.errorLogFileWriter.getFile();
   }

   @Override
   public File getSessionLogFolder() {
      return this.sessionLogLocation;
   }

   @Override
   public LogFileRepository asRepository() {
      synchronized (this.sessionLock) {
         return new LogFileRepositoryImpl(this.getLocation());
      }
   }

   @Override
   public Level getSavedLogLevel(Logger var1) {
      synchronized (this.sessionLock) {
         if (this.storedLevels == null) {
            this.storedLevels = this.readStoredLevels();
         }

         String var3 = var1.getName();
         String var4 = this.storedLevels.getProperty(var3);
         if (var4 != null) {
            Level var10000;
            try {
               var10000 = Level.parse(var4);
            } catch (IllegalArgumentException var7) {
               ErrorLog.log(this.getClass(), null, var7);
               return null;
            }

            return var10000;
         } else {
            return null;
         }
      }
   }

   private Properties readStoredLevels() {
      File var1 = this.getStoredLevelsFile();
      if (var1.canRead()) {
         try {
            return PropertiesReader.read(var1);
         } catch (IOException var3) {
            ErrorLog.log(this.getClass(), null, var3);
         }
      }

      return new Properties();
   }

   private File getStoredLevelsFile() {
      return new File(this.getLocation(), "loglevels.properties");
   }

   @Override
   public void saveLogLevels(Logger... var1) {
      synchronized (this.sessionLock) {
         if (this.storedLevels == null) {
            this.storedLevels = this.readStoredLevels();
         }

         for (Logger var6 : var1) {
            this.storedLevels.setProperty(var6.getName(), var6.getLevel().getName());
         }

         this.saveStoredLevelsToDisk();
      }
   }

   private void saveStoredLevelsToDisk() {
      try {
         File var1 = this.getStoredLevelsFile();
         PropertiesWriter.write(this.storedLevels, var1);
      } catch (IOException var2) {
         ErrorLog.log(this.getClass(), null, var2);
      }
   }

   private class FileCleanup {
      private FileCleanup() {
      }

      public void start() {
         Thread var1 = new Thread(new Runnable() {
            @Override
            public void run() {
               synchronized (LogDirectoryImpl.this.sessionLock) {
                  FileCleanup.this.cleanupFiles();
               }
            }
         });
         var1.setPriority(2);
         var1.start();
      }

      private void cleanupFiles() {
         for (File var4 : LogDirectoryImpl.this.getLocation().listFiles()) {
            if (var4.isDirectory()) {
               this.handleDirectory(var4);
            } else {
               this.handleFile(var4);
            }
         }
      }

      private void handleDirectory(File var1) {
         if (this.shouldDirectoryBeDeleted(var1)) {
            try {
               FileUtils.deleteDirectory(var1);
            } catch (IOException var3) {
               ErrorLog.log(LogDirectoryImpl.class, "Failed to delete old client log directory: " + var3.getMessage(), var3);
            }
         }
      }

      private boolean shouldDirectoryBeDeleted(File var1) {
         try {
            Date var2 = new Date(Long.parseLong(var1.getName()));
            return this.isTooOld(var2);
         } catch (NumberFormatException var3) {
            return false;
         }
      }

      private boolean isTooOld(Date var1) {
         return LogDirectoryImpl.this.sessionTimestamp.getTime() - var1.getTime() > LogDirectoryImpl.AGE_LIMIT;
      }

      private void handleFile(File var1) {
         if (this.shouldFileBeDeleted(var1)) {
            try {
               FileUtils.forceDelete(var1);
            } catch (IOException var3) {
               ErrorLog.log(LogDirectoryImpl.class, "Failed to delete old client log file: " + var3.getMessage(), var3);
            }
         }
      }

      private boolean shouldFileBeDeleted(File var1) {
         String var2 = var1.getName();
         return !var2.equals("README.txt") && !var2.equals("loglevels.properties");
      }
   }

   private static class LogFileFilter implements FileFilter {
      private LogFileFilter() {
      }

      @Override
      public boolean accept(File var1) {
         if (var1.isFile()) {
            String var2 = var1.getName();
            if (!var2.equals("system.err") && !var2.equals("system.out")) {
               String var3 = org.etlforge.io.FileUtils.getExtension(var1);
               return !"lck".equalsIgnoreCase(var3);
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   private abstract class StreamRedirector {
      private StreamRedirector() {
      }

      protected final void redirect(PrintStream var1, String var2, String var3) {
         try {
            File var4 = new File(LogDirectoryImpl.this.sessionLogLocation, var3);
            if (!var4.exists() && !var4.createNewFile()) {
               throw new IOException("Could not create the file " + var4);
            }

            PrintStream var5 = null;
            if (this.shouldPrintToConsole(var2)) {
               PrintStream[] var6 = new PrintStream[]{var1};
               var5 = new MultiplePrintStream(var4, var6);
            } else {
               var5 = new PrintStream(var4);
            }

            this.updateSystem(var5);
         } catch (FileNotFoundException var7) {
            var7.printStackTrace();
         } catch (IOException var8) {
            var8.printStackTrace();
         }
      }

      protected abstract void updateSystem(PrintStream var1);

      private boolean shouldPrintToConsole(String var1) {
         return Boolean.getBoolean(var1) || DebugMode.IS_ENABLED;
      }
   }

   private class SystemErrRedirector extends LogDirectoryImpl.StreamRedirector {
      private SystemErrRedirector() {
      }

      public void redirect() {
         this.redirect(System.err, "SystemErrToConsole", "system.err");
      }

      @Override
      protected void updateSystem(PrintStream var1) {
         System.setErr(var1);
      }
   }

   private class SystemOutRedirector extends LogDirectoryImpl.StreamRedirector {
      private SystemOutRedirector() {
      }

      public void redirect() {
         this.redirect(System.out, "SystemOutToConsole", "system.out");
      }

      @Override
      protected void updateSystem(PrintStream var1) {
         System.setOut(var1);
      }
   }
}
