package org.etlforge.integration.client.ui.deploy.history;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

final class ViewDeployResultDetailsAction extends AbstractSingleObjectSelectionAction<DeployResult> {
   private IntegrationProject project;

   public ViewDeployResultDetailsAction(IntegrationProject var1) {
      super(DeployResult.class);
      ActionDecorators.VIEW_DETAILS.decorate(this);
      this.putValue("ShortDescription", Strings.get("Deploy.History.ViewDetails.ToolTip"));
      this.setProject(var1);
   }

   public void setProject(IntegrationProject var1) {
      this.project = var1;
      this.setEnabled(var1 != null);
   }

   protected boolean isEnabledFor(DeployResult var1) {
      return this.project != null;
   }

   protected void run(final DeployResult var1, ActionEvent var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewDeployResultDetailsAction.this.displayDetails(var1);
         }
      });
   }

   private void displayDetails(DeployResult var1) {
      DeployResultDetailsViewer var2 = DeployResultDetailsViewer.create(var1, this.project);
      DeployResultDetailsDialog.display(var2);
   }
}
