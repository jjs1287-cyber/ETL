package org.etlforge.integration.data.entity.operation.pipeline.script;

import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineFactory;

public final class ScriptPipelineFactory implements OperationPipelineFactory {
   @Override
   public OperationPipeline createPipeline(Operation var1, IntegrationEntityLookup var2) {
      assert false : "We should not need a OperationPipelineFactory for the Script operation type";
      return null;
   }
}
