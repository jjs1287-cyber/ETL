package org.etlforge.ui.widget.tree;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;

public final class MemoizingKongaTreeFilter implements KongaTreeFilter2 {
   private final Map<KongaTreeNode, Boolean> cache;
   private final KongaTreeFilter filter;

   public MemoizingKongaTreeFilter(KongaTreeFilter var1) {
      Preconditions.checkNotNull(var1, "filter");
      this.filter = var1;
      this.cache = Maps.newIdentityHashMap();
   }

   @Override
   public boolean apply(KongaTreeNode var1) {
      Boolean var2 = this.cache.get(var1);
      if (var2 == null) {
         var2 = this.filter.apply(var1);
         this.cache.put(var1, var2);
      }

      return var2;
   }

   @Override
   public void hasBeenApplied(KongaTree var1) {
      if (this.filter instanceof KongaTreeFilter2) {
         ((KongaTreeFilter2)this.filter).hasBeenApplied(var1);
      }
   }

   public void forgetState() {
      this.cache.clear();
   }
}
