package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.testhistory.TestTransformationHistoryViewFactory;

public final class ViewTestTransformationHistoryAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.TestTransformationHistory";
   private final ApplicationWindow appWin;

   public ViewTestTransformationHistoryAction(ApplicationWindow var1, CurrentProjectService var2) {
      super(var2, "action.ic.TestTransformationHistory", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewTestTransformationHistoryAction.this.openHistory();
         }
      });
   }

   private void openHistory() {
      TestTransformationHistoryViewFactory var1 = new TestTransformationHistoryViewFactory(this.getCurrentProjectService());
      this.appWin.displayContent(var1, ApplicationWindow.SOUTH, null, null);
   }
}
