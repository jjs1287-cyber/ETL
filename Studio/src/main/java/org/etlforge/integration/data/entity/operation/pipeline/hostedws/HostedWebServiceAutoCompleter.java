package org.etlforge.integration.data.entity.operation.pipeline.hostedws;

import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.WebServiceRequestStructure;

public final class HostedWebServiceAutoCompleter extends PipelineAutoCompleter {
   public HostedWebServiceAutoCompleter(IntegrationEntityLookup var1) {
      super(var1);
   }

   @Override
   public void complete(OperationPipeline var1, PipelineActivity var2) {
      HostedWebServicePipelineBrowser var3 = new HostedWebServicePipelineBrowser(var1);
      if (var2 == var3.getFirstTransformationActivity()) {
         Transformation var4 = (Transformation)var2.getContent();
         if (var4 != null) {
            DataStructure var5 = var4.getInputStructure();
            if (var5 instanceof WebServiceRequestStructure) {
               WebServiceCall var6 = this.lookupWsCall(((WebServiceRequestStructure)var5).getFunctionCallId());
               if (var3.getWebServiceCall() == null) {
                  var3.setWebServiceCall(var6);
               }

               this.removeSecondTransformationIfNotNeeded(var1, var3, var4, var6);
            }
         }
      }
   }

   private void removeSecondTransformationIfNotNeeded(OperationPipeline var1, HostedWebServicePipelineBrowser var2, Transformation var3, WebServiceCall var4) {
      if (this.isTransformationSelfContained(var3, var4)) {
         this.removeSecondTransformation(var1, var2);
      }
   }

   private boolean isTransformationSelfContained(Transformation var1, WebServiceCall var2) {
      return var2.getOutputStructure().equals(var1.getOutputStructure());
   }

   private void removeSecondTransformation(OperationPipeline var1, HostedWebServicePipelineBrowser var2) {
      TransformationActivity var3 = var2.getSecondTransformationActivity();
      if (var3 != null) {
         var1.removeActivity(var3);
      }
   }
}
