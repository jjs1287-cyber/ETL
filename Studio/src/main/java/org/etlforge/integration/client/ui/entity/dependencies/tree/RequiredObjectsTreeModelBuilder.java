package org.etlforge.integration.client.ui.entity.dependencies.tree;

import org.etlforge.integration.data.entity.RequiredObjectsTree;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class RequiredObjectsTreeModelBuilder {
   public KongaTreeModel buildModel(RequiredObjectsTree var1) {
      RequiredObjectsTree.Node var2 = var1.getRoot();
      KongaTreeNode var3 = this.getNodeFor(var2, null);
      this.buildBranch(var2, var3);
      return new DefaultKongaTreeModel(var3);
   }

   private KongaTreeNode getNodeFor(RequiredObjectsTree.Node var1, KongaTreeNode var2) {
      IntegrationEntityTreeNode var3 = new IntegrationEntityTreeNode(var2, var1.item);
      var3.setAllowsChildren(true);
      return var3;
   }

   private void buildBranch(RequiredObjectsTree.Node var1, KongaTreeNode var2) {
      for (RequiredObjectsTree.Node var4 : var1.children()) {
         KongaTreeNode var5 = this.getNodeFor(var4, var2);
         this.buildBranch(var4, var5);
      }
   }
}
