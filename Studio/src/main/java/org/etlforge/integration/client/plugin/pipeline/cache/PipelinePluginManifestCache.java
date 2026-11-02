package org.etlforge.integration.client.plugin.pipeline.cache;

import java.util.List;
import org.etlforge.application.cache.DataCache;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;

public interface PipelinePluginManifestCache extends DataCache {
   String CACHE_ID = "PipelinePluginManifestCache";

   List<PipelinePluginManifest> getCachedManifests() throws DataCacheException;

   List<PipelinePosition> getPipelinePositions(EntityType var1) throws DataCacheException;

   PipelinePluginManifest getCachedManifest(String var1) throws DataCacheException;

   void removeAll();

   void add(List<PipelinePluginManifest> var1) throws DataCacheException;

   void addCacheListener(PipelinePluginManifestCacheListener var1);

   void removeCacheListener(PipelinePluginManifestCacheListener var1);
}
