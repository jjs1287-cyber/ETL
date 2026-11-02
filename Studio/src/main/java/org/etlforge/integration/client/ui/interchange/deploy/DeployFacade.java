package org.etlforge.integration.client.ui.interchange.deploy;

import org.etlforge.integration.client.deploy.DeployChecker;
import org.etlforge.integration.client.deploy.DeployGate;
import org.etlforge.integration.client.deploy.serverapi.DeployCallback;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface DeployFacade {
   DeployGate newDeployGate();

   void deploy(Iterable<? extends IntegrationEntity> var1, DeployCallback var2);

   DeployResult deploy(IntegrationEntity var1);

   DeployResult deploy(Iterable<? extends IntegrationEntity> var1);

   void promptDeploy(DeployChecker var1, Runnable var2);
}
