package org.etlforge.integration.client.deploy;

import org.etlforge.integration.data.entity.IntegrationEntity;

public class RelaxedDeployChecker implements DeployChecker {
   @Override
   public boolean isUpToDate(Iterable<IntegrationEntity> var1) {
      return true;
   }

   @Override
   public void promptDeploy(DeployPrompt var1, DeployPromptCallback var2) {
      throw new UnsupportedOperationException();
   }
}
