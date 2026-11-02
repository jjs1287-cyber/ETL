package org.etlforge.integration.client.plugin.ui.pipeline.tree;

import javax.swing.Icon;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

public final class PipelinePluginTreeNode extends DefaultKongaTreeNode {
   private final String displayName;

   public PipelinePluginTreeNode(KongaTreeNode var1, AppliedPipelinePlugin var2) {
      this(var1, var2.getPluginId(), var2.getDisplayName(), true);
   }

   public PipelinePluginTreeNode(KongaTreeNode var1, AppliedPipelinePlugin var2, boolean var3) {
      this(var1, var2.getPluginId(), var2.getDisplayName(), var3);
   }

   public PipelinePluginTreeNode(KongaTreeNode var1, PluginIdentifier var2, String var3) {
      this(var1, var2, var3, true);
   }

   public PipelinePluginTreeNode(KongaTreeNode var1, PluginIdentifier var2, String var3, boolean var4) {
      super(var1, var2, var4);
      this.displayName = var3 != null ? var3 : var2.getName();
   }

   public PluginIdentifier getUserObject() {
      return (PluginIdentifier)super.getUserObject();
   }

   @Override
   public String getDisplayName() {
      return this.displayName;
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return ClientIcons.PLUGIN_16;
   }
}
