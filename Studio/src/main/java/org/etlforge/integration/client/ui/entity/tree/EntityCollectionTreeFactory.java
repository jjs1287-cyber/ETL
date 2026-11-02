package org.etlforge.integration.client.ui.entity.tree;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class EntityCollectionTreeFactory {
   private final EntityCollectionTreeModelFactory modelFactory = new EntityCollectionTreeModelFactory();

   public EntityCollectionTreeFactory() {
      this.modelFactory.setNodeOrder(new EntityCollectionTreeFactory.NodeSorter());
   }

   public KongaTree newTree(List<? extends IntegrationEntity> var1) {
      return this.newTreeImpl(var1, new DefaultKongaTreeNode("root", true), false);
   }

   public KongaTree newTree(List<? extends IntegrationEntity> var1, KongaTreeNode var2) {
      return this.newTreeImpl(var1, var2, true);
   }

   public void setNodeOrder(Comparator<IntegrationEntityTreeNode> var1) {
      this.modelFactory.setNodeOrder(var1);
   }

   private KongaTree newTreeImpl(List<? extends IntegrationEntity> var1, KongaTreeNode var2, boolean var3) {
      KongaTreeModel var4 = this.modelFactory.newModel(var1, var2);
      KongaTree var5 = new KongaTree(var4);
      var5.setCellRenderer(new IntegrationEntityTreeRenderer());
      var5.expandAll();
      var5.setRootVisible(var3);
      return var5;
   }

   private static class NodeSorter implements Comparator<IntegrationEntityTreeNode>, Serializable {
      private final Comparator<IntegrationEntity> impl = new EntityTypeAndNameSorter();

      private NodeSorter() {
      }

      public int compare(IntegrationEntityTreeNode var1, IntegrationEntityTreeNode var2) {
         IntegrationEntity var3 = var1.getUserObject();
         IntegrationEntity var4 = var2.getUserObject();
         return this.impl.compare(var3, var4);
      }
   }
}
