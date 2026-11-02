package org.etlforge.integration.client.deploy.history;

import org.etlforge.integration.client.deploy.serverapi.DeployResult;

public interface DeployHistoryListener {
   void deployResultAdded(DeployResult var1);

   void deployResultRemoved(DeployResult var1);

   void historyCleared();
}
