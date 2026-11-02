package org.etlforge.integration.client.ui.deploy;

import java.util.List;
import org.etlforge.integration.client.deploy.DeployPromptCallback;
import org.etlforge.integration.data.entity.IntegrationEntity;

public abstract class AbstractDeployPromptCallback implements DeployPromptCallback {
   private final SelectedObjectsDeployer deployer;

   protected AbstractDeployPromptCallback(SelectedObjectsDeployer var1) {
      this.deployer = var1;
   }

   @Override
   public final void deployGreenLighted(List<? extends IntegrationEntity> var1) {
      this.deployer.deploy(var1, this);
   }

   @Override
   public void cancelled() {
   }

   @Override
   public void caught(Throwable var1) {
   }
}
