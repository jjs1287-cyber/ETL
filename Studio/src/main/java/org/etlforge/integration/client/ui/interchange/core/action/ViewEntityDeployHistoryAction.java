package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.deploy.history.DeployHistory;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.entity.DeployHistoryViewFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ViewEntityDeployHistoryAction extends AbstractSingleActiveObjectAction<IntegrationEntity> {
   public static final String ID = "action.ic.ItemDeployHistory";
   private final ApplicationWindow appWin;
   private final CurrentProjectService currentProjectService;

   public ViewEntityDeployHistoryAction(ApplicationWindow var1, CurrentProjectService var2) {
      super(var1.getActiveObjectService(), "action.ic.ItemDeployHistory", InterchangeActionDictionary.DICTIONARY, IntegrationEntity.class);
      this.appWin = var1;
      this.currentProjectService = var2;
   }

   protected void runOnObject(final IntegrationEntity var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewEntityDeployHistoryAction.this.displayHistory(var1);
         }
      });
   }

   private void displayHistory(IntegrationEntity var1) {
      ManagedProject var2 = ProjectUtils.getManagedProject(var1);
      if (var2 == null) {
         ErrorLog.log(this.getClass(), null, new Exception("No ManagedProject found for the requested project item."));
      } else {
         DeployHistory var3 = var2.getDeployHistory();
         DeployHistoryViewFactory var4 = new DeployHistoryViewFactory(var3, this.currentProjectService);
         var4.setEntity(var1);
         var4.openInWindow(this.appWin);
      }
   }
}
