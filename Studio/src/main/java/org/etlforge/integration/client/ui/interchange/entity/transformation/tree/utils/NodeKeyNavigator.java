package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import java.awt.event.KeyEvent;

final class NodeKeyNavigator {
   private final TreeComponent treeComponent;

   public NodeKeyNavigator(TreeComponent var1) {
      this.treeComponent = var1;
   }

   public void checkNavigation(KeyEvent var1) {
      if (!var1.isControlDown()) {
         Node var2 = this.treeComponent.getHighlightedNode();
         if (var2 != null) {
            switch (var1.getKeyCode()) {
               case 37:
                  this.navigateLeft(var2);
                  var1.consume();
                  break;
               case 38:
                  this.navigateUp(var2);
                  var1.consume();
                  break;
               case 39:
                  this.navigateRight(var2);
                  var1.consume();
                  break;
               case 40:
                  this.navigateDown(var2);
                  var1.consume();
            }
         }
      }
   }

   private void navigateLeft(Node var1) {
      if (!var1.isFolder() || !var1.isPartiallyExpanded() && !var1.isFullyExpanded()) {
         if (var1.m_parent != null) {
            this.highlightNode(var1.m_parent);
         }
      } else {
         this.toggleExpansion(var1);
      }
   }

   private void toggleExpansion(Node var1) {
      this.treeComponent.toggleNodeExpansion(var1);
   }

   private void highlightNode(Node var1) {
      this.treeComponent.showNode(var1, true);
   }

   private void navigateUp(Node var1) {
      Node var2 = this.getNextUpNode(var1);
      if (var2 != null) {
         this.highlightNode(var2);
      }
   }

   private Node getNextUpNode(Node var1) {
      Node var2 = this.getLastVisiblePreviousSibling(var1);
      return var2 != null ? this.deepestVisibleDescendant(var2) : var1.m_parent;
   }

   private Node getLastVisiblePreviousSibling(Node var1) {
      for (Node var2 = var1.getPreviousSibling(); var2 != null; var2 = var2.getPreviousSibling()) {
         if (!var2.isHidden()) {
            return var2;
         }
      }

      return null;
   }

   private Node deepestVisibleDescendant(Node var1) {
      if (var1.isFolder() && (var1.isFullyExpanded() || var1.isPartiallyExpanded())) {
         Node var2 = this.getLastVisibleChild(var1);
         if (var2 != null) {
            return this.deepestVisibleDescendant(var2);
         }
      }

      return var1;
   }

   private Node getLastVisibleChild(Node var1) {
      for (Node var2 = var1.getLastChild(); var2 != null; var2 = var2.getPreviousSibling()) {
         if (!var2.isHidden()) {
            return var2;
         }
      }

      return null;
   }

   private void navigateRight(Node var1) {
      if (var1.isFolder()) {
         if (var1.isExpanded()) {
            this.navigateDown(var1);
         } else {
            this.toggleExpansion(var1);
         }
      }
   }

   private void navigateDown(Node var1) {
      Node var2 = this.getNextNodeDown(var1);
      if (var2 != null) {
         this.highlightNode(var2);
      }
   }

   private Node getNextNodeDown(Node var1) {
      if (var1.isPartiallyExpanded() || var1.isFullyExpanded() && var1.hasChildren()) {
         Node var2 = this.getFirstVisibleChild(var1);
         if (var2 != null) {
            return var2;
         }
      }

      for (Node var4 = var1; var4 != null; var4 = var4.m_parent) {
         Node var3 = this.getFirstVisibleSibling(var1);
         if (var3 != null) {
            return var3;
         }

         var1 = var4;
      }

      return null;
   }

   private Node getFirstVisibleChild(Node var1) {
      for (Node var2 = var1.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (!var2.isHidden()) {
            return var2;
         }
      }

      return null;
   }

   private Node getFirstVisibleSibling(Node var1) {
      for (Node var2 = var1.getNextSibling(); var2 != null; var2 = var2.getNextSibling()) {
         if (!var2.isHidden()) {
            return var2;
         }
      }

      return null;
   }
}
