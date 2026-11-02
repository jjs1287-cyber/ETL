package org.etlforge.integration.client.ui.deploy.history;

import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.integration.client.deploy.DeployedEntityDescriptor;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNodeObjectConverter;
import org.etlforge.ui.widget.tree.TreeSelectionSource;

public class DeployedEntityDescriptorTree implements UiProvider, SelectionSource {
   private final KongaTree tree;
   private final TreeSelectionSource selectionDelegate;
   private final TreePanel treePanel;

   public DeployedEntityDescriptorTree(DeployedEntityDescriptor var1, String var2) {
      this.tree = DeployedEntityDescriptorTreeFactory.newTree(var1);
      this.selectionDelegate = this.createSelectionDelegate(this.tree);
      this.treePanel = new TreePanel(var2, this.tree);
      this.treePanel.activated();
   }

   private TreeSelectionSource createSelectionDelegate(KongaTree var1) {
      TreeSelectionSource var2 = new TreeSelectionSource(var1);
      var2.setNodeObjectConverter(new KongaTreeNodeObjectConverter(var1.getModel()));
      return var2;
   }

   public void expandAll() {
      this.tree.expandAll();
   }

   public void setVisibleRowCount(int var1) {
      this.tree.setVisibleRowCount(var1);
   }

   public void setSelectionMode(KongaTree.SelectionMode var1) {
      this.tree.setSelectionMode(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.treePanel.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   public OrderedSelection getSelection() {
      return this.selectionDelegate.getSelection();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionDelegate.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selectionDelegate.removeSelectionListener(var1);
   }
}
