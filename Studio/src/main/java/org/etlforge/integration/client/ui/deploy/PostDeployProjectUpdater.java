package org.etlforge.integration.client.ui.deploy;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.deploy.DeployLog;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;

final class PostDeployProjectUpdater {
   private final ManagedProject managedProject;
   private final DeployData deployData;

   public PostDeployProjectUpdater(ManagedProject var1, DeployData var2) {
      this.managedProject = var1;
      this.deployData = var2;
   }

   public void handle(DeployResult var1) {
      if (var1.getCause() == null) {
         this.updateProject();
      }
   }

   private void updateProject() {
      try {
         DeployLog.LOG.fine("Acquiring the IntegrationProjectLock...");
         IntegrationProjectLock.acquire();
         if (this.deployData.isPartial()) {
            this.onPartialProjectDeployed(this.deployData.getProject());
         } else {
            this.onCompleteProjectDeployed();
         }
      } catch (Exception var5) {
         ErrorLog.attention(DefaultDeployLauncher.class, Strings.get("Launcher.PostDeployError"), var5);
      } finally {
         IntegrationProjectLock.release();
         DeployLog.LOG.fine("Releasing the IntegrationProjectLock");
      }
   }

   private void onCompleteProjectDeployed() throws InterchangeSavingException {
      DeployLog.LOG.fine("Handing over to the ProjectManager for processing complete project deploy");
      ManagedProject var1 = this.deployData.getProject().getExtensionObject(ManagedProject.class);
      var1.projectWasDeployed();
   }

   private void onPartialProjectDeployed(IntegrationProject var1) throws InterchangeSavingException {
      DeployLog.LOG.fine("Handing over to the ProjectManager for processing partial project deploy");
      this.managedProject.partialProjectWasDeployed(var1);
   }
}
