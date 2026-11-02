package org.etlforge.integration.client.ui.core.window;

import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.errorlog.ErrorLogListener;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.core.resources.CoreResources;
import org.etlforge.system.Disposable;
import org.etlforge.ui.event.ApplicationMessageEvent;

final class StatusPanelErrorPublisher implements Disposable {
   private final ApplicationWindow window;
   private final ErrorLogListener logListener;

   private StatusPanelErrorPublisher(ApplicationWindow var1) {
      this.window = var1;
      this.logListener = new ErrorLogListener() {
         @Override
         public void errorOccurred(ErrorLogEntry var1) {
            if (var1.level() == ErrorLevel.LOG) {
               StatusPanelErrorPublisher.this.publishError(var1);
            }
         }
      };
      ErrorLog.addListener(this.logListener);
   }

   public static Disposable installInWindow(ApplicationWindow var0) {
      return new StatusPanelErrorPublisher(var0);
   }

   @Override
   public void dispose() {
      ErrorLog.removeListener(this.logListener);
   }

   private void publishError(ErrorLogEntry var1) {
      this.window
         .postApplicationMessageEvent(
            new ApplicationMessageEvent(var1.source(), CoreResources.STRINGS.getString("StatusPanelErrorPublisher.ExceptionMessage"), 3)
         );
   }
}
