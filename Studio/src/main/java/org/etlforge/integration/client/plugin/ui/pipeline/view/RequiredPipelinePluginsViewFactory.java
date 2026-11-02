package org.etlforge.integration.client.plugin.ui.pipeline.view;

import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;
import org.etlforge.integration.client.plugin.serverapi.PipelinePluginInfoProvider;

public final class RequiredPipelinePluginsViewFactory implements WindowSectionContentProvider {
   private final PipelinePluginInfoProvider infoProvider;
   private ProjectPluginInfo projectPlugins;

   public RequiredPipelinePluginsViewFactory(PipelinePluginInfoProvider var1) {
      this.infoProvider = var1;
   }

   public RequiredPipelinePluginsView getContent(WindowSection var1) {
      RequiredPipelinePluginsViewImpl var2 = new RequiredPipelinePluginsViewImpl(var1, this.infoProvider);
      var2.setProjectPlugins(this.projectPlugins);
      return var2;
   }

   @Override
   public String getContentId() {
      return RequiredPipelinePluginsView.CONTENT_ID;
   }

   public void setProjectPlugins(ProjectPluginInfo var1) {
      this.projectPlugins = var1;
   }
}
