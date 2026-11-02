package org.etlforge.application.ui.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.viewer.error.log.ErrorLogWindowProviderFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSectionContentProvider;

public final class ShowErrorLogAction extends AbstractApplicationWindowAction {
   public static final String ID = "action.ErrorLog";

   public ShowErrorLogAction(ApplicationWindow var1) {
      super(var1, "action.ErrorLog", WindowActionDictionary.DICTIONARY);
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ShowErrorLogAction.this.openErrorLog();
         }
      });
   }

   private void openErrorLog() {
      WindowSectionContentProvider var1 = ErrorLogWindowProviderFactory.newContentProvider();
      this.getWindow().displayContent(var1, ApplicationWindow.SOUTH, null, null);
   }
}
