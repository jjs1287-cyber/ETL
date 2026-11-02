package org.etlforge.integration.data.entity.operation.pipeline.transformation;

import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;

public final class TransformationAutoCompleter extends PipelineAutoCompleter {
   public TransformationAutoCompleter(IntegrationEntityLookup var1) {
      super(var1);
   }

   @Override
   public void complete(OperationPipeline var1, PipelineActivity var2) {
      TransformationPipelineBrowser var3 = new TransformationPipelineBrowser(var1);
      Object var4 = var2.getContent();
      if (var4 instanceof Transformation) {
         Transformation var5 = (Transformation)var4;
         this.checkSource(var3, var5, "Source");
         this.checkTarget(var3, var5);
      }
   }

   private void checkTarget(OperationPipelineBrowser var1, Transformation var2) {
      Target var3 = this.lookupTarget(var2.getTargetId());
      if (var3 != null) {
         TargetActivity var4 = var1.getTargetActivity();
         if (var4 == null) {
            this.insertTargetActivityIfPossible(var1);
         }

         var1.setTarget(var3);
      }
   }

   private void insertTargetActivityIfPossible(OperationPipelineBrowser var1) {
      if (var1.getPipeline().getLastActivity().getType() == PipelineActivityType.TRANSFORMATION) {
         var1.getPipeline().insertLast(new TargetActivity("Target"));
      }
   }
}
