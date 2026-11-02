package org.etlforge.integration.client.plugin.ui.pipeline.selector;

import java.util.List;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;

public interface PluginPositionSelectorController {
   void refreshListOfPlugins();

   void selectedPositions(List<PipelinePosition> var1);
}
