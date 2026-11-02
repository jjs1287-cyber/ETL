package org.etlforge.application.ui.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.job.JobConsoleWindowProviderFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSectionContentProvider;

public final class ShowProcessConsoleAction extends AbstractApplicationWindowAction {
   public static final String ID = "action.ProcessConsole";

   public ShowProcessConsoleAction(ApplicationWindow var1) {
      super(var1, "action.ProcessConsole", WindowActionDictionary.DICTIONARY);
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ShowProcessConsoleAction.this.openConsole();
         }
      });
   }

   private void openConsole() {
      WindowSectionContentProvider var1 = JobConsoleWindowProviderFactory.newContentProvider();
      this.getWindow().displayContent(var1, ApplicationWindow.SOUTH, null, null);
   }
}
