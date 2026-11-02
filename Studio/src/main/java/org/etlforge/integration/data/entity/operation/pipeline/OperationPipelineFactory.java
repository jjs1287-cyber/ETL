package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;

public interface OperationPipelineFactory {
   OperationPipeline createPipeline(Operation var1, IntegrationEntityLookup var2);
}
