package org.etlforge.application.ui.viewer.error.log;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.errorlog.ErrorLogListener;

public abstract class AbstractErrorLogDisplayer implements ErrorLogDisplayer {
   private ErrorLogListener logListener;

   @Override
   public final synchronized void setDynamic(boolean var1) {
      if (var1) {
         this.installLogListener();
      } else {
         this.uninstallLogListener();
      }
   }

   private void installLogListener() {
      if (this.logListener == null) {
         this.logListener = new AbstractErrorLogDisplayer.LogListener();
         ErrorLog.addListener(this.logListener);
      }
   }

   private void uninstallLogListener() {
      if (this.logListener != null) {
         ErrorLog.removeListener(this.logListener);
         this.logListener = null;
      }
   }

   protected abstract void handleNewError(ErrorLogEntry var1);

   private class LogListener implements ErrorLogListener {
      private LogListener() {
      }

      @Override
      public void errorOccurred(ErrorLogEntry var1) {
         AbstractErrorLogDisplayer.this.handleNewError(var1);
      }
   }
}
