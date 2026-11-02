package org.etlforge.integration.client.ui;

import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.integration.client.ui.crom.tree.CromTreeWidget;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public final class MappingStructureTreeWidget implements UiProvider {
   private final TreePanel treePanel;
   private final CromTreeWidget tree = new CromTreeWidget();

   public MappingStructureTreeWidget(String var1) {
      this(var1, null);
   }

   public MappingStructureTreeWidget(String var1, MappingTreeStructure var2) {
      this.treePanel = this.tree.inTreePanel(var1);
      if (var2 != null) {
         this.populate(var2);
      }
   }

   public void displayWaitNode() {
      this.tree.displayWaitNode();
   }

   public void removeWaitNode() {
      this.tree.removeWaitNode();
   }

   public void populate(MappingTreeStructure var1) {
      this.tree.displayWaitNode();
      MappingStructureTreeWidget.ModelBuilder var2 = new MappingStructureTreeWidget.ModelBuilder(var1);
      var2.execute();
   }

   public void setRootVisible(boolean var1) {
      this.tree.setRootVisible(var1);
   }

   public void expandAll() {
      this.tree.expandAll();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.treePanel.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.treePanel.requestFocus();
   }

   private class ModelBuilder extends KongaSwingWorker<CROM_Tree, Void> {
      private final MappingTreeStructure structure;

      public ModelBuilder(MappingTreeStructure var2) {
         this.structure = var2;
      }

      protected CROM_Tree doInBackground() throws Exception {
         return CROM_Tree.fromMappingStructure(this.structure);
      }

      protected void onSuccess(CROM_Tree var1) {
         MappingStructureTreeWidget.this.tree.setCromTree(var1);
      }

      @Override
      protected void onFailure(Throwable var1) {
         MappingStructureTreeWidget.this.tree.removeWaitNode();
      }
   }
}
