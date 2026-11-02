package org.etlforge.ui.widget.tree;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;

public final class KongaTreeCompositeFilter implements KongaTreeFilter {
   private final List<KongaTreeFilter> filters;

   public static KongaTreeFilter memoize(KongaTreeFilter... var0) {
      return new KongaTreeCompositeFilter(var0).memoize();
   }

   public KongaTreeCompositeFilter(Collection<? extends KongaTreeFilter> var1) {
      this.filters = Lists.newArrayList(var1);
      Preconditions.checkArgument(this.noNulls(), "null filter encountered");
   }

   public KongaTreeCompositeFilter(KongaTreeFilter... var1) {
      this.filters = Lists.newArrayList(var1);
      Preconditions.checkArgument(this.noNulls(), "null filter encountered");
   }

   private boolean noNulls() {
      for (KongaTreeFilter var2 : this.filters) {
         if (var2 == null) {
            return false;
         }
      }

      return true;
   }

   public void addFilter(KongaTreeFilter var1) {
      Preconditions.checkNotNull(var1, "filter");
      this.filters.add(var1);
   }

   @Override
   public boolean apply(KongaTreeNode var1) {
      for (KongaTreeFilter var3 : this.filters) {
         if (!var3.apply(var1)) {
            return false;
         }
      }

      return true;
   }

   public KongaTreeFilter memoize() {
      return new MemoizingKongaTreeFilter(this);
   }
}
