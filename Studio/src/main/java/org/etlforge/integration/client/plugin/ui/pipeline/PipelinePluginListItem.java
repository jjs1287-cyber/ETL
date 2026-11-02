package org.etlforge.integration.client.plugin.ui.pipeline;

import javax.swing.JComboBox;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

final class PipelinePluginListItem implements ListDecorator {
   private final PipelinePluginManifest plugin;
   private final String tooltip;

   public PipelinePluginListItem(PipelinePluginManifest var1) {
      this.plugin = var1;
      this.tooltip = new PipelinePluginToolTip(var1).get();
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setIcon(ClientIcons.PLUGIN_16);
      var1.setText(this.plugin.getDisplayName());
      var1.setToolTip(this.tooltip);
   }

   public PipelinePluginManifest getPlugin() {
      return this.plugin;
   }

   public void applyToolTipTo(JComboBox var1) {
      var1.setToolTipText(this.tooltip);
   }
}
