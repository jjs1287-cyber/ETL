package org.etlforge.integration.client.ui.entity.tree;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.tree.TreePath;
import org.etlforge.collections.IterableEnumeration;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public class IntegrationEntityTreeModel extends DefaultKongaTreeModel {
   private final IntegrationEntityTreeNodeCreator nodeCreator;
   private final ImmutableSet<EntityType> types;

   public IntegrationEntityTreeModel(String var1) {
      this(new DefaultKongaTreeNode(var1, false));
   }

   public IntegrationEntityTreeModel(KongaTreeNode var1) {
      this(var1, EntityType.allTypes());
   }

   public IntegrationEntityTreeModel(KongaTreeNode var1, Iterable<EntityType> var2) {
      super(var1);
      this.types = ImmutableSet.copyOf(var2);
      this.nodeCreator = new IntegrationEntityTreeNodeCreator(this);
   }

   void invalidate() {
      this.invalidate(this.getRoot());
   }

   private void invalidate(KongaTreeNode var1) {
      this.nodeChanged(var1);

      for (KongaTreeNode var3 : var1.children()) {
         this.invalidate(var3);
      }
   }

   public List<IntegrationEntityTreeNode> insertIntegrationEntities(List<IntegrationEntity> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (IntegrationEntity var4 : var1) {
         if (this.isOfInterest(var4)) {
            IntegrationEntityTreeNode var5 = this.nodeCreator.createNode(var4);
            IntegrationEntityTreeNode var6 = this.nodeCreator.getParentNode();
            if (var6 != null) {
               super.insertNode(var5, var6);
               var2.add(var5);
            }
         }
      }

      return var2;
   }

   private boolean isOfInterest(IntegrationEntity var1) {
      EntityType var2 = var1.getEntityType();
      if (this.types.contains(var2)) {
         return true;
      } else {
         return var2 == EntityType.Folder ? this.types.contains(((Folder)var1).getItemType()) : false;
      }
   }

   public void removeIntegrationEntities(List<IntegrationEntity> var1) {
      for (KongaTreeNode var4 : this.findNodesToRemove(var1)) {
         super.removeNode(var4);
      }
   }

   private List<KongaTreeNode> findNodesToRemove(List<IntegrationEntity> var1) {
      HashSet var2 = Sets.newHashSet(Iterables.filter(var1, new Predicate<IntegrationEntity>() {
         public boolean apply(IntegrationEntity var1) {
            return IntegrationEntityTreeModel.this.isOfInterest(var1);
         }
      }));
      if (var2.isEmpty()) {
         return Collections.emptyList();
      }

      ArrayList var3 = Lists.newArrayList();

      for (KongaTreeNode var5 : this.getRoot().depthFirstEnumeration()) {
         if (var2.contains(var5.getUserObject())) {
            var3.add(var5);
            var2.remove(var5.getUserObject());
            if (var2.isEmpty()) {
               break;
            }
         }
      }

      return var3;
   }

   public boolean moveIntegrationEntities(List<IntegrationEntity> var1) {
      boolean var2 = false;
      Map var3 = this.buildNodeMap();

      for (IntegrationEntity var5 : var1) {
         IntegrationEntityTreeNode var6 = (IntegrationEntityTreeNode)var3.get(var5);
         if (var6 != null) {
            KongaTreeNode var7 = var6.getParent();
            if (var7 instanceof IntegrationEntityTreeNode && ((IntegrationEntityTreeNode)var7).getUserObject() != var5.getParent()) {
               var6.removeFromParent();
               IntegrationEntityTreeNode var8 = (IntegrationEntityTreeNode)var3.get(var5.getParent());
               if (var8 != null) {
                  var8.add(var6);
               }

               var2 = true;
            }
         }
      }

      if (var2) {
         this.fireTreeStructureChanged(this, new TreePath(this.getRoot()));
      }

      return var2;
   }

   private Map<IntegrationEntity, IntegrationEntityTreeNode> buildNodeMap() {
      IdentityHashMap var1 = Maps.newIdentityHashMap();
      IterableEnumeration var2 = this.getRoot().breadthFirstEnumeration();

      while (var2.hasMoreElements()) {
         KongaTreeNode var3 = (KongaTreeNode)var2.nextElement();
         if (var3 instanceof IntegrationEntityTreeNode) {
            IntegrationEntityTreeNode var4 = (IntegrationEntityTreeNode)var3;
            var1.put(var4.getUserObject(), var4);
         }
      }

      return var1;
   }

   public final KongaTreeNode findNodeForObject(IntegrationEntity var1) {
      IterableEnumeration var2 = this.getRoot().breadthFirstEnumeration();

      while (var2.hasMoreElements()) {
         KongaTreeNode var3 = (KongaTreeNode)var2.nextElement();
         if (var3.getUserObject() == var1) {
            return var3;
         }
      }

      return null;
   }

   @Override
   public void sortChildren(KongaTreeNode var1) {
      ArrayList var2 = Lists.newArrayList();
      this.sortChildrenImpl(var1, var2);

      for (Object __etlItem172 : var2) {
         KongaTreeNode var4 = (KongaTreeNode) __etlItem172;
         this.nodeStructureChanged(var4);
      }
   }

   private void sortChildrenImpl(KongaTreeNode var1, List<KongaTreeNode> var2) {
      if (var1.sortChildren()) {
         var2.add(var1);
      }

      for (int var3 = 0; var3 < var1.getChildCount(); var3++) {
         KongaTreeNode var4 = var1.getChildAt(var3);
         this.sortChildrenImpl(var4, var2);
      }
   }
}
