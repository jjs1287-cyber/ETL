package org.etlforge.ui.widget.tree;

import org.etlforge.ui.widget.tree.check.CheckBoxNode;

public final class LeafDisplayNameFilterCheck implements KongaTreeFilter2 {
   private final LeafDisplayNameFilter filter;

   public LeafDisplayNameFilterCheck(LeafDisplayNameFilter var1) {
      this.filter = var1;
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
         if (var1 instanceof CheckBoxNode) {
            CheckBoxNode var2 = (CheckBoxNode)var1;
            if (var2.isSelected()) {
               return true;
            }
         }

         return this.filter.applyCondition(var1);
      }
   }

   @Override
   public void hasBeenApplied(KongaTree var1) {
      this.filter.hasBeenApplied(var1);
   }
}
