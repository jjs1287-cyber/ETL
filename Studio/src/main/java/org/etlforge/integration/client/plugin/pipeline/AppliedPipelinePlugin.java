package org.etlforge.integration.client.plugin.pipeline;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.util.version.Version;

public final class AppliedPipelinePlugin {
   private final IntegrationEntityId entityId;
   private final PluginIdentifier pluginId;
   private String displayName;
   private final PipelinePosition pipelinePosition;

   public AppliedPipelinePlugin(IntegrationEntityId var1, PipelinePluginManifest var2, PipelinePosition var3) {
      this(var1, var2.getName(), var2.getVersion(), var3);
   }

   public AppliedPipelinePlugin(IntegrationEntityId var1, PipelinePosition var2) {
      this(var1, var2.getPluginId().getName(), var2.getPluginId().getVersion(), var2);
   }

   public AppliedPipelinePlugin(IntegrationEntityId var1, String var2, Version var3, PipelinePosition var4) {
      this.entityId = Preconditions.checkNotNull(var1, "entityId");
      this.pipelinePosition = Preconditions.checkNotNull(var4, "position");
      this.pluginId = new PluginIdentifier(var2, var3);
   }

   public PluginIdentifier getPluginId() {
      return this.pluginId;
   }

   public String getDisplayName() {
      return this.displayName != null ? this.displayName : this.pluginId.getName();
   }

   public void setDisplayName(String var1) {
      this.displayName = var1;
   }

   public PipelinePosition getPipelinePosition() {
      return this.pipelinePosition;
   }

   public IntegrationEntityId getEntityId() {
      return this.entityId;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof AppliedPipelinePlugin ? this.equals((AppliedPipelinePlugin)var1) : false;
      }
   }

   public boolean equals(AppliedPipelinePlugin var1) {
      return this.entityId.equals(var1.entityId) && this.pluginId.equals(var1.pluginId) && this.pipelinePosition.equals(var1.pipelinePosition);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.entityId, this.pluginId, this.pipelinePosition);
   }
}
