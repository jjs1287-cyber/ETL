package org.etlforge.ui.widget.tree;

import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public final class TreeUtils {
   public static TreePath getPathForNode(JTree var0, TreeNode var1) {
      LinkedList var2 = new LinkedList();
      Object var3 = var0.getModel().getRoot();

      while (var1 != null) {
         var2.addFirst(var1);
         if (var1 == var3) {
            return new TreePath(var2.toArray());
         }

         var1 = var1.getParent();
      }

      return null;
   }

   public static Object getPreviousSibling(TreeModel var0, TreeNode var1) {
      TreeNode var2 = var1.getParent();
      if (var2 == null) {
         return null;
      }

      int var3 = var0.getIndexOfChild(var2, var1);
      return var3 <= 0 ? null : var0.getChild(var2, var3 - 1);
   }

   public static Object getNextSibling(TreeModel var0, TreeNode var1) {
      TreeNode var2 = var1.getParent();
      if (var2 == null) {
         return null;
      }

      int var3 = var0.getIndexOfChild(var2, var1);
      return var3 >= 0 && var3 < var0.getChildCount(var2) - 1 ? var0.getChild(var2, var3 + 1) : null;
   }

   public static Object getFirstChild(TreeModel var0, Object var1) {
      int var2 = var0.getChildCount(var1);
      return var2 > 0 ? var0.getChild(var1, 0) : null;
   }

   public static Object getLastChild(TreeModel var0, Object var1) {
      int var2 = var0.getChildCount(var1);
      return var2 > 0 ? var0.getChild(var1, var2 - 1) : null;
   }

   public static TreeNode getNextNodeDown(TreeModel var0, TreeNode var1) {
      TreeNode var2 = null;
      if (var0.getChildCount(var1) == 0) {
         var2 = (TreeNode)getNextSibling(var0, var1);
         if (var2 == null) {
            TreeNode var3 = var1.getParent();

            while (var3 != null && var2 == null) {
               var2 = (TreeNode)getNextSibling(var0, var3);
               if (var2 == null) {
                  var3 = var3.getParent();
               }
            }
         }
      } else {
         var2 = (TreeNode)getFirstChild(var0, var1);
      }

      return var2;
   }

   public static TreeNode getNextNodeUp(TreeModel var0, TreeNode var1) {
      TreeNode var2 = (TreeNode)getPreviousSibling(var0, var1);
      if (var2 == null) {
         var2 = var1.getParent();
      } else {
         while (var0.getChildCount(var2) > 0) {
            var2 = (TreeNode)getLastChild(var0, var2);
         }
      }

      return var2;
   }

   public static Object getLastNode(TreeModel var0, Object var1) {
      int var2 = var0.getChildCount(var1);
      if (var2 == 0) {
         return var1;
      }

      Object var3 = var0.getChild(var1, var2 - 1);
      return getLastNode(var0, var3);
   }

   public static boolean hasFolders(KongaTreeModel var0) {
      KongaTreeNode var1 = var0.getRoot();

      for (KongaTreeNode var3 : var1.depthFirstEnumeration()) {
         if (var3 != var1 && !var3.isLeaf()) {
            return true;
         }
      }

      return false;
   }

   public static KongaTreeModel createFlatModel(Object var0, Object... var1) {
      return createFlatModel(var0, Arrays.asList(var1));
   }

   public static KongaTreeModel createFlatModel(Object var0, Iterable<?> var1) {
      DefaultKongaTreeNode var2 = new DefaultKongaTreeNode(var0, true);

      for (Object var4 : var1) {
         new DefaultKongaTreeNode(var2, var4, false);
      }

      return new DefaultKongaTreeModel(var2);
   }

   private TreeUtils() {
   }
}
