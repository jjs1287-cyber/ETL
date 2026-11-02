package org.etlforge.application.ui.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.TempFolderConsole;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WindowWaitService;

public final class OpenTempFolderConsoleAction extends AbstractAction {
   public OpenTempFolderConsoleAction() {
      super("&Temp Folder Console");
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            OpenTempFolderConsoleAction.this.openDialog();
         }
      });
   }

   private void openDialog() {
      final TempFolderConsole var1 = new TempFolderConsole();
      KongaDialog var2 = new KongaDialog(UiUtils.getActiveWindow(), "Temp Folder Console") {
         @Override
         protected void onDialogOpened() {
            super.onDialogOpened();
            var1.update();
         }
      };
      var2.standardLayout(var1, KongaDialog.CLOSE_ONLY);
      var1.setWaitService(new WindowWaitService(var2));
      var2.setResizable(false);
      var2.setLocationRelativeTo(null);
      var2.setCloseButtonAsDefault();
      var2.setVisible(true);
   }
}
