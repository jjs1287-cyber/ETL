package org.etlforge.ui.widget.tree;

import javax.swing.tree.TreeNode;
import org.etlforge.collections.IterableEnumeration;

public class KongaTreeNodeObjectConverter implements NodeObjectConverter {
   private final KongaTreeModel model;

   public KongaTreeNodeObjectConverter(KongaTreeModel var1) {
      if (var1 == null) {
         throw new NullPointerException("model");
      }

      this.model = var1;
   }

   public KongaTreeNode toNode(Object var1) {
      KongaTreeNode var2 = this.model.getRoot();
      return this.search(var2, var1);
   }

   private KongaTreeNode search(KongaTreeNode var1, Object var2) {
      Object var3 = var1.getUserObject();
      if (var3 != null && var3.equals(var2)) {
         return var1;
      }

      IterableEnumeration var4 = var1.children();

      while (var4.hasMoreElements()) {
         KongaTreeNode var5 = this.search((KongaTreeNode)var4.nextElement(), var2);
         if (var5 != null) {
            return var5;
         }
      }

      return null;
   }

   @Override
   public Object toObject(TreeNode var1) {
      return var1 instanceof KongaTreeNode ? ((KongaTreeNode)var1).getUserObject() : null;
   }
}
