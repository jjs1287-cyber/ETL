package org.etlforge.integration.client.ui.entity.tree;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.PreOrderTraverser;

final class IntegrationEntityTreeNodeCreator {
   private final KongaTreeModel model;
   private IntegrationEntityTreeNode parentNode;

   public IntegrationEntityTreeNodeCreator(KongaTreeModel var1) {
      Preconditions.checkNotNull(var1, "model");
      this.model = var1;
   }

   public IntegrationEntityTreeNode createNode(IntegrationEntity var1) {
      this.parentNode = this.findParentNode(var1);
      return new IntegrationEntityTreeNode(var1);
   }

   public IntegrationEntityTreeNode getParentNode() {
      return this.parentNode;
   }

   private IntegrationEntityTreeNode findParentNode(IntegrationEntity var1) {
      IntegrationEntityTreeNode var2 = null;
      IntegrationEntity var3 = var1.getParent();

      for (Object var5 : new PreOrderTraverser(this.model)) {
         if (((KongaTreeNode)var5).getUserObject() == var3) {
            var2 = (IntegrationEntityTreeNode)var5;
            break;
         }
      }

      if (var2 == null && this.model.getFilter() != null) {
         var2 = this.doUnfilteredSearchForParent(var3);
      }

      return var2;
   }

   private IntegrationEntityTreeNode doUnfilteredSearchForParent(IntegrationEntity var1) {
      return this.doUnfilteredSearchForParent(var1, this.model.getRoot());
   }

   private IntegrationEntityTreeNode doUnfilteredSearchForParent(IntegrationEntity var1, KongaTreeNode var2) {
      if (var2.getUserObject() == var1) {
         return (IntegrationEntityTreeNode)var2;
      }

      for (KongaTreeNode var4 : var2.children()) {
         IntegrationEntityTreeNode var5 = this.doUnfilteredSearchForParent(var1, var4);
         if (var5 != null) {
            return var5;
         }
      }

      return null;
   }
}
