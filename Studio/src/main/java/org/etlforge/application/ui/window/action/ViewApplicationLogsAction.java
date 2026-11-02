package org.etlforge.application.ui.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.Application;
import org.etlforge.application.log.LogFileRepository;
import org.etlforge.application.ui.viewer.log.LogFileWindowProviderFactory;
import org.etlforge.application.ui.window.AddContentWaitIndicator;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.ui.wait.WaitLock;

public final class ViewApplicationLogsAction extends AbstractApplicationWindowAction {
   public static final String ID = "action.Logs";

   public ViewApplicationLogsAction(ApplicationWindow var1) {
      super(var1, "action.Logs", WindowActionDictionary.DICTIONARY);
   }

   @Override
   public void execute(ActionEvent var1) {
      final WaitLock var2 = this.getWindow().startWait();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewApplicationLogsAction.this.openLogView(var2);
         }
      });
   }

   private void openLogView(WaitLock var1) {
      WindowSectionContentProvider var2 = this.getContentProvider(var1);
      this.getWindow().displayContent(var2, ApplicationWindow.EAST, null, new AddContentWaitIndicator(var1));
   }

   private WindowSectionContentProvider getContentProvider(WaitLock var1) {
      LogFileRepository var2 = Application.getApplicationDirectory().getLogDirectory().asRepository();
      return LogFileWindowProviderFactory.newContentProvider(var2);
   }
}
