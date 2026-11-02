package org.etlforge.ui.widget.tree;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.etlforge.collections.IterableEnumeration;
import org.etlforge.ui.widget.tree.event.TreeModelListeners;

public class DefaultKongaTreeModel implements KongaTreeModel {
   private KongaTreeNode rootNode;
   private final DefaultKongaTreeModel.FilteredEventHandler eventHandler;
   private final TreeModelListeners listeners;
   private KongaTreeFilter filter;

   public DefaultKongaTreeModel(KongaTreeNode var1) {
      this.rootNode = var1;
      this.eventHandler = new DefaultKongaTreeModel.FilteredEventHandler();
      this.listeners = new TreeModelListeners();
   }

   @Override
   public KongaTreeNode getRoot() {
      return this.rootNode;
   }

   public void setRoot(KongaTreeNode var1) {
      KongaTreeNode var2 = this.rootNode;
      this.rootNode = var1;
      if (var1 == null && var2 != null) {
         this.fireTreeStructureChanged(this, null);
      } else {
         this.nodeStructureChanged(var1);
      }
   }

   @Override
   public void setFilter(KongaTreeFilter var1) {
      this.filter = var1;
      this.fireTreeStructureChanged(this, new TreePath(this.rootNode));
   }

   @Override
   public KongaTreeFilter getFilter() {
      return this.filter;
   }

   @Override
   public TreePath getPath(Object var1) {
      if (var1 instanceof TreeNode) {
         TreeNode var2 = (TreeNode)var1;
         LinkedList var3 = new LinkedList();

         while (var2 != null) {
            var3.addFirst(var2);
            if (var2 == this.rootNode) {
               return new TreePath(var3.toArray());
            }

            var2 = var2.getParent();
         }
      }

      return null;
   }

   @Override
   public int getChildCount(Object var1) {
      if (!(var1 instanceof KongaTreeNode)) {
         return 0;
      }

      KongaTreeNode var2 = (KongaTreeNode)var1;
      if (this.filter == null) {
         return var2.getChildCount();
      }

      int var3 = 0;
      IterableEnumeration var4 = var2.children();

      while (var4.hasMoreElements()) {
         if (this.filter.apply((KongaTreeNode)var4.nextElement())) {
            var3++;
         }
      }

      return var3;
   }

   @Override
   public boolean isLeaf(Object var1) {
      return var1 instanceof KongaTreeNode ? ((KongaTreeNode)var1).isLeaf() : false;
   }

   @Override
   public void addTreeModelListener(TreeModelListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeTreeModelListener(TreeModelListener var1) {
      this.listeners.remove(var1);
   }

   public KongaTreeNode getChild(Object var1, int var2) {
      if (var1 instanceof KongaTreeNode) {
         KongaTreeNode var3 = (KongaTreeNode)var1;
         if (var2 < 0 || var2 > var3.getChildCount()) {
            return null;
         }

         if (this.filter == null) {
            return var3.getChildAt(var2);
         }

         int var4 = 0;
         IterableEnumeration var5 = var3.children();

         while (var5.hasMoreElements()) {
            KongaTreeNode var6 = (KongaTreeNode)var5.nextElement();
            if (this.filter.apply(var6)) {
               if (var4 == var2) {
                  return var6;
               }

               var4++;
            }
         }
      }

      return null;
   }

   @Override
   public int getIndexOfChild(Object var1, Object var2) {
      if (var1 != null && var2 != null) {
         if (var1 instanceof KongaTreeNode && var2 instanceof KongaTreeNode) {
            KongaTreeNode var3 = (KongaTreeNode)var1;
            if (this.filter == null) {
               return var3.getIndex((KongaTreeNode)var2);
            }

            int var4 = 0;
            IterableEnumeration var5 = var3.children();

            while (var5.hasMoreElements()) {
               KongaTreeNode var6 = (KongaTreeNode)var5.nextElement();
               if (this.filter.apply(var6)) {
                  if (var6 == var2) {
                     return var4;
                  }

                  var4++;
               }
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   @Override
   public void valueForPathChanged(TreePath var1, Object var2) {
      KongaTreeNode var3 = (KongaTreeNode)var1.getLastPathComponent();
      var3.setUserObject(var2);
      this.nodeChanged(var3);
   }

   protected void fireTreeNodesChanged(Object var1, Object[] var2, int[] var3, Object[] var4) {
      TreeModelEvent var5 = new TreeModelEvent(var1, var2, var3, var4);
      this.eventHandler.treeNodesChanged(var5);
   }

   protected void fireTreeNodesInserted(Object var1, Object[] var2, int[] var3, Object[] var4) {
      TreeModelEvent var5 = new TreeModelEvent(var1, var2, var3, var4);
      this.eventHandler.treeNodesInserted(var5);
   }

   protected void fireTreeNodesRemoved(Object var1, Object[] var2, int[] var3, Object[] var4) {
      TreeModelEvent var5 = new TreeModelEvent(var1, var2, var3, var4);
      this.eventHandler.treeNodesRemoved(var5);
   }

   protected void fireTreeStructureChanged(Object var1, Object[] var2, int[] var3, Object[] var4) {
      TreeModelEvent var5 = new TreeModelEvent(var1, var2, var3, var4);
      this.eventHandler.treeStructureChanged(var5);
   }

   public void fireTreeStructureChanged(Object var1, TreePath var2) {
      TreeModelEvent var3 = new TreeModelEvent(var1, var2);
      this.eventHandler.treeStructureChanged(var3);
   }

   public void nodesWereInserted(KongaTreeNode var1, int[] var2) {
      if (this.listeners != null && var1 != null && var2 != null && var2.length > 0) {
         int var3 = var2.length;
         Object[] var4 = new Object[var3];

         for (int var5 = 0; var5 < var3; var5++) {
            var4[var5] = var1.getChildAt(var2[var5]);
         }

         this.fireTreeNodesInserted(this, this.getPathToRoot(var1), var2, var4);
      }
   }

   @Override
   public void nodeChanged(KongaTreeNode var1) {
      if (this.listeners != null && var1 != null) {
         KongaTreeNode var2 = var1.getParent();
         if (var2 != null) {
            int var3 = var2.getIndex(var1);
            if (var3 != -1) {
               int[] var4 = new int[]{var3};
               this.nodesChanged(var2, var4);
            }
         } else if (var1 == this.getRoot()) {
            this.nodesChanged(var1, null);
         }
      }
   }

   private void nodesChanged(KongaTreeNode var1, int[] var2) {
      if (var1 != null) {
         if (var2 != null) {
            int var3 = var2.length;
            if (var3 > 0) {
               Object[] var4 = new Object[var3];

               for (int var5 = 0; var5 < var3; var5++) {
                  var4[var5] = var1.getChildAt(var2[var5]);
               }

               this.fireTreeNodesChanged(this, this.getPathToRoot(var1), var2, var4);
            }
         } else if (var1 == this.getRoot()) {
            this.fireTreeNodesChanged(this, this.getPathToRoot(var1), null, null);
         }
      }
   }

   protected void nodeStructureChanged(KongaTreeNode var1) {
      if (var1 != null) {
         this.fireTreeStructureChanged(this, this.getPathToRoot(var1), null, null);
      }
   }

   public TreeNode[] getPathToRoot(Object var1) {
      return this.getPathToRoot((KongaTreeNode)var1, 0);
   }

   protected TreeNode[] getPathToRoot(TreeNode var1, int var2) {
      TreeNode[] var3;
      if (var1 == null) {
         if (var2 == 0) {
            return null;
         }

         var3 = new TreeNode[var2];
      } else {
         var2++;
         if (var1 == this.getRoot()) {
            var3 = new TreeNode[var2];
         } else {
            var3 = this.getPathToRoot(var1.getParent(), var2);
         }

         var3[var3.length - var2] = var1;
      }

      return var3;
   }

   private void nodesWereRemoved(KongaTreeNode var1, int[] var2, Object[] var3) {
      if (var1 != null && var2 != null) {
         this.fireTreeNodesRemoved(this, this.getPathToRoot(var1), var2, var3);
      }
   }

   public void insertNode(KongaTreeNode var1, KongaTreeNode var2) {
      var2.addChild(var1);
      int[] var3 = new int[]{var2.getIndex(var1)};
      this.nodesWereInserted(var2, var3);
   }

   public void removeNode(KongaTreeNode var1) {
      KongaTreeNode var2 = var1.getParent();
      if (var2 == null) {
         throw new IllegalArgumentException("node does not have a parent.");
      }

      int[] var3 = new int[1];
      Object[] var4 = new Object[1];
      var3[0] = var2.getIndex(var1);
      var2.remove(var3[0]);
      var4[0] = var1;
      this.nodesWereRemoved(var2, var3, var4);
   }

   public void sortChildren(KongaTreeNode var1) {
      if (var1.sortChildren()) {
         this.nodeStructureChanged(var1);
      }
   }

   private class FilteredEventHandler implements TreeModelListener {
      private FilteredEventHandler() {
      }

      @Override
      public void treeNodesChanged(TreeModelEvent var1) {
         TreeModelEvent var2 = this.getFilteredEvent(var1);
         DefaultKongaTreeModel.this.listeners.call().treeNodesChanged(var2);
      }

      private TreeModelEvent getFilteredEvent(TreeModelEvent var1) {
         if (DefaultKongaTreeModel.this.filter == null) {
            return var1;
         }

         Object[] var2 = var1.getChildren();
         return var2 == null ? var1 : this.applyFilterToEvent(var1, var2);
      }

      private TreeModelEvent applyFilterToEvent(TreeModelEvent var1, Object[] var2) {
         Object var3 = var1.getTreePath().getLastPathComponent();
         ArrayList var4 = Lists.newArrayList();
         ArrayList var5 = Lists.newArrayList();

         for (Object var9 : var2) {
            int var10 = DefaultKongaTreeModel.this.getIndexOfChild(var3, var9);
            if (var10 >= 0) {
               var4.add(var9);
               var5.add(var10);
            }
         }

         return new TreeModelEvent(var1.getSource(), var1.getPath(), Ints.toArray(var5), var4.toArray());
      }

      @Override
      public void treeNodesInserted(TreeModelEvent var1) {
         TreeModelEvent var2 = this.getFilteredEvent(var1);
         DefaultKongaTreeModel.this.listeners.call().treeNodesInserted(var2);
      }

      @Override
      public void treeNodesRemoved(TreeModelEvent var1) {
         TreeModelEvent var2 = this.getFilteredNodesRemovedEvent(var1);
         if (var2 != null) {
            DefaultKongaTreeModel.this.listeners.call().treeNodesRemoved(var2);
         }
      }

      private TreeModelEvent getFilteredNodesRemovedEvent(TreeModelEvent var1) {
         if (DefaultKongaTreeModel.this.filter == null) {
            return var1;
         }

         KongaTreeNode var2 = (KongaTreeNode)var1.getTreePath().getLastPathComponent();
         Object[] var3 = var1.getChildren();
         int[] var4 = var1.getChildIndices();
         int var5 = var4[var4.length - 1];
         int var6 = var4[0];
         int var7 = 0;
         int var8 = 0;
         int var9 = 0;
         ArrayList var10 = Lists.newArrayList();
         ArrayList var11 = Lists.newArrayList();

         for (int var12 = 0; var12 <= var5; var12++) {
            boolean var13 = var12 == var6;
            Object var14 = var13 ? var3[var7] : var2.getChildAt(var8);
            if (DefaultKongaTreeModel.this.filter.apply((KongaTreeNode)var14)) {
               if (var13) {
                  var10.add(var9);
                  var11.add(var14);
               }

               var9++;
            }

            if (var13) {
               if (++var7 == var4.length) {
                  break;
               }

               var6 = var4[var7];
            } else {
               var8++;
            }
         }

         return var10.isEmpty() ? null : new TreeModelEvent(var1.getSource(), var1.getPath(), Ints.toArray(var10), var11.toArray());
      }

      @Override
      public void treeStructureChanged(TreeModelEvent var1) {
         if (DefaultKongaTreeModel.this.filter == null) {
            DefaultKongaTreeModel.this.listeners.call().treeStructureChanged(var1);
         } else {
            this.sendFilteredEvent(var1);
         }
      }

      private void sendFilteredEvent(TreeModelEvent var1) {
         TreePath var2 = var1.getTreePath();
         TreeModelEvent var3 = new TreeModelEvent(DefaultKongaTreeModel.this, var2);
         DefaultKongaTreeModel.this.listeners.call().treeStructureChanged(var3);
      }
   }
}
