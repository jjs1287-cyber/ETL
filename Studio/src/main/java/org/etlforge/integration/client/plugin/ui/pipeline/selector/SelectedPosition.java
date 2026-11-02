package org.etlforge.integration.client.plugin.ui.pipeline.selector;

import com.google.common.base.Preconditions;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;

public final class SelectedPosition {
   private final PipelinePosition position;
   private final PipelinePluginManifestCache cache;

   public SelectedPosition(PipelinePosition var1, PipelinePluginManifestCache var2) {
      this.position = Preconditions.checkNotNull(var1, "position");
      this.cache = Preconditions.checkNotNull(var2, "cache");
   }

   public PipelinePosition getPosition() {
      return this.position;
   }

   public PipelinePluginManifestCache getCache() {
      return this.cache;
   }

   public AppliedPipelinePlugin asAppliedPipelinePlugin(IntegrationEntity var1) {
      AppliedPipelinePlugin var2 = new AppliedPipelinePlugin(var1.getID(), this.position);
      this.setDisplayName(var2);
      return var2;
   }

   private void setDisplayName(AppliedPipelinePlugin var1) {
      try {
         PipelinePluginManifest var2 = this.cache.getCachedManifest(this.position.getPluginId().getName());
         var1.setDisplayName(var2.getDisplayName());
      } catch (DataCacheException var3) {
         ErrorLog.log(this.getClass(), "Failed to lookup the display name for plugin " + this.position.getPluginId(), var3);
      }
   }
}
