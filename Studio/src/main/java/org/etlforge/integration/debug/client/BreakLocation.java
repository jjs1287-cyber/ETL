package org.etlforge.integration.debug.client;

import org.etlforge.integration.data.entity.IntegrationEntityId;

public interface BreakLocation {
   IntegrationEntityId getIntegrationEntityId();

   int getLineNumber();

   String getTargetNodePath();

   GeneratorInstance getGeneratorInstance();
}
