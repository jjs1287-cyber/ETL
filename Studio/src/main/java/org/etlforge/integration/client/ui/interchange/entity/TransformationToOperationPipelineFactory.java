package org.etlforge.integration.client.ui.interchange.entity;

import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public interface TransformationToOperationPipelineFactory {
   boolean isSupported(Transformation var1);

   OperationPipeline createPipeline(Transformation var1, IntegrationEntityLookup var2);
}
