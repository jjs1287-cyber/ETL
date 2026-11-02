package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.deploy.history.DeployHistory;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.interchange.entity.DeployHistoryViewFactory;

public final class ViewProjectDeployHistoryAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.DeployHistory";
   private final ApplicationWindow appWin;

   public ViewProjectDeployHistoryAction(ApplicationWindow var1, CurrentProjectService var2) {
      super(var2, "action.ic.DeployHistory", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewProjectDeployHistoryAction.this.openDeployHistory();
         }
      });
   }

   private void openDeployHistory() {
      ManagedProject var1 = this.getManagedProject();
      DeployHistory var2 = var1.getDeployHistory();
      DeployHistoryViewFactory var3 = new DeployHistoryViewFactory(var2, this.getCurrentProjectService());
      var3.openInWindow(this.appWin);
   }
}
