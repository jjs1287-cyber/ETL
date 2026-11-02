package org.etlforge.integration.client.ui.de.global.tree;

import javax.swing.tree.TreeNode;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.NodeObjectConverter;
import org.etlforge.ui.widget.tree.PreOrderTraverser;

final class SelectionConverter implements NodeObjectConverter {
   private final KongaTreeModel model;

   public SelectionConverter(KongaTreeModel var1) {
      this.model = var1;
   }

   @Override
   public TreeNode toNode(Object var1) {
      if (var1 instanceof String) {
         for (Object var3 : new PreOrderTraverser(this.model)) {
            if (var3 instanceof DataElementNode) {
               DataElementNode var4 = (DataElementNode)var3;
               if (var1.equals(var4.getUserObject())) {
                  return var4;
               }
            }
         }
      }

      return null;
   }

   @Override
   public Object toObject(TreeNode var1) {
      return var1 instanceof DataElementNode ? ((DataElementNode)var1).getUserObject() : null;
   }
}
