package org.etlforge.integration.client.ui.script.builder.plugin;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.TreeNode;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUtils;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeObjectConverter;
import org.etlforge.ui.widget.tree.TreeSelectionSource;

public final class ScriptPluginTree implements UiProvider, Disposable {
   private final ScriptPluginProvider provider;
   private final ScriptPluginTree.ProviderListener providerListener;
   private final KongaTree tree;
   private final TreeSelectionSource selectionSource;
   private SelectionAction defaultAction;
   private final Action refreshAction;

   public ScriptPluginTree(ScriptPluginProvider var1) {
      this.provider = var1;
      this.providerListener = new ScriptPluginTree.ProviderListener();
      var1.addListener(this.providerListener);
      this.tree = this.createTree();
      this.selectionSource = new TreeSelectionSource(this.tree);
      this.selectionSource.setNodeObjectConverter(new ScriptPluginTree.SelectionConverter());
      this.refreshAction = new ScriptPluginTree.RefreshAction();
      this.installDefaultAction();
   }

   private KongaTree createTree() {
      KongaTree var1 = new KongaTree(this.createModel());
      ScriptBuilderUtils.setTreeStyle(var1);
      var1.setCellRenderer(new PluginTreeNodeRenderer());
      var1.setSelectionMode(KongaTree.SelectionMode.SINGLE_TREE_SELECTION);
      return var1;
   }

   private KongaTreeModel createModel() {
      TreeModelBuilder var1 = new TreeModelBuilder(this.provider);
      return var1.buildModel();
   }

   private void installDefaultAction() {
      SelectionAction var1 = this.getEffectiveDefaultAction();
      this.selectionSource.setDefaultAction(var1);
   }

   private SelectionAction getEffectiveDefaultAction() {
      return this.isEmpty() ? new ScriptPluginTree.LoginActionWrapper() : this.defaultAction;
   }

   private boolean isEmpty() {
      return this.tree.getModel().getRoot().getChildCount() == 0;
   }

   public ScriptPluginProvider getProvider() {
      return this.provider;
   }

   public KongaTree getTree() {
      return this.tree;
   }

   public Action getRefreshAction() {
      return this.refreshAction;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.tree.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   @Override
   public void dispose() {
      this.provider.removeListener(this.providerListener);
   }

   public PipelinePluginManifest getSelectedPlugin() {
      List var1 = this.tree.getSelectedNodes();
      if (var1.size() == 1) {
         KongaTreeNode var2 = (KongaTreeNode)var1.get(0);
         if (var2 instanceof PluginTreeNode) {
            return ((PluginTreeNode)var2).getUserObject();
         }
      }

      return null;
   }

   public void setDefaultAction(SelectionAction var1) {
      this.defaultAction = var1;
      this.installDefaultAction();
   }

   public void addSelectionAction(SelectionAction var1) {
      var1.setSelectionSource(this.selectionSource);
   }

   private final class LoginActionWrapper extends AbstractSelectionAction {
      private LoginActionWrapper() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ScriptPluginTree.this.provider.refresh();
      }
   }

   private final class ProviderListener implements ScriptPluginProviderListener {
      private ProviderListener() {
      }

      @Override
      public void availablePluginsChanged() {
         this.updateTree();
      }

      private void updateTree() {
         final KongaTreeModel var1 = ScriptPluginTree.this.createModel();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               boolean var1x = ScriptPluginTree.this.tree.isExpanded(0);
               ScriptPluginTree.this.tree.setModel(var1);
               if (var1x) {
                  ScriptPluginTree.this.tree.expandRow(0);
               }

               ScriptPluginTree.this.installDefaultAction();
            }
         });
      }

      @Override
      public void serverConnectionChanged(ServerInfo var1) {
         this.updateTree();
         ActionUtils.setEnabledSafely(ScriptPluginTree.this.refreshAction, var1 != null);
      }
   }

   private final class RefreshAction extends AbstractAction {
      public RefreshAction() {
         ActionDecorators.REFRESH.decorate(this);
         this.putValue("ShortDescription", "Refreh the list of available plugins");
         this.setEnabled(ScriptPluginTree.this.provider.getCurrentServer() != null);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         synchronized (ScriptPluginTree.this.provider) {
            if (ScriptPluginTree.this.provider.getCurrentServer() != null) {
               ScriptPluginTree.this.provider.refresh();
            }
         }
      }
   }

   private static final class SelectionConverter implements NodeObjectConverter {
      private SelectionConverter() {
      }

      @Override
      public TreeNode toNode(Object var1) {
         return null;
      }

      @Override
      public Object toObject(TreeNode var1) {
         return var1 instanceof PluginTreeNode ? ((PluginTreeNode)var1).getUserObject() : null;
      }
   }
}
