package org.etlforge.integration.data.entity.operation.pipeline.archive;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.ArchiveOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineFactory;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;

public final class ArchivePipelineFactory implements OperationPipelineFactory {
   @Override
   public OperationPipeline createPipeline(Operation var1, IntegrationEntityLookup var2) {
      SourceActivity var3 = SourceActivity.newInstance(var2.getEntity(var1.getSourceId(), Source.class), "Source");
      TargetActivity var4 = TargetActivity.newInstance(var2.getEntity(var1.getTargetId(), Target.class), "Target");
      ArrayList var5 = Lists.newArrayList();
      var5.add(var3);
      var5.add(var4);
      return new OperationPipeline(ArchiveOperationType.TYPE, var5);
   }
}
