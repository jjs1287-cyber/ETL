package org.etlforge.ui.widget.tree;

import java.util.List;
import javax.swing.tree.TreeNode;
import org.etlforge.ui.util.find.SearchDirection;
import org.etlforge.ui.util.find.TextSearchParameters;
import org.etlforge.util.text.WordFinder;

public final class TreeNodeFinder {
   private final KongaTree tree;
   private final TreeNode startNode;
   private final TextSearchParameters params;
   private String currentSearchString;

   public TreeNodeFinder(KongaTree var1, TextSearchParameters var2) {
      this(var1, getStartNode(var1), var2);
   }

   public TreeNodeFinder(KongaTree var1, TreeNode var2, TextSearchParameters var3) {
      this.tree = var1;
      this.startNode = var2 == null ? getStartNode(var1) : var2;
      this.params = var3;
   }

   private static TreeNode getStartNode(KongaTree var0) {
      List var1 = var0.getSelectedNodes();
      return var1 != null && var1.size() == 1 ? (TreeNode)var1.get(0) : var0.getModel().getRoot();
   }

   public final TreeNode findNode() {
      this.currentSearchString = this.applyCase(this.params.getSearchString());
      TreeNode var1 = this.startNode;
      if (this.isTreeNodeMatch(var1)) {
         var1 = this.getNextNode(var1, this.params.getDirection());
      }

      return this.searchTree(var1);
   }

   private String applyCase(String var1) {
      return this.params.isCaseSensitive() ? var1 : var1.toLowerCase();
   }

   private TreeNode getNextNode(TreeNode var1, SearchDirection var2) {
      if (var1 == null) {
         return null;
      }

      TreeNode var3 = null;
      if (var2 == SearchDirection.DOWN) {
         var3 = TreeUtils.getNextNodeDown(this.tree.getModel(), var1);
      } else {
         var3 = TreeUtils.getNextNodeUp(this.tree.getModel(), var1);
      }

      return var3;
   }

   private TreeNode searchTree(TreeNode var1) {
      if (this.isTreeNodeMatch(var1)) {
         return var1;
      }

      for (TreeNode var2 = this.getNextNode(var1, this.params.getDirection()); var2 != null; var2 = this.getNextNode(var2, this.params.getDirection())) {
         if (this.isTreeNodeMatch(var2)) {
            return var2;
         }
      }

      return null;
   }

   private boolean isTreeNodeMatch(TreeNode var1) {
      if (var1 == null) {
         return false;
      }

      if (this.params.isWholeWordsOnly()) {
         return this.getStartPosition(this.getNodeText(var1)) >= 0;
      }

      String var2 = this.getTextToSearch(var1);
      return var2.contains(this.currentSearchString);
   }

   private int getStartPosition(String var1) {
      String var2 = this.applyCase(var1);
      WordFinder var3 = new WordFinder(var2);
      String var4 = this.currentSearchString;
      return this.params.getDirection() == SearchDirection.DOWN ? var3.getStartOfNextMatch(var4, 0) : var3.getStartOfPreviousMatch(var4, var2.length());
   }

   private String getTextToSearch(TreeNode var1) {
      String var2 = this.getNodeText(var1);
      return this.applyCase(var2);
   }

   private String getNodeText(TreeNode var1) {
      return var1 instanceof KongaTreeNode ? ((KongaTreeNode)var1).getDisplayName() : var1.toString();
   }
}
