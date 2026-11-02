package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.operation.ScriptOperationType;

public final class ScriptTemplate extends OperationTemplate {
   public static final String SCRIPT = "Script";

   public ScriptTemplate() {
      super(ScriptOperationType.TYPE);
   }

   @Override
   protected PipelineActivity[] createChain() {
      return new PipelineActivity[]{PipelineActivityType.SCRIPT.createEmptyActivity()};
   }
}
