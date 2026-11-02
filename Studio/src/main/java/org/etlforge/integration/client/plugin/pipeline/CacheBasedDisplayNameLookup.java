package org.etlforge.integration.client.plugin.pipeline;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.client.plugin.PluginDisplayNameLookup;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;

public final class CacheBasedDisplayNameLookup implements PluginDisplayNameLookup {
   private final Map<PluginIdentifier, String> map = Maps.newHashMap();

   public CacheBasedDisplayNameLookup(PipelinePluginManifestCache var1) {
      this.initializeMap(var1);
   }

   private void initializeMap(PipelinePluginManifestCache var1) {
      try {
         for (PipelinePluginManifest var3 : var1.getCachedManifests()) {
            PluginIdentifier var4 = new PluginIdentifier(var3.getName(), var3.getVersion());
            this.map.put(var4, var3.getDisplayName());
         }
      } catch (DataCacheException var5) {
         var5.printStackTrace();
      }
   }

   @Override
   public String getDisplayName(PluginIdentifier var1) {
      String var2 = this.map.get(var1);
      if (var2 == null) {
         var2 = var1.getName();
      }

      return var2;
   }
}
