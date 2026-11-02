package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.testhistory.TestConnectionHistoryViewFactory;

public final class ViewTestConnectionHistoryAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.TestConnectionHistory";
   private final ApplicationWindow appWin;

   public ViewTestConnectionHistoryAction(ApplicationWindow var1, CurrentProjectService var2) {
      super(var2, "action.ic.TestConnectionHistory", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewTestConnectionHistoryAction.this.openHistory();
         }
      });
   }

   private void openHistory() {
      TestConnectionHistoryViewFactory var1 = new TestConnectionHistoryViewFactory(this.getCurrentProjectService());
      this.appWin.displayContent(var1, ApplicationWindow.SOUTH, null, null);
   }
}
