package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.operation.ArchiveOperationType;

public class ArchiveTemplate extends OperationTemplate {
   public ArchiveTemplate() {
      super(ArchiveOperationType.TYPE);
   }

   @Override
   protected PipelineActivity[] createChain() {
      return new PipelineActivity[]{new SourceActivity("Source"), new TargetActivity("Target")};
   }
}
