package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.operation.TransformationOperationType;

public class TransformationTemplate extends OperationTemplate {
   public static final String TRANSFORMATION = "Transformation";

   public TransformationTemplate() {
      super(TransformationOperationType.TYPE);
   }

   @Override
   protected PipelineActivity[] createChain() {
      return new PipelineActivity[]{new SourceActivity("Source"), new TransformationActivity("Transformation"), new TargetActivity("Target")};
   }
}
