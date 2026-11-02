package org.etlforge.integration.client.ui.deploy;

import org.etlforge.integration.client.deploy.DeployException;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface PreDeployProcessor {
   void process(IntegrationEntity var1) throws DeployException;
}
