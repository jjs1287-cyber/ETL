package org.etlforge.integration.data.entity.operation.pipeline.archive;

import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.ArchiveOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;

public final class ArchiveTargetFilterProvider implements EntityFilterProvider<Target> {
   @Override
   public EntityFilter getFilter(PipelineActivity<Target> var1) {
      return ArchiveOperationType.TYPE.getTargetFilter();
   }
}
