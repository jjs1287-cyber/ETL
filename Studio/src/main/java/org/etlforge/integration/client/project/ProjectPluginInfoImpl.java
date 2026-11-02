package org.etlforge.integration.client.project;

import com.google.common.base.Function;
import java.util.Collections;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.pipeline.MutableAppliedPipelinePluginStore;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCacheFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.io.Folder;

final class ProjectPluginInfoImpl implements ProjectPluginInfo {
   private final ManagedProject managedProject;
   private final ProjectPipelinePluginStore appliedPlugins;
   private final PipelinePluginManifestCache manifestCache;

   public ProjectPluginInfoImpl(ManagedProject var1) {
      this.managedProject = var1;
      this.manifestCache = this.createManifestCache(var1);
      this.appliedPlugins = new ProjectPipelinePluginStore();
      this.appliedPlugins.setManifests(this.manifestCache);
   }

   public void addAllFrom(ProjectPipelinePluginStore var1) {
      this.appliedPlugins.addAllFrom(var1);
   }

   @Override
   public IntegrationProject getProject() {
      return this.managedProject.getProject();
   }

   @Override
   public MutableAppliedPipelinePluginStore getAppliedPlugins() {
      return this.appliedPlugins;
   }

   @Override
   public PipelinePluginManifestCache getManifestCache() {
      return this.manifestCache;
   }

   public void removeEntities(Iterable<IntegrationEntity> var1) {
      boolean var2 = false;

      for (IntegrationEntity var4 : var1) {
         if (this.getAppliedPlugins().removeAll(var4)) {
            var2 = true;
         }
      }

      if (var2) {
         this.savePluginInfoSilently();
      }
   }

   private void savePluginInfoSilently() {
      try {
         this.managedProject.savePluginInfo();
      } catch (InterchangeSavingException var3) {
         String var2 = "Failed to persist pipeline plugin information when closing the project " + this.managedProject.getProject().getName();
         ErrorLog.log(ProjectPluginInfoImpl.class, var2, var3);
      }
   }

   @Override
   public void applyNewPluginSettings(
      IntegrationEntity var1, Iterable<AppliedPipelinePlugin> var2, Function<AppliedPipelinePlugin, PipelinePluginManifestCache> var3
   ) throws Exception {
      try {
         IntegrationProjectLock.acquire();
         this.appliedPlugins.setAssociations(var1, var2);

         for (AppliedPipelinePlugin var5 : var2) {
            PipelinePluginManifestCache var6 = (PipelinePluginManifestCache)var3.apply(var5);
            if (var6 != null) {
               this.addManifestToCache(var5, var6);
            }
         }

         var1.setDeployDirty(true);
         this.managedProject.saveIntegrationEntity(var1);
         this.managedProject.savePluginInfo();
      } finally {
         IntegrationProjectLock.release();
      }
   }

   private void addManifestToCache(AppliedPipelinePlugin var1, PipelinePluginManifestCache var2) throws DataCacheException {
      String var3 = var1.getPluginId().getName();
      PipelinePluginManifest var4 = var2.getCachedManifest(var3);
      this.manifestCache.add(Collections.singletonList(var4));
   }

   private PipelinePluginManifestCache createManifestCache(ManagedProject var1) {
      Folder var2 = ProjectFolders.getCacheFolder(var1.getLocation());
      String var3 = "PipelinePluginManifestCache";
      Folder var4 = var2.subFolder(var3);
      PipelinePluginManifestCacheFactory var5 = PipelinePluginManifestCacheFactory.newFileBasedCacheFactory(var4);
      return var5.createCache();
   }
}
