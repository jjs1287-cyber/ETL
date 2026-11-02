package org.etlforge.integration.data.entity.operation.pipeline.transformation;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.TransformationOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;

public final class TransformationPipelineFactory implements OperationPipelineFactory {
   @Override
   public OperationPipeline createPipeline(Operation var1, IntegrationEntityLookup var2) {
      SourceActivity var3 = SourceActivity.newInstance(var2.getEntity(var1.getSourceId(), Source.class), "Source");
      TransformationActivity var4 = TransformationActivity.newInstance(var2.getEntity(var1.getTransformationId(), Transformation.class), "Transformation");
      TargetActivity var5 = TargetActivity.newInstance(var2.getEntity(var1.getTargetId(), Target.class), "Target");
      ArrayList var6 = Lists.newArrayList();
      var6.add(var3);
      var6.add(var4);
      var6.add(var5);
      return new OperationPipeline(TransformationOperationType.TYPE, var6);
   }
}
