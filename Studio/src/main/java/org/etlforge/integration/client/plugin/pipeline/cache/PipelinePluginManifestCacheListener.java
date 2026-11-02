package org.etlforge.integration.client.plugin.pipeline.cache;

import java.util.List;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;

public interface PipelinePluginManifestCacheListener {
   void cacheWasCleared(PipelinePluginManifestCache var1);

   void manifestsWereCached(PipelinePluginManifestCache var1, List<PipelinePluginManifest> var2);
}
