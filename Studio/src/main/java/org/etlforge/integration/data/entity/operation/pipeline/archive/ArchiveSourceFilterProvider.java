package org.etlforge.integration.data.entity.operation.pipeline.archive;

import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.operation.ArchiveOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;

public final class ArchiveSourceFilterProvider implements EntityFilterProvider<Source> {
   @Override
   public EntityFilter getFilter(PipelineActivity<Source> var1) {
      return ArchiveOperationType.TYPE.getSourceFilter();
   }
}
