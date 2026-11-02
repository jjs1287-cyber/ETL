package org.etlforge.ui.widget.tree;

import com.google.common.base.Function;
import org.apache.commons.lang.StringUtils;
import org.etlforge.string.StringMatcher;
import org.etlforge.string.WildcardStringMatcher;

public final class LeafDisplayNameFilterFunction implements Function<String, KongaTreeFilter> {
   private final StringMatcher matcher;
   private final boolean memoize;

   public LeafDisplayNameFilterFunction() {
      this(new WildcardStringMatcher(false));
   }

   public LeafDisplayNameFilterFunction(StringMatcher var1) {
      this(var1, false);
   }

   public LeafDisplayNameFilterFunction(StringMatcher var1, boolean var2) {
      this.matcher = var1;
      this.memoize = true;
   }

   public KongaTreeFilter apply(String var1) {
      LeafDisplayNameFilter var2 = this.newLeafDisplayNameFilter(var1);
      return this.applyFiler(var1, var2);
   }

   LeafDisplayNameFilter newLeafDisplayNameFilter(String var1) {
      return new LeafDisplayNameFilter(this.matcher, var1);
   }

   KongaTreeFilter applyFiler(String var1, KongaTreeFilter var2) {
      if (StringUtils.isBlank(var1)) {
         return null;
      } else {
         return this.memoize ? new MemoizingKongaTreeFilter(var2) : var2;
      }
   }
}
