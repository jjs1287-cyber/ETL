package org.etlforge.integration.data.entity.operation.pipeline.ws;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.WebServiceCallChain;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.operation.pipeline.WebServiceCallActivity;

public final class WebServicePipelineFactory implements OperationPipelineFactory {
   @Override
   public OperationPipeline createPipeline(Operation var1, IntegrationEntityLookup var2) {
      SourceActivity var3 = SourceActivity.newInstance(var2.getEntity(var1.getSourceId(), Source.class), "Source");
      TargetActivity var4 = TargetActivity.newInstance(var2.getEntity(var1.getTargetId(), Target.class), "Target");
      WebServiceCallChain var5 = new WebServiceCallChain(var1, var2);
      TransformationActivity var6 = TransformationActivity.newInstance(var5.getRequestTransformation(), "Request");
      WebServiceCallActivity var7 = WebServiceCallActivity.newInstance(var5.getWebServiceCall(), "Web Service Call");
      TransformationActivity var8 = TransformationActivity.newInstance(var5.getResponseTransformation(), "Response");
      ArrayList var9 = Lists.newArrayList();
      var9.add(var3);
      var9.add(var6);
      var9.add(var7);
      var9.add(var8);
      var9.add(var4);
      return new OperationPipeline(WebServiceOperationType.TYPE, var9);
   }
}
