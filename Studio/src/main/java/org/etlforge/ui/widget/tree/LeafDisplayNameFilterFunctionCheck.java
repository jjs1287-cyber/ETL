package org.etlforge.ui.widget.tree;

import com.google.common.base.Function;

public final class LeafDisplayNameFilterFunctionCheck implements Function<String, KongaTreeFilter> {
   private final LeafDisplayNameFilterFunction filterFunction;

   public LeafDisplayNameFilterFunctionCheck(LeafDisplayNameFilterFunction var1) {
      this.filterFunction = var1;
   }

   public KongaTreeFilter apply(String var1) {
      LeafDisplayNameFilter var2 = this.filterFunction.newLeafDisplayNameFilter(var1);
      return this.filterFunction.applyFiler(var1, new LeafDisplayNameFilterCheck(var2));
   }
}
