package org.etlforge.integration.data.entity.operation.pipeline;

import java.util.Arrays;
import java.util.List;
import org.etlforge.integration.data.entity.operation.OperationType;

public abstract class OperationTemplate {
   public static final String SOURCE = "Source";
   public static final String TARGET = "Target";
   private final OperationType type;

   protected OperationTemplate(OperationType var1) {
      this.type = var1;
   }

   public final OperationPipeline getPipeline() {
      List var1 = Arrays.asList(this.createChain());
      return new OperationPipeline(this.type, var1);
   }

   protected abstract PipelineActivity[] createChain();
}
