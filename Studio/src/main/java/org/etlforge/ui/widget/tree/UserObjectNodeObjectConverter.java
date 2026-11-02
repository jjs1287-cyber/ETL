package org.etlforge.ui.widget.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class UserObjectNodeObjectConverter implements NodeObjectConverter {
   private final Class<?> userObjectClass;

   public UserObjectNodeObjectConverter() {
      this(Object.class);
   }

   public UserObjectNodeObjectConverter(Class<?> var1) {
      this.userObjectClass = var1;
   }

   @Override
   public Object toObject(TreeNode var1) {
      Object var2 = null;
      if (var1 instanceof KongaTreeNode) {
         var2 = ((KongaTreeNode)var1).getUserObject();
      } else if (var1 instanceof DefaultMutableTreeNode) {
         var2 = ((DefaultMutableTreeNode)var1).getUserObject();
      }

      return this.userObjectClass.isInstance(var2) ? var2 : null;
   }

   @Override
   public TreeNode toNode(Object var1) {
      return null;
   }
}
