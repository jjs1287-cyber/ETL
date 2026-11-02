package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.collect.ImmutableSet;

public interface ActivityInsertionPolicy {
   boolean PLUGINS_SUPPORTED = false;

   ImmutableSet<PipelineActivityType> getAllowedActivities(ActivityConnection var1);
}
