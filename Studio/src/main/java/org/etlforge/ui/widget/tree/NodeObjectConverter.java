package org.etlforge.ui.widget.tree;

import javax.swing.tree.TreeNode;

public interface NodeObjectConverter {
   NodeObjectConverter TRIVIAL = new NodeObjectConverter() {
      @Override
      public Object toObject(TreeNode var1) {
         return var1;
      }

      @Override
      public TreeNode toNode(Object var1) {
         return var1 instanceof TreeNode ? (TreeNode)var1 : null;
      }
   };

   Object toObject(TreeNode var1);

   TreeNode toNode(Object var1);

   abstract class Adapter implements NodeObjectConverter {
      @Override
      public TreeNode toNode(Object var1) {
         return null;
      }

      @Override
      public Object toObject(TreeNode var1) {
         return null;
      }
   }
}
