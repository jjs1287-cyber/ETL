package org.etlforge.integration.client.ui.script.builder.plugin;

import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class PluginTreeNode extends DefaultKongaTreeNode {
   public PluginTreeNode(KongaTreeNode var1, PipelinePluginManifest var2) {
      super(var1, var2, false);
   }

   @Override
   public String getDisplayName() {
      return this.getUserObject().getDisplayName();
   }

   public PipelinePluginManifest getUserObject() {
      return (PipelinePluginManifest)super.getUserObject();
   }
}
