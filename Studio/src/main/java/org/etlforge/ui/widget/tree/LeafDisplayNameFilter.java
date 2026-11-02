package org.etlforge.ui.widget.tree;

import com.google.common.base.Predicate;
import org.etlforge.string.StringMatcher;
import org.etlforge.string.StringMatcherPredicate;

public final class LeafDisplayNameFilter implements KongaTreeFilter2 {
   private final Predicate<String> condition;

   public LeafDisplayNameFilter(StringMatcher var1, String var2) {
      this.condition = new StringMatcherPredicate(var1, var2);
   }

   public LeafDisplayNameFilter(Predicate<String> var1) {
      this.condition = var1;
   }

   @Override
   public boolean apply(KongaTreeNode var1) {
      if (var1.getAllowsChildren()) {
         for (KongaTreeNode var3 : var1.children()) {
            if (this.apply(var3)) {
               return true;
            }
         }

         return false;
      } else {
         return this.applyCondition(var1);
      }
   }

   boolean applyCondition(KongaTreeNode var1) {
      String var2 = var1.getDisplayName();
      return this.condition.apply(var2);
   }

   @Override
   public void hasBeenApplied(KongaTree var1) {
      var1.expandAllNonEmptyFolderNodes();
   }
}
