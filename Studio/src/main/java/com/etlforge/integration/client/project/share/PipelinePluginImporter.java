package com.etlforge.integration.client.project.share;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;

final class PipelinePluginImporter {
   private final ProjectPluginInfo from;
   private final ProjectPluginInfo to;

   public PipelinePluginImporter(ManagedProject var1, ManagedProject var2) {
      this.from = var1.getPipelinePlugins();
      this.to = var2.getPipelinePlugins();
   }

   public void transfer(IntegrationEntity var1) {
      ArrayList var2 = Lists.newArrayList();

      for (AppliedPipelinePlugin var6 : this.from.getAppliedPlugins().getAllAssociations(var1)) {
         PipelinePluginManifest var7 = this.findExistingManifest(this.to, var6.getPluginId());
         if (var7 == null) {
            var7 = this.transferManifest(var6.getPluginId());
         }

         if (var7 != null) {
            var2.add(new AppliedPipelinePlugin(var1.getID(), var7, var6.getPipelinePosition()));
         }
      }

      this.to.getAppliedPlugins().setAssociations(var1, var2);
   }

   private PipelinePluginManifest findExistingManifest(ProjectPluginInfo var1, PluginIdentifier var2) {
      try {
         for (PipelinePluginManifest var4 : var1.getManifestCache().getCachedManifests()) {
            if (var2.equals(new PluginIdentifier(var4.getName(), var4.getVersion()))) {
               return var4;
            }
         }
      } catch (DataCacheException var5) {
         var5.printStackTrace();
      }

      return null;
   }

   private PipelinePluginManifest transferManifest(PluginIdentifier var1) {
      PipelinePluginManifest var2 = this.findExistingManifest(this.from, var1);
      if (var2 != null) {
         try {
            PipelinePluginManifestCache var3 = this.to.getManifestCache();
            var3.add(Collections.singletonList(var2));
         } catch (DataCacheException var4) {
            var4.printStackTrace();
         }
      }

      return var2;
   }
}
