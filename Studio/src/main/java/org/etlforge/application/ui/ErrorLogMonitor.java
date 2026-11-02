package org.etlforge.application.ui;

import java.awt.EventQueue;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.errorlog.ErrorLogConsoleWriter;
import org.etlforge.application.errorlog.ErrorLogConsoleWritingPreference;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.errorlog.ErrorLogListener;
import org.etlforge.application.errorlog.ErrorLogMonitorLevelPreference;
import org.etlforge.application.ui.viewer.error.dialog.ErrorDisplayer;

public final class ErrorLogMonitor implements ErrorLogListener {
   private final ErrorLogListener consoleWriter = new ErrorLogConsoleWriter();

   @Override
   public void errorOccurred(ErrorLogEntry var1) {
      if (this.isWritingToConsole()) {
         this.writeToConsole(var1);
      }

      if (this.isFatal(var1)) {
         this.handleFatalError(var1);
      } else {
         this.handleNonFatalError(var1);
      }
   }

   private boolean isWritingToConsole() {
      return ErrorLogConsoleWritingPreference.INSTANCE.get();
   }

   private void writeToConsole(ErrorLogEntry var1) {
      this.consoleWriter.errorOccurred(var1);
   }

   private boolean isFatal(ErrorLogEntry var1) {
      return var1.level() == ErrorLevel.FATAL;
   }

   private void handleFatalError(ErrorLogEntry var1) {
      ErrorDisplayer var2 = ErrorDisplayer.minimalDisplayer();
      if (EventQueue.isDispatchThread()) {
         var2.show(var1);
      } else {
         this.displayFromNonGuiThread(var2, var1);
      }
   }

   private void displayFromNonGuiThread(ErrorDisplayer var1, ErrorLogEntry var2) {
      try {
         var1.showAndWait(var2);
      } catch (InterruptedException var4) {
         Thread.currentThread().interrupt();
         var4.printStackTrace();
      }
   }

   private void handleNonFatalError(ErrorLogEntry var1) {
      if (this.shouldErrorMessageBeDisplayed(var1)) {
         this.displayErrorToUser(var1);
      }
   }

   private boolean shouldErrorMessageBeDisplayed(ErrorLogEntry var1) {
      ErrorLevel var2 = ErrorLogMonitorLevelPreference.getPreferredLevel();
      return var1.level().compareTo(var2) >= 0;
   }

   private void displayErrorToUser(ErrorLogEntry var1) {
      ErrorDisplayer.defaultDisplayer().show(var1);
   }
}
