package org.etlforge.integration.client.ui.interchange.cli;

import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.deploy.DeployLauncher;
import org.etlforge.integration.client.ui.deploy.DeployWorker;
import org.etlforge.integration.client.ui.deploy.option.DeployDirtyItemsOption;
import org.etlforge.integration.client.ui.deploy.option.DeployOption;
import org.etlforge.integration.client.ui.interchange.core.action.SaveAllGate;
import org.etlforge.integration.data.project.IntegrationProject;

public final class DeployDirtyItemsExecutor extends AbstractDeployProjectExecutor {
   public DeployDirtyItemsExecutor(CurrentProjectService var1, DeployWorker var2, SaveAllGate var3) {
      super(var1, var2, var3);
   }

   @Override
   protected DeployOption createOption(IntegrationProject var1, DeployLauncher var2) {
      return new DeployDirtyItemsOption(var1, var2);
   }
}
