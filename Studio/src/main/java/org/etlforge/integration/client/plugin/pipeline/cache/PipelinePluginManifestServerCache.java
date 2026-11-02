package org.etlforge.integration.client.plugin.pipeline.cache;

import java.util.List;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.cache.ServerCache;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;

public interface PipelinePluginManifestServerCache extends ServerCache<PipelinePluginManifestCache> {
   void addManifests(ServerInfo var1, List<PipelinePluginManifest> var2) throws DataCacheException;

   PipelinePluginManifestCache getCache(ServerGuid var1);
}
