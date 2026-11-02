package org.etlforge.integration.client.ui.entity.dependencies.tree;

import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeRenderer;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RequiredObjectsTree;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;

public final class RequiredObjectsTreeUi implements UiProvider {
   private final KongaTree tree;

   public RequiredObjectsTreeUi(RequiredObjectsTree var1) {
      this.tree = new KongaTree(this.buildTreeModel(var1));
      this.tree.setCellRenderer(new IntegrationEntityTreeRenderer());
      this.tree.setRowHeight(20);
      this.tree.expandAll();
   }

   public RequiredObjectsTreeUi(IntegrationEntity var1, ProjectDependencies var2) {
      this(new RequiredObjectsTree(var2, var1));
   }

   private KongaTreeModel buildTreeModel(RequiredObjectsTree var1) {
      RequiredObjectsTreeModelBuilder var2 = new RequiredObjectsTreeModelBuilder();
      return var2.buildModel(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.tree.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   public void setVisibleRowCount(int var1) {
      this.tree.setVisibleRowCount(var1);
   }
}
