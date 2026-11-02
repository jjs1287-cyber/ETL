package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.testhistory.LoadSourceHistoryViewFactory;

public final class ViewLoadSourceHistoryAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.LoadSourceHistory";
   private final ApplicationWindow appWin;

   public ViewLoadSourceHistoryAction(ApplicationWindow var1, CurrentProjectService var2) {
      super(var2, "action.ic.LoadSourceHistory", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewLoadSourceHistoryAction.this.openHistory();
         }
      });
   }

   private void openHistory() {
      LoadSourceHistoryViewFactory var1 = new LoadSourceHistoryViewFactory(this.getCurrentProjectService());
      this.appWin.displayContent(var1, ApplicationWindow.SOUTH, null, null);
   }
}
