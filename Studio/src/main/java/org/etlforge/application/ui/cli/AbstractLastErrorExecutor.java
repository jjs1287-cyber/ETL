package org.etlforge.application.ui.cli;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;

abstract class AbstractLastErrorExecutor implements CommandExecutor {
   @Override
   public final void execute(String[] var1) throws CommandException {
      ErrorLogEntry var2 = ErrorLog.lastError();
      if (var2 != null) {
         this.handle(var2);
      } else {
         NonBlockingAlert.info("No errors have been reported.", "No errors");
      }
   }

   protected abstract void handle(ErrorLogEntry var1);
}
