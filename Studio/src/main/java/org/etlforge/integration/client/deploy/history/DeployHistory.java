package org.etlforge.integration.client.deploy.history;

import java.util.List;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.data.project.IntegrationProject;

public interface DeployHistory {
   IntegrationProject getProject();

   void addResult(DeployResult var1);

   void removeResult(DeployResult var1);

   List<DeployResult> getAllResults();

   void removeAll();

   void addListener(DeployHistoryListener var1);

   void removeListener(DeployHistoryListener var1);
}
