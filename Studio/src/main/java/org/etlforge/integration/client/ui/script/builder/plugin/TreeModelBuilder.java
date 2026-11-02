package org.etlforge.integration.client.ui.script.builder.plugin;

import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class TreeModelBuilder {
   private final ScriptPluginProvider provider;

   public TreeModelBuilder(ScriptPluginProvider var1) {
      this.provider = var1;
   }

   public KongaTreeModel buildModel() {
      KongaTreeNode var1;
      synchronized (this.provider) {
         var1 = this.createRoot();
         if (this.isConnected()) {
            this.addPlugins(var1);
         }
      }

      return new DefaultKongaTreeModel(var1);
   }

   private KongaTreeNode createRoot() {
      ServerInfo var1 = this.provider.getCurrentServer();
      return var1 != null
         ? new DefaultKongaTreeNode("Plugins @ " + var1.getName(), true)
         : new DefaultKongaTreeNode("Connect to a server to see available plugins", false);
   }

   private boolean isConnected() {
      return this.provider.getCurrentServer() != null;
   }

   private void addPlugins(KongaTreeNode var1) {
      for (PipelinePluginManifest var3 : this.provider.getAvailablePlugins()) {
         new PluginTreeNode(var1, var3);
      }

      if (var1.getChildCount() == 0) {
         var1.addChild(new DefaultKongaTreeNode("[No plugins available]", false));
      }
   }
}
