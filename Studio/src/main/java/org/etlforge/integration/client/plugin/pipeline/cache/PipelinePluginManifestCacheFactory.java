package org.etlforge.integration.client.plugin.pipeline.cache;

import com.google.common.base.Preconditions;
import org.etlforge.io.Folder;

public abstract class PipelinePluginManifestCacheFactory {
   public abstract PipelinePluginManifestCache createCache();

   public static PipelinePluginManifestCacheFactory newFileBasedCacheFactory(final Folder var0) {
      Preconditions.checkNotNull(var0, "dir");
      return new PipelinePluginManifestCacheFactory() {
         @Override
         public PipelinePluginManifestCache createCache() {
            return new FileBasedPipelinePluginManifestCache(var0);
         }
      };
   }
}
