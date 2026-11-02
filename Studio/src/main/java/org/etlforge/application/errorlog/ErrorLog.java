package org.etlforge.application.errorlog;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.NonInteractiveMode;
import org.etlforge.util.clipboard.ClipboardUtils;

public final class ErrorLog {
   private static final ErrorLogListeners listeners = new ErrorLogListeners();
   private static ErrorLogPersistor persistor = new ErrorLog.NoPersistor();
   private static ErrorDetailsGenerator detailsGenerator = ErrorDetailsGenerator.DEFAULT;
   private static final LinkedList<ErrorLogEntry> loggedErrors = new LinkedList<>();
   private static final Object loggedErrorsLock = new Object();
   private static final Object persistorLock = new Object();

   private ErrorLog() {
   }

   public static void log(Object var0, String var1, Throwable var2) {
      message(var0, ErrorLevel.LOG, var1, var2);
   }

   public static void internal(Object var0, String var1, Throwable var2) {
      message(var0, ErrorLevel.INTERNAL, var1, var2);
   }

   public static void attention(Object var0, String var1, Throwable var2) {
      message(var0, ErrorLevel.ATTENTION, var1, var2);
   }

   public static void fatal(Object var0, String var1, Throwable var2) {
      message(var0, ErrorLevel.FATAL, var1, var2);
   }

   public static void message(Object var0, ErrorLevel var1, String var2, Throwable var3) {
      String var4 = detailsGenerator.getDetails(var1, var3);
      String var5 = var2;
      if (StringUtils.isBlank(var5)) {
         var5 = var3.getMessage();
      }

      if (StringUtils.isBlank(var5)) {
         var5 = "An error occurred.";
      }

      log(new ErrorLogEntry(var0, var1, var5, var4, var3));
   }

   public static List<ErrorLogEntry> getLoggedErrors() {
      synchronized (loggedErrorsLock) {
         return Lists.newArrayList(loggedErrors);
      }
   }

   public static ErrorLogEntry lastError() {
      synchronized (loggedErrorsLock) {
         return loggedErrors.isEmpty() ? null : loggedErrors.getFirst();
      }
   }

   public static int getNumberOfErrors() {
      synchronized (loggedErrorsLock) {
         return loggedErrors.size();
      }
   }

   public static boolean isEmpty() {
      synchronized (loggedErrorsLock) {
         return loggedErrors.isEmpty();
      }
   }

   public static void setPersistor(ErrorLogPersistor var0) {
      synchronized (persistorLock) {
         persistor = var0 != null ? var0 : new ErrorLog.NoPersistor();
      }
   }

   public static synchronized void setDetailsGenerator(ErrorDetailsGenerator var0) {
      detailsGenerator = var0 != null ? var0 : ErrorDetailsGenerator.DEFAULT;
   }

   private static void log(ErrorLogEntry var0) {
      if (NonInteractiveMode.on()) {
         logInNonInteractiveMode(var0);
      } else {
         synchronized (persistorLock) {
            persistor.persist(var0);
         }

         synchronized (loggedErrorsLock) {
            loggedErrors.addFirst(var0);
         }

         listeners.notifyListeners(var0);
      }
   }

   private static void logInNonInteractiveMode(ErrorLogEntry var0) {
      PrintWriter var1 = new PrintWriter(System.err);

      try {
         var0.report(var1);
      } catch (IOException var6) {
         var6.printStackTrace();
      } finally {
         var1.close();
      }
   }

   public static void addListener(ErrorLogListener var0) {
      listeners.add(var0);
   }

   public static void removeListener(ErrorLogListener var0) {
      listeners.remove(var0);
   }

   public static void addListener(ErrorLevel var0, ErrorLogListener var1) {
      listeners.add(var0, var1);
   }

   public static void removeListener(ErrorLevel var0, ErrorLogListener var1) {
      listeners.remove(var0, var1);
   }

   public static void exportToClipboard() {
      String var0 = constructString();
      ClipboardUtils.copyToSystemClipboard(var0);
   }

   private static String constructString() {
      return ErrorLogEntry.toString(getLoggedErrors());
   }

   public static void exportToFile(File var0) throws IOException {
      Files.write(constructString(), var0, Charsets.UTF_8);
   }

   private static final class NoPersistor implements ErrorLogPersistor {
      private NoPersistor() {
      }

      @Override
      public void persist(ErrorLogEntry var1) {
      }
   }
}
