package org.etlforge.integration.client.ui.entity.tree;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.tree.TopLevelCategoryTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeFilter2;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class MatchingItemsTreeFilter implements KongaTreeFilter2 {
   private final ImmutableSet<IntegrationEntity> items;

   public MatchingItemsTreeFilter(Collection<? extends IntegrationEntity> var1) {
      this.items = this.collectAllItems(var1);
   }

   private ImmutableSet<IntegrationEntity> collectAllItems(Collection<? extends IntegrationEntity> var1) {
      HashSet var2 = Sets.newHashSet(var1);
      this.addMissingParentFolders(var2);
      return ImmutableSet.copyOf(var2);
   }

   private void addMissingParentFolders(Set<IntegrationEntity> var1) {
      HashSet var2 = Sets.newHashSet();

      for (IntegrationEntity var4 : var1) {
         IntegrationEntity var5 = var4.getParent();

         while (var5 != null && var2.add(var5)) {
            var5 = var5.getParent();
         }
      }

      var1.addAll(var2);
   }

   @Override
   public boolean apply(KongaTreeNode var1) {
      if (var1 instanceof TopLevelCategoryTreeNode) {
         return this.applyToChildren(var1);
      }

      Object var2 = var1.getUserObject();
      return this.items.contains(var2);
   }

   private boolean applyToChildren(KongaTreeNode var1) {
      for (KongaTreeNode var3 : var1.children()) {
         if (this.apply(var3)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void hasBeenApplied(KongaTree var1) {
      var1.expandAllNonEmptyFolderNodes();
   }
}
