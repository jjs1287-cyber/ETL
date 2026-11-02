package org.etlforge.integration.client.project.deploy;

import org.etlforge.integration.client.deploy.serverapi.DeployData;

public interface PreDeployJobFactory {
   PreDeployJob getJob(DeployData var1);
}
