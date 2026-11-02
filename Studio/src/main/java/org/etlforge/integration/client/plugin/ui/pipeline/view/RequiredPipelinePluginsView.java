package org.etlforge.integration.client.plugin.ui.pipeline.view;

import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;

public interface RequiredPipelinePluginsView extends WindowSectionContent {
   String CONTENT_ID = RequiredPipelinePluginsView.class.getName();

   void refresh();

   void setProjectPlugins(ProjectPluginInfo var1);
}
