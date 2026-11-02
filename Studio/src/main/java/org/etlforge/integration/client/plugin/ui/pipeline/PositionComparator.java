package org.etlforge.integration.client.plugin.ui.pipeline;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;

final class PositionComparator implements Comparator<PipelinePosition> {
   public int compare(PipelinePosition var1, PipelinePosition var2) {
      return ComparisonChain.start()
         .compare(var1.getEntityType(), var2.getEntityType())
         .compare(var1.getRelativePosition(), var2.getRelativePosition())
         .compare(var1.getExecutionOrder(), var2.getExecutionOrder())
         .result();
   }
}
