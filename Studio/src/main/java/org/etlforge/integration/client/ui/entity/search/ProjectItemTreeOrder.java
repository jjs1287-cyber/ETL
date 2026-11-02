package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Predicate;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.etlforge.integration.client.entity.search.ProjectItemOrder;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTree;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.ui.widget.tree.BreadthFirstSearcher;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.TreeUtils;

public class ProjectItemTreeOrder<T extends IntegrationEntity> implements ProjectItemOrder<T> {
   private final IntegrationEntityTree tree;
   private final Class<T> cls;
   private KongaTreeNode currentNode;
   private final KongaTreeNode root;
   private final Predicate<KongaTreeNode> itemFinder;

   public ProjectItemTreeOrder(IntegrationEntityTree var1, EntityType var2, Class<T> var3) {
      this.tree = var1;
      this.cls = var3;
      this.root = this.getRoot(var2);
      this.itemFinder = new ProjectItemTreeOrder.ItemFinder(var2);
   }

   private KongaTreeNode getRoot(EntityType var1) {
      IntegrationEntityTreeModel var2 = this.tree.getModel();
      return (KongaTreeNode)BreadthFirstSearcher.findFirst(var2, new ProjectItemTreeOrder.RootFinder(var1));
   }

   @Override
   public T getFirst() {
      return this.next(this.root);
   }

   private T next(KongaTreeNode var1) {
      if (var1 == null) {
         return null;
      }

      for (KongaTreeNode var2 = (KongaTreeNode)TreeUtils.getNextNodeDown(this.tree.getModel(), var1);
         var2 != null;
         var2 = (KongaTreeNode)TreeUtils.getNextNodeDown(this.tree.getModel(), var2)
      ) {
         if (this.itemFinder.apply(var2)) {
            return this.storeAndReturnItem(var2);
         }
      }

      return null;
   }

   private T storeAndReturnItem(KongaTreeNode var1) {
      this.currentNode = var1;
      return this.cls.cast(var1.getUserObject());
   }

   @Override
   public T getLast() {
      if (this.root != null) {
         KongaTreeNode var1 = (KongaTreeNode)TreeUtils.getLastChild(this.tree.getModel(), this.root);
         if (this.itemFinder.apply(var1)) {
            return this.storeAndReturnItem(var1);
         }
      }

      return null;
   }

   @Override
   public T next() {
      KongaTreeNode var1 = this.getStartForDownwardTraversal();
      return this.next(var1);
   }

   private KongaTreeNode getStartForDownwardTraversal() {
      KongaTreeNode var1 = this.currentNode;
      if (var1 == null) {
         int[] var2 = this.tree.getSelectionRows();
         if (var2.length > 0) {
            int var3 = var2[var2.length - 1];
            TreePath var4 = this.tree.getPathForRow(var3);
            var1 = (KongaTreeNode)var4.getLastPathComponent();
         }
      }

      if (var1 == null) {
         var1 = this.root;
      }

      return var1;
   }

   @Override
   public T previous() {
      KongaTreeNode var1 = this.getStartForUpwardTraversal();
      return this.previous(var1);
   }

   private KongaTreeNode getStartForUpwardTraversal() {
      KongaTreeNode var1 = this.currentNode;
      if (var1 == null) {
         int[] var2 = this.tree.getSelectionRows();
         if (var2.length > 0) {
            int var3 = var2[0];
            TreePath var4 = this.tree.getPathForRow(var3);
            var1 = (KongaTreeNode)var4.getLastPathComponent();
         }
      }

      if (var1 == null) {
         var1 = this.root;
      }

      return var1;
   }

   private T previous(KongaTreeNode var1) {
      if (var1 == null) {
         return null;
      }

      for (KongaTreeNode var2 = (KongaTreeNode)TreeUtils.getNextNodeUp(this.tree.getModel(), var1);
         var2 != null;
         var2 = (KongaTreeNode)TreeUtils.getNextNodeUp(this.tree.getModel(), var2)
      ) {
         if (this.itemFinder.apply(var2)) {
            return this.storeAndReturnItem(var2);
         }
      }

      return null;
   }

   @Override
   public boolean isBefore(T var1, T var2) {
      if (this.root == null) {
         return false;
      }

      KongaTreeNode var3 = (KongaTreeNode)BreadthFirstSearcher.findFirst(this.tree.getModel(), this.root, new ProjectItemTreeOrder.NodeFinder(var2));
      if (var3 == null) {
         return false;
      }

      ProjectItemTreeOrder.NodeFinder var4 = new ProjectItemTreeOrder.NodeFinder(var1);

      for (TreeNode var5 = TreeUtils.getNextNodeUp(this.tree.getModel(), var3); var5 != null; var5 = TreeUtils.getNextNodeUp(this.tree.getModel(), var5)) {
         if (var4.apply(var5)) {
            return true;
         }
      }

      return false;
   }

   private static class ItemFinder implements Predicate<KongaTreeNode> {
      private final EntityType type;

      public ItemFinder(EntityType var1) {
         this.type = var1;
      }

      public boolean apply(KongaTreeNode var1) {
         Object var2 = var1.getUserObject();
         return var2 instanceof IntegrationEntity && ((IntegrationEntity)var2).getEntityType() == this.type;
      }
   }

   private static class NodeFinder implements Predicate<Object> {
      private final IntegrationEntity item;

      public NodeFinder(IntegrationEntity var1) {
         this.item = var1;
      }

      @Override
      public boolean apply(Object var1) {
         return var1 instanceof KongaTreeNode ? ((KongaTreeNode)var1).getUserObject() == this.item : false;
      }
   }

   private static class RootFinder implements Predicate<Object> {
      private final EntityType type;

      public RootFinder(EntityType var1) {
         this.type = var1;
      }

      @Override
      public boolean apply(Object var1) {
         if (var1 instanceof KongaTreeNode) {
            KongaTreeNode var2 = (KongaTreeNode)var1;
            if (var2.getUserObject() instanceof RootFolder) {
               RootFolder var3 = (RootFolder)var2.getUserObject();
               if (var3.getItemType() == this.type) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}
