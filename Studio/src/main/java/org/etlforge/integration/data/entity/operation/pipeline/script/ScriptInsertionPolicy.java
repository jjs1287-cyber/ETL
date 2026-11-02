package org.etlforge.integration.data.entity.operation.pipeline.script;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityConnection;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;

public final class ScriptInsertionPolicy implements ActivityInsertionPolicy {
   @Override
   public ImmutableSet<PipelineActivityType> getAllowedActivities(ActivityConnection var1) {
      return ImmutableSet.of(PipelineActivityType.SCRIPT);
   }
}
