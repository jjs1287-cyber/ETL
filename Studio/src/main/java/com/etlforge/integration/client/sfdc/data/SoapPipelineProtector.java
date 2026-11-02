package com.etlforge.integration.client.sfdc.data;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.operation.pipeline.WebServiceCallActivity;
import org.etlforge.integration.data.entity.operation.pipeline.ws.WebServicePipelineBrowser;

public final class SoapPipelineProtector extends OperationPipelineProtector {
   private final ImmutableSet<SoapPipelineProtector.Transformation> transformationsToProtect;

   public SoapPipelineProtector(EnumSet<SoapPipelineProtector.Transformation> var1) {
      this.transformationsToProtect = Sets.immutableEnumSet(var1);
   }

   @Override
   public void protectPipeline(OperationPipeline var1) {
      WebServicePipelineBrowser var2 = new WebServicePipelineBrowser(var1);
      WebServiceCallActivity var3 = var2.getFunctionActivity();
      var3.setReadOnly(true);

      for (SoapPipelineProtector.Transformation var5 : this.transformationsToProtect) {
         TransformationActivity var6 = var5 == SoapPipelineProtector.Transformation.REQUEST ? var2.getRequestActivity() : var2.getResponseActivity();
         var6.setReadOnly(true);
      }
   }

   public enum Transformation {
      REQUEST,
      RESPONSE;
   }
}
