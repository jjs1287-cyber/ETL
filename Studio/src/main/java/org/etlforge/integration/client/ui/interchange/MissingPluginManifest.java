package org.etlforge.integration.client.ui.interchange;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestServerCache;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;

final class MissingPluginManifest {
   private final String pluginName;
   private final PipelinePluginManifestCache projectCache;
   private Logger logger = Logger.getAnonymousLogger();

   public MissingPluginManifest(String var1, PipelinePluginManifestCache var2) {
      this.pluginName = var1;
      this.projectCache = var2;
   }

   public void setLogger(Logger var1) {
      this.logger = var1;
   }

   public void restore(PipelinePluginManifestServerCache var1, List<KnownServer> var2) {
      for (KnownServer var4 : var2) {
         PipelinePluginManifestCache var5 = var1.getCache(var4.getServerInfo().getGuid());

         try {
            PipelinePluginManifest var6 = var5.getCachedManifest(this.pluginName);
            if (var6 != null) {
               this.projectCache.add(Collections.singletonList(var6));
               this.logger.info("The manifest for the plugin " + this.pluginName + " was found in the cache for server " + var4.getServerInfo().getName());
               return;
            }
         } catch (DataCacheException var7) {
            var7.printStackTrace();
         }
      }

      this.logger.info("Could not find a manifest for the plugin " + this.pluginName);
   }

   @Override
   public String toString() {
      return this.pluginName;
   }
}
