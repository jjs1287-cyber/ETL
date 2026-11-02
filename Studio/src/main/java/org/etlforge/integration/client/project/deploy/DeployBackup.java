package org.etlforge.integration.client.project.deploy;

import org.etlforge.integration.client.deploy.serverapi.DeployCallback;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.project.ProjectManager;

public final class DeployBackup extends DeployCallback.Adapter {
   private final ProjectManager projectManager;

   public DeployBackup(ProjectManager var1) {
      this.projectManager = var1;
   }

   @Override
   public void deployComplete(DeployData var1, DeployResult var2) {
      ProjectBackupHandler var3 = new ProjectBackupHandler(this.projectManager, var1.getProject());
      var3.setTag(var1.getTag());
      var3.handleDeployResult(var2);
   }
}
