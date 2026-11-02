package org.etlforge.application.ui.viewer.error.dialog;

import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.viewer.error.ErrorLogEntryDisplayer;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public class ErrorDialog extends KongaDialog {
   private final ErrorLogEntryDisplayer displayer;
   private final Action closeAction = new ErrorDialog.CloseAction();

   public ErrorDialog(Window var1, ErrorLogEntry var2) {
      super(var1, getString("Error.Dialog.Title"));
      this.displayer = this.createDisplayer(var2);
      this.createDialogUi(var2);
      this.setDefaultButton(this.closeAction);
      this.pack();
      this.manageLocation(ErrorDialog.class);
   }

   public final void setDetailsShowing(boolean var1) {
      this.displayer.setDetailsShowing(var1);
   }

   private void createDialogUi(ErrorLogEntry var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(this.displayer);
      var2.withEmptyBorder(10);
      var2.asContentPaneFor(this);
   }

   private ErrorLogEntryDisplayer createDisplayer(ErrorLogEntry var1) {
      ErrorLogEntryDisplayer var2 = new ErrorLogEntryDisplayer();
      var2.display(var1);
      var2.setDisposeHandler(this.closeAction);
      return var2;
   }

   private static String getString(String var0) {
      return ApplicationResources.STRINGS.getString(var0);
   }

   private class CloseAction extends AbstractAction {
      public CloseAction() {
         super(ErrorDialog.getString("Error.Dialog.Close.Name"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ErrorDialog.this.onOk();
      }
   }
}
