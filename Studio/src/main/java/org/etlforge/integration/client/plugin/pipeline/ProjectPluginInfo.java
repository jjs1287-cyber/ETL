package org.etlforge.integration.client.plugin.pipeline;

import com.google.common.base.Function;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public interface ProjectPluginInfo {
   IntegrationProject getProject();

   MutableAppliedPipelinePluginStore getAppliedPlugins();

   PipelinePluginManifestCache getManifestCache();

   void applyNewPluginSettings(IntegrationEntity var1, Iterable<AppliedPipelinePlugin> var2, Function<AppliedPipelinePlugin, PipelinePluginManifestCache> var3) throws Exception;
}
