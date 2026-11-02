package org.etlforge.integration.client.plugin.pipeline;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestServerCache;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;

public class GlobalPipelinePlugins implements AppliedPipelinePluginStore {
   private final List<PipelinePluginManifest> manifests;

   public GlobalPipelinePlugins(ServerGuid var1) {
      Preconditions.checkNotNull(var1, "serverId");
      this.manifests = this.getCachedManifests(var1);
   }

   @Override
   public AppliedPipelinePlugin[] getAllAssociations(IntegrationEntity var1) {
      ArrayList var2 = new ArrayList();

      for (PipelinePluginManifest var4 : this.manifests) {
         for (PipelinePosition var8 : var4.getPipelinePositions()) {
            if (this.isGlobalForEntity(var8, var1)) {
               AppliedPipelinePlugin var9 = new AppliedPipelinePlugin(var1.getID(), var4, var8);
               var2.add(var9);
            }
         }
      }

      return (AppliedPipelinePlugin[]) var2.toArray(new AppliedPipelinePlugin[var2.size()]);
   }

   @Override
   public void addListener(AppliedPipelinePluginStoreListener var1) {
   }

   @Override
   public void removeListener(AppliedPipelinePluginStoreListener var1) {
   }

   private List<PipelinePluginManifest> getCachedManifests(ServerGuid var1) {
      try {
         PipelinePluginManifestCache var2 = this.getCache(var1);
         if (var2 != null) {
            return var2.getCachedManifests();
         }
      } catch (DataCacheException var3) {
         var3.printStackTrace();
      }

      return Collections.emptyList();
   }

   private PipelinePluginManifestCache getCache(ServerGuid var1) {
      DataCacheService var2 = Application.getCacheService();
      PipelinePluginManifestServerCache var3 = var2.getCache(PipelinePluginManifestServerCache.class);
      return var3 != null ? var3.getCache(var1) : null;
   }

   private boolean isGlobalForEntity(PipelinePosition var1, IntegrationEntity var2) {
      return !var1.isOptional() && var1.getEntityType() == var2.getEntityType();
   }
}
