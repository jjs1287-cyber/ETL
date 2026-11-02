package org.etlforge.integration.client.ui.entity.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class EntityCollectionTreeModelFactory {
   private boolean useRootFolders;
   private Comparator<IntegrationEntityTreeNode> nodeOrder;

   public void setUseRootFolders(boolean var1) {
      this.useRootFolders = var1;
   }

   public void setNodeOrder(Comparator<IntegrationEntityTreeNode> var1) {
      this.nodeOrder = var1;
   }

   public KongaTreeModel newModel(Collection<? extends IntegrationEntity> var1) {
      return this.newModel(var1, new DefaultKongaTreeNode("root", true));
   }

   public KongaTreeModel newModel(Collection<? extends IntegrationEntity> var1, KongaTreeNode var2) {
      List var3 = this.getTopLevelNodes(Sets.newHashSet(var1));
      if (!this.useRootFolders && this.nodeOrder != null) {
         Collections.sort(var3, this.nodeOrder);
      }

      for (Object __etlItem41 : var3) {
         KongaTreeNode var5 = (KongaTreeNode) __etlItem41;
         var2.addChild(var5);
      }

      return new DefaultKongaTreeModel(var2);
   }

   private List<IntegrationEntityTreeNode> getTopLevelNodes(Set<IntegrationEntity> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (IntegrationEntity var4 : var1) {
         if (!var1.contains(var4.getParent())) {
            var2.add(this.createNodeForEntity(var4));
         }
      }

      return var2;
   }

   private IntegrationEntityTreeNode createNodeForEntity(IntegrationEntity var1) {
      return var1 instanceof Folder ? this.createNodeForFolder((Folder)var1) : new IntegrationEntityTreeNode(var1);
   }

   private IntegrationEntityTreeNode createNodeForFolder(Folder var1) {
      IntegrationEntityTreeNode var2 = new IntegrationEntityTreeNode(var1);

      for (IntegrationEntity var4 : var1.getAllChildren()) {
         IntegrationEntityTreeNode var5 = this.createNodeForEntity(var4);
         var2.addChild(var5);
      }

      return var2;
   }
}
