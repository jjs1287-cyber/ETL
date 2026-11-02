package org.etlforge.integration.client.deploy;

import org.etlforge.integration.data.entity.IntegrationEntity;

public interface DeployGate {
   void setItemsToCheck(IntegrationEntity... var1);

   void setItemsToCheck(Iterable<? extends IntegrationEntity> var1);

   void setJob(Runnable var1);

   void setDeployChecker(DeployChecker var1);

   void start();
}
