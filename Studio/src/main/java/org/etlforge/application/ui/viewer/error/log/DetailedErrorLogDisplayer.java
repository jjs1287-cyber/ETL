package org.etlforge.application.ui.viewer.error.log;

import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.ui.UiCanvas;

public class DetailedErrorLogDisplayer extends AbstractErrorLogDisplayer implements ErrorLogDisplayer {
   private final ErrorLogTable errorTable = new ErrorLogTable();

   @Override
   protected void handleNewError(ErrorLogEntry var1) {
      this.errorTable.addError(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.errorTable.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.errorTable.requestFocus();
   }
}
