package org.etlforge.application.ui.cli;

import org.etlforge.application.errorlog.ErrorLogEntry;

final class CopyLastErrorExecutor extends AbstractLastErrorExecutor {
   @Override
   protected void handle(ErrorLogEntry var1) {
      var1.copyToClipboard();
   }
}
