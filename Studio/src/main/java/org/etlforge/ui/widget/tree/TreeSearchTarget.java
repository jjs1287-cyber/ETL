package org.etlforge.ui.widget.tree;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.etlforge.ui.util.find.SearchDirection;
import org.etlforge.ui.util.find.SearchTarget;
import org.etlforge.ui.util.find.TextSearchParameters;

public class TreeSearchTarget implements SearchTarget {
   private final KongaTree tree;
   private TreeSearchTarget.NodeMatch currentMatch;

   public TreeSearchTarget(KongaTree var1) {
      this.tree = var1;
   }

   public TreeSearchTarget.NodeMatch nextMatch(TextSearchParameters var1) {
      TreeNode var2 = this.findNode(var1);
      if (var2 != null) {
         this.currentMatch = new TreeSearchTarget.NodeMatch(this.tree, var2);
         this.tree.makeNodeSelected(var2, false);
         return this.currentMatch;
      } else {
         return null;
      }
   }

   private TreeNode findNode(TextSearchParameters var1) {
      TreeNodeFinder var2 = this.createFinder(var1);
      TreeNode var3 = var2.findNode();
      if (var3 == null && var1.isWrap()) {
         TreeNodeFinder var4 = this.createWrappingFinder(var1);
         var3 = var4.findNode();
      }

      return var3;
   }

   private TreeNodeFinder createFinder(TextSearchParameters var1) {
      return this.currentMatch == null ? new TreeNodeFinder(this.tree, var1) : new TreeNodeFinder(this.tree, this.currentMatch.node, var1);
   }

   private TreeNodeFinder createWrappingFinder(TextSearchParameters var1) {
      TreeNode var2;
      if (var1.getDirection() == SearchDirection.DOWN) {
         TreePath var3 = this.tree.getPathForRow(0);
         var2 = (TreeNode)var3.getLastPathComponent();
      } else {
         var2 = this.getLastNode();
      }

      return new TreeNodeFinder(this.tree, var2, var1);
   }

   private TreeNode getLastNode() {
      KongaTreeModel var1 = this.tree.getModel();
      Object var2 = TreeUtils.getLastNode(var1, var1.getRoot());
      return var2 instanceof TreeNode ? (TreeNode)var2 : null;
   }

   public static final class NodeMatch implements SearchTarget.Match {
      private final KongaTree tree;
      private final TreeNode node;

      public NodeMatch(KongaTree var1, TreeNode var2) {
         this.tree = var1;
         this.node = var2;
      }

      public KongaTree getTree() {
         return this.tree;
      }

      public TreeNode getNode() {
         return this.node;
      }

      @Override
      public int compareLocation(SearchTarget.Match var1) {
         int var2 = this.getRowNumber();
         int var3 = ((TreeSearchTarget.NodeMatch)var1).getRowNumber();
         return var2 - var3;
      }

      private int getRowNumber() {
         TreePath var1 = this.tree.getPath(this.node);
         return this.tree.getRowForPath(var1);
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 31 * var1 + System.identityHashCode(this.tree);
         return 31 * var1 + System.identityHashCode(this.node);
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof TreeSearchTarget.NodeMatch)) {
            return false;
         }

         TreeSearchTarget.NodeMatch var2 = (TreeSearchTarget.NodeMatch)var1;
         return this.tree == var2.tree && this.node == var2.node;
      }
   }
}
