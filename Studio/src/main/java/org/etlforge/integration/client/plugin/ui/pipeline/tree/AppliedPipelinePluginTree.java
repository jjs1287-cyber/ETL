package org.etlforge.integration.client.plugin.ui.pipeline.tree;

import java.util.Set;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeRenderer;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.ComponentCustomizer;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.TreeSelectionSource;
import org.etlforge.ui.widget.tree.UserObjectNodeObjectConverter;

public final class AppliedPipelinePluginTree implements UiProvider {
   private final AppliedPipelinePluginTreeModelBuilder modelBuilder;
   private final KongaTree tree;
   private final TreeSelectionSource selectionSource;

   public AppliedPipelinePluginTree(ProjectPluginInfo var1) {
      this(var1, null);
   }

   public AppliedPipelinePluginTree(ProjectPluginInfo var1, String var2) {
      this.modelBuilder = new AppliedPipelinePluginTreeModelBuilder(var1);
      this.modelBuilder.setRootName(var2);
      this.tree = this.createTree();
      this.selectionSource = this.createSelectionSource();
   }

   public KongaTree getWidget() {
      return this.tree;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.tree.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   public TreeSelectionSource asSelectionSource() {
      return this.selectionSource;
   }

   public boolean isEmpty() {
      return this.modelBuilder.isEmpty();
   }

   public Set<AppliedPipelinePlugin> getPlugins() {
      return this.modelBuilder.getPlugins();
   }

   public void refresh() {
      final KongaTreeModel var1 = this.modelBuilder.buildNewModel();
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            AppliedPipelinePluginTree.this.tree.setModel(var1);
            AppliedPipelinePluginTree.this.tree.expandAll();
         }
      });
   }

   public void setProjectPlugins(ProjectPluginInfo var1) {
      this.modelBuilder.setProjectPlugins(var1);
      this.refresh();
   }

   public void customize(final ComponentCustomizer<KongaTree> var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            var1.customize(AppliedPipelinePluginTree.this.tree);
         }
      });
   }

   private KongaTree createTree() {
      KongaTree var1 = new KongaTree(this.modelBuilder.buildNewModel());
      var1.expandAll();
      var1.setCellRenderer(new IntegrationEntityTreeRenderer());
      return var1;
   }

   private TreeSelectionSource createSelectionSource() {
      TreeSelectionSource var1 = new TreeSelectionSource(this.tree);
      var1.setNodeObjectConverter(new UserObjectNodeObjectConverter());
      return var1;
   }
}
