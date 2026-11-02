package org.etlforge.integration.client.deploy.serverapi;

import org.etlforge.integration.data.entity.IntegrationEntityId;

public interface EntityWarning {
   IntegrationEntityId getIntegrationEntityId();

   String getWarning();
}
