package org.etlforge.integration.data.entity.operation.pipeline.hostedws;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.HostedWebServiceCallChain;
import org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.operation.pipeline.WebServiceCallActivity;

public final class HostedWebServicePipelineFactory implements OperationPipelineFactory {
   @Override
   public OperationPipeline createPipeline(Operation var1, IntegrationEntityLookup var2) {
      HostedWebServiceCallChain var3 = new HostedWebServiceCallChain(var1, var2);
      TransformationActivity var4 = TransformationActivity.newInstance(var3.getFirstTransformation(), "Transformation");
      WebServiceCallActivity var5 = WebServiceCallActivity.newInstance(var3.getWebServiceCall(), "Web Service Call");
      TransformationActivity var6 = TransformationActivity.newInstance(var3.getSecondTransformation(), "Transformation");
      ArrayList var7 = Lists.newArrayList();
      var7.add(var5);
      var7.add(var4);
      var7.add(var6);
      return new OperationPipeline(HostedWebServiceOperationType.TYPE, var7);
   }
}
