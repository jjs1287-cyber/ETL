package org.etlforge.integration.client.ui.entity.tree;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.MemoizingKongaTreeFilter;

public final class EntityTreeFilter implements KongaTreeFilter {
   private final EntityFilter entityFilter;

   public static KongaTreeFilter memoize(EntityFilter var0) {
      return new EntityTreeFilter(var0).memoize();
   }

   public EntityTreeFilter(EntityFilter var1) {
      this.entityFilter = var1;
   }

   @Override
   public boolean apply(KongaTreeNode var1) {
      return this.isMatch(var1) || this.hasMatchingChild(var1);
   }

   private boolean isMatch(KongaTreeNode var1) {
      Object var2 = var1.getUserObject();
      if (var2 instanceof IntegrationEntity) {
         IntegrationEntity var3 = (IntegrationEntity)var2;
         return this.entityFilter.apply(var3);
      } else {
         return false;
      }
   }

   private boolean hasMatchingChild(KongaTreeNode var1) {
      for (int var2 = 0; var2 < var1.getChildCount(); var2++) {
         if (this.apply(var1.getChildAt(var2))) {
            return true;
         }
      }

      return false;
   }

   public KongaTreeFilter memoize() {
      return new MemoizingKongaTreeFilter(this);
   }
}
