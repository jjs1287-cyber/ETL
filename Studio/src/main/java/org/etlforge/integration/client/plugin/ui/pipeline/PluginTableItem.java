package org.etlforge.integration.client.plugin.ui.pipeline;

import com.google.common.collect.ComparisonChain;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.integration.plugin.pipeline.RelativePosition;
import org.etlforge.util.version.Version;

public final class PluginTableItem implements Comparable<PluginTableItem> {
   private final PluginIdentifier pluginId;
   private final String pluginDisplayName;
   private final PipelinePosition position;

   public PluginTableItem(PipelinePosition var1, String var2) {
      this.pluginId = var1.getPluginId();
      this.position = var1;
      this.pluginDisplayName = var2;
   }

   public PluginTableItem(AppliedPipelinePlugin var1) {
      this.pluginId = var1.getPluginId();
      this.pluginDisplayName = var1.getDisplayName();
      this.position = var1.getPipelinePosition();
   }

   public AppliedPipelinePlugin asAppliedPipelinePlugin(IntegrationEntity var1) {
      AppliedPipelinePlugin var2 = new AppliedPipelinePlugin(var1.getID(), this.position);
      var2.setDisplayName(this.pluginDisplayName);
      return var2;
   }

   public PluginIdentifier getPluginId() {
      return this.pluginId;
   }

   public String getPluginName() {
      return this.pluginId.getName();
   }

   public Version getPluginVersion() {
      return this.pluginId.getVersion();
   }

   public String getDisplayName() {
      return this.pluginDisplayName;
   }

   public PipelinePosition getPipelinePosition() {
      return this.position;
   }

   public RelativePosition getRelativePosition() {
      return this.position.getRelativePosition();
   }

   public boolean isGlobal() {
      return !this.position.isOptional();
   }

   public int compareTo(PluginTableItem var1) {
      return ComparisonChain.start()
         .compare(this.position, var1.position, new PositionComparator())
         .compare(this.pluginDisplayName, var1.pluginDisplayName)
         .result();
   }
}
