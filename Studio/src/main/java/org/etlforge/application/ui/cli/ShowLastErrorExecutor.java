package org.etlforge.application.ui.cli;

import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.ui.viewer.error.log.ErrorLogEntryDetailsDisplayer;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.util.UiUtils;

final class ShowLastErrorExecutor extends AbstractLastErrorExecutor {
   @Override
   protected void handle(ErrorLogEntry var1) {
      ErrorLogEntryDetailsDisplayer var2 = new ErrorLogEntryDetailsDisplayer(var1);
      UiProviderDialogDisplayer.display(UiUtils.getActiveFrame(), var2, "Error Details", KongaDialog.CLOSE_ONLY, ErrorLogEntryDetailsDisplayer.class);
   }
}
