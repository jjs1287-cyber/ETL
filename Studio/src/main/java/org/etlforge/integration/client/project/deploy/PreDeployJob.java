package org.etlforge.integration.client.project.deploy;

import org.etlforge.integration.client.deploy.serverapi.DeployData;

public interface PreDeployJob {
   PreDeployStatus run(DeployData var1);
}
