package org.etlforge.integration.data.entity.operation.pipeline;

import java.util.EventListener;

public interface PipelineActivityListener extends EventListener {
   void contentChanged(PipelineActivityEvent var1);

   void activityObjectChanged(PipelineActivityEvent var1);
}
