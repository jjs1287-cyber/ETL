package org.etlforge.application.config;

import java.io.File;
import org.etlforge.application.Application;
import org.etlforge.application.appdir.ApplicationDirectory;
import org.etlforge.application.appdir.LogDirectory;
import org.etlforge.application.errorlog.ErrorLog;

public final class FatalConfigurationError {
   public static final int ILLEGAL_VERSION = 0;
   public static final int SERVER_INITIALIZATION = 1;
   public static final int VIEW_DATA_INITIALIZATION = 2;
   public static final int ENTITY_TYPE_META_DATA_INITIALIZATION = 3;
   public static final int DATA_LOCATION_META_DATA_INITIALIZATION = 4;
   public static final int RESOURCE_LOADING = 5;
   public static final int PROJECT_PERSISTANCE_CONFIGURATION = 6;
   public static final int COMMAND_LINE_ARGUMENT_PARSING = 7;
   public static final int BUILD_PROPERTIES_INITIALIZATION = 8;
   public static final int APPLICATION_CONFIGURATION = 9;
   public static final int UNREGISTERED_ACTION_DELEGATE = 11;

   public static void throwError(int var0, Object var1, Throwable var2, String var3) {
      throwErrorImpl(var0, var1, var2, var3);
   }

   private static void throwErrorImpl(int var0, Object var1, Throwable var2, String var3) {
      Throwable var4 = var2 != null ? var2 : new Exception();
      String var5 = getErrorMessage(var0, var3);
      printToConsole(var4, var5);
      ErrorLog.fatal(var1, var5, var4);
      System.exit(1);
   }

   private static String getErrorMessage(int var0, String var1) {
      StringBuilder var2 = new StringBuilder("Fatal error [").append(var0);
      var2.append("]: The application configuration is invalid.");
      checkSupportUrl(var2);
      checkLogDir(var2);
      if (var1 != null) {
         var2.append("\n\n").append(var1);
      }

      return var2.toString();
   }

   private static void printToConsole(Throwable var0, String var1) {
      System.err.println(var1);
      var0.printStackTrace();
   }

   private static void checkSupportUrl(StringBuilder var0) {
      try {
         String var1 = ApplicationConfiguration.getSupportUrl();
         if (var1 != null) {
            var0.append("\nPlease contact etlforge support: ").append(var1);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   private static void checkLogDir(StringBuilder var0) {
      try {
         LogDirectory var1 = getLogDir();
         if (var1 != null) {
            File var2 = var1.getSessionLogFolder();
            if (var2 != null) {
               var0.append("\n\n").append("The log files for this session are located in: ").append(var2.getAbsolutePath());
            }

            File var3 = var1.getSessionErrorLog();
            if (var3 != null) {
               var0.append("\n\n").append("The error log for this session is written to: ").append(var3.getAbsolutePath());
            }
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   private static LogDirectory getLogDir() {
      ApplicationDirectory var0 = Application.getApplicationDirectory();
      return var0 == null ? null : var0.getLogDirectory();
   }

   private FatalConfigurationError() {
   }
}
