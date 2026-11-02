package org.etlforge.application;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.log.ApplicationLog;

final class ExitHandler {
   private static final List<ExitListener> exitListeners = new CopyOnWriteArrayList<>();
   private static final Logger logger = ApplicationLog.getMainLogger();

   public static synchronized void handleExitRequest() {
      if (isExitAllowed()) {
         exit();
      }
   }

   private static boolean isExitAllowed() {
      for (ExitListener var1 : exitListeners) {
         log("Now asking permission to exit: " + var1.getClass());
         long var2 = System.currentTimeMillis();
         if (!var1.isExitAllowed()) {
            logTimeElapsed("Request denied!", var2);
            return false;
         }

         logTimeElapsed("Request granted.", var2);
      }

      return true;
   }

   private static void log(String var0) {
      try {
         logger.info(var0);
      } catch (Throwable var2) {
      }
   }

   private static void logTimeElapsed(String var0, long var1) {
      log(var0 + " Time elapsed (ms): " + (System.currentTimeMillis() - var1));
   }

   private static void exit() {
      try {
         performExitWork();
      } finally {
         log("Good-bye!");
         System.exit(0);
      }
   }

   private static void performExitWork() {
      for (ExitListener var1 : exitListeners) {
         try {
            log("Now performing exit work: " + var1.getClass());
            long var2 = System.currentTimeMillis();
            var1.performExitWork();
            logTimeElapsed("Exit work completed.", var2);
         } catch (Exception var4) {
            ErrorLog.log(Application.class, "An unexpected error occurred in a registered ExitListener", var4);
         }
      }
   }

   public static void addListener(ExitListener var0) {
      exitListeners.add(var0);
   }

   public static void removeListener(ExitListener var0) {
      exitListeners.remove(var0);
   }

   private ExitHandler() {
   }
}
