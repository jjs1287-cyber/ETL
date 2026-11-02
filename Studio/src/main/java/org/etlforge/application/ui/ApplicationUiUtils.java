package org.etlforge.application.ui;

import java.awt.EventQueue;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.ui.UiThreadViolationException;

public final class ApplicationUiUtils {
   public static void logIfNotOnEDT(Class<?> var0, String var1) {
      if (!EventQueue.isDispatchThread()) {
         ErrorLog.internal(var0, var1 + " should be called on the event-dispatching thread.", new UiThreadViolationException());
      }
   }

   public static void logIfOnEDT(Class<?> var0, String var1) {
      if (EventQueue.isDispatchThread()) {
         ErrorLog.internal(var0, var1 + " should not be called on the event-dispatching thread.", new UiThreadViolationException());
      }
   }

   private ApplicationUiUtils() {
   }
}
