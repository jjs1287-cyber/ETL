package org.etlforge.application.ui.window;

import org.etlforge.application.DebugMode;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.errorlog.ErrorLogListener;
import org.etlforge.application.ui.window.action.ShowErrorLogAction;
import org.etlforge.ui.UiCanvas;

public final class ErrorLogQuickLauncher implements QuickLauncher {
   private final QuickLauncher impl;
   private boolean hideIfNoErrors;

   public ErrorLogQuickLauncher(ApplicationWindow var1) {
      ShowErrorLogAction var2 = new ShowErrorLogAction(var1);
      this.impl = new ActionBasedApplicationWindowQuickLauncher(var2);
      this.hideIfNoErrors = true;
   }

   public void setHideIfNoErrors(boolean var1) {
      this.hideIfNoErrors = var1;
   }

   @Override
   public void publish() {
      if (this.hideIfNoErrors && ErrorLog.isEmpty()) {
         this.delayPublish();
      } else {
         this.impl.publish();
      }
   }

   @Override
   public void hide() {
      this.impl.hide();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.impl.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.impl.requestFocus();
   }

   private void delayPublish() {
      ErrorLogQuickLauncher.DelayedPublisher var1 = new ErrorLogQuickLauncher.DelayedPublisher();
      ErrorLog.addListener(var1);
   }

   private final class DelayedPublisher implements ErrorLogListener {
      private DelayedPublisher() {
      }

      @Override
      public void errorOccurred(ErrorLogEntry var1) {
         if (this.shouldPublish(var1)) {
            ErrorLogQuickLauncher.this.impl.publish();
         }
      }

      private boolean shouldPublish(ErrorLogEntry var1) {
         return var1.level() == ErrorLevel.INTERNAL ? DebugMode.IS_ENABLED : true;
      }
   }
}
