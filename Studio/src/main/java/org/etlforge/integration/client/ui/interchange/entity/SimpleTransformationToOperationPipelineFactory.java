package org.etlforge.integration.client.ui.interchange.entity;

import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.TransformationOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationPipelineBrowser;
import org.etlforge.integration.data.structure.WebServiceRequestStructure;
import org.etlforge.integration.data.structure.WebServiceResponseStructure;

public final class SimpleTransformationToOperationPipelineFactory implements TransformationToOperationPipelineFactory {
   @Override
   public boolean isSupported(Transformation var1) {
      return !this.isHostedWebServiceCall(var1);
   }

   private boolean isHostedWebServiceCall(Transformation var1) {
      return var1.getInputStructure() instanceof WebServiceRequestStructure || var1.getOutputStructure() instanceof WebServiceResponseStructure;
   }

   @Override
   public OperationPipeline createPipeline(Transformation var1, IntegrationEntityLookup var2) {
      OperationPipeline var3 = TransformationOperationType.TYPE.createNewTemplate();
      TransformationPipelineBrowser var4 = new TransformationPipelineBrowser(var3);
      var4.setTransformation(var1);
      TransformationAutoCompleter var5 = new TransformationAutoCompleter(var2);
      var5.complete(var3, var4.getTransformationActivity());
      return var3;
   }
}
