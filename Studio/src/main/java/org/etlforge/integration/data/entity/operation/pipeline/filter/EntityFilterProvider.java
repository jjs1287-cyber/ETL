package org.etlforge.integration.data.entity.operation.pipeline.filter;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.functors.EntityFilter;

public interface EntityFilterProvider<E extends IntegrationEntity> {
   EntityFilter getFilter(PipelineActivity<E> var1);
}
