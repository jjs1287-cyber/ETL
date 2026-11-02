package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.testhistory.TestOperationHistoryViewFactory;

public final class ViewTestOperationHistoryAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.TestOperationHistory";
   private final ApplicationWindow appWin;

   public ViewTestOperationHistoryAction(ApplicationWindow var1, CurrentProjectService var2) {
      super(var2, "action.ic.TestOperationHistory", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewTestOperationHistoryAction.this.openHistory();
         }
      });
   }

   private void openHistory() {
      TestOperationHistoryViewFactory var1 = new TestOperationHistoryViewFactory(this.getCurrentProjectService());
      this.appWin.displayContent(var1, ApplicationWindow.SOUTH, null, null);
   }
}
