package org.etlforge.integration.client.plugin.ui.pipeline.applied;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.pipeline.MutableAppliedPipelinePluginStore;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.client.plugin.ui.pipeline.selector.SelectedPosition;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class AppliedPipelinePluginsModel extends AbstractPropertyBasedModel {
   public static final String PLUGINS = "plugins";
   public static final String DIRTY = "dirty";
   public static final String BUSY = "busy";
   private final IntegrationEntity entity;
   private final Map<AppliedPipelinePlugin, PipelinePluginManifestCache> plugins;
   private final Property<Boolean> dirty = this.newProperty("dirty", Boolean.FALSE);
   private final Property<Boolean> busy = this.newProperty("busy", Boolean.FALSE);
   private final Object lock = new Object();

   public AppliedPipelinePluginsModel(ProjectPluginInfo var1, IntegrationEntity var2) {
      this.plugins = Maps.newHashMap();
      this.entity = var2;
      MutableAppliedPipelinePluginStore var3 = var1.getAppliedPlugins();
      PipelinePluginManifestCache var4 = var1.getManifestCache();

      for (AppliedPipelinePlugin var8 : var3.getAllAssociations(var2)) {
         this.plugins.put(var8, var4);
      }
   }

   public IntegrationEntity getEntity() {
      return this.entity;
   }

   public List<AppliedPipelinePlugin> getPlugins() {
      return Lists.newArrayList(this.plugins.keySet());
   }

   public void addPlugins(List<SelectedPosition> var1) {
      boolean var2 = false;
      synchronized (this.lock) {
         for (SelectedPosition var5 : var1) {
            AppliedPipelinePlugin var6 = var5.asAppliedPipelinePlugin(this.entity);
            if (this.plugins.put(var6, var5.getCache()) == null) {
               var2 = true;
            }
         }
      }

      if (var2) {
         this.onSettingsChanged();
      }
   }

   private void onSettingsChanged() {
      this.sendChange("plugins", null, this.getPlugins());
      this.setDirty(true);
   }

   public void addPlugin(AppliedPipelinePlugin var1, PipelinePluginManifestCache var2) {
      Object var3;
      synchronized (this.lock) {
         var3 = this.plugins.put(var1, var2);
      }

      if (var3 == null) {
         this.onSettingsChanged();
      }
   }

   public void removePlugins(Iterable<AppliedPipelinePlugin> var1) {
      boolean var2 = false;
      synchronized (this.lock) {
         for (AppliedPipelinePlugin var5 : var1) {
            if (this.plugins.remove(var5) != null) {
               var2 = true;
            }
         }
      }

      if (var2) {
         this.onSettingsChanged();
      }
   }

   public Function<AppliedPipelinePlugin, PipelinePluginManifestCache> getCacheLookup() {
      return new Function<AppliedPipelinePlugin, PipelinePluginManifestCache>() {
         public PipelinePluginManifestCache apply(AppliedPipelinePlugin var1) {
            synchronized (AppliedPipelinePluginsModel.this.lock) {
               return AppliedPipelinePluginsModel.this.plugins.get(var1);
            }
         }
      };
   }

   public PipelinePluginManifestCache getManifestCache(PluginIdentifier var1) {
      synchronized (this.lock) {
         for (Entry var4 : this.plugins.entrySet()) {
            if (((AppliedPipelinePlugin)var4.getKey()).getPluginId().equals(var1)) {
               return (PipelinePluginManifestCache)var4.getValue();
            }
         }

         return null;
      }
   }

   public void setBusy(boolean var1) {
      this.busy.set(var1);
   }

   public boolean isBusy() {
      return this.busy.get();
   }

   public boolean isDirty() {
      return this.dirty.get();
   }

   public void setDirty(boolean var1) {
      this.dirty.set(var1);
   }
}
