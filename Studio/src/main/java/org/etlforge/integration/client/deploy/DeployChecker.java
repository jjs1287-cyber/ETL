package org.etlforge.integration.client.deploy;

import org.etlforge.integration.data.entity.IntegrationEntity;

public interface DeployChecker {
   boolean isUpToDate(Iterable<IntegrationEntity> var1);

   void promptDeploy(DeployPrompt var1, DeployPromptCallback var2);
}
