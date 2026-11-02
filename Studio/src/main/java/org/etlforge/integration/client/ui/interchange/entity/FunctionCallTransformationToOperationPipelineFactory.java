package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.FunctionCallOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;

public class FunctionCallTransformationToOperationPipelineFactory implements TransformationToOperationPipelineFactory {
   private final FunctionCallOperationType operationType;

   public FunctionCallTransformationToOperationPipelineFactory(FunctionCallOperationType var1) {
      this.operationType = Preconditions.checkNotNull(var1, "operationType");
   }

   @Override
   public boolean isSupported(Transformation var1) {
      return this.operationType.isRequest(var1) || this.operationType.isResponse(var1);
   }

   @Override
   public OperationPipeline createPipeline(Transformation var1, IntegrationEntityLookup var2) {
      OperationPipeline var3 = this.operationType.createNewTemplate();
      FunctionCallPipelineBrowser var4 = this.operationType.createPipelineBrowser(var3);
      TransformationActivity var5 = this.operationType.isRequest(var1) ? var4.getRequestActivity() : var4.getResponseActivity();
      var5.setContent(var1);
      PipelineAutoCompleter var6 = this.operationType.getPipelineAutoCompleter(var2);
      var6.complete(var3, var5);
      return var3;
   }
}
