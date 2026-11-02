package org.etlforge.ui.widget.tree;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

public final class ExpansionFilter {
   private final Predicate<? super KongaTreeNode> condition;
   private boolean alwaysExpandRoot;

   public ExpansionFilter(Predicate<? super KongaTreeNode> var1) {
      Preconditions.checkNotNull(var1, "condition");
      this.condition = var1;
      this.alwaysExpandRoot = true;
   }

   public void setAlwaysExpandRoot(boolean var1) {
      this.alwaysExpandRoot = var1;
   }

   public void applyTo(KongaTree var1) {
      var1.collapseAll();
      KongaTreeNode var2 = var1.getModel().getRoot();
      this.processNode(var1, var2, true);
   }

   private void processNode(KongaTree var1, KongaTreeNode var2, boolean var3) {
      if (this.shouldExpand(var2, var3)) {
         var1.makeNodeExpanded(var2);
         KongaTreeModel var4 = var1.getModel();
         int var5 = var4.getChildCount(var2);

         for (int var6 = 0; var6 < var5; var6++) {
            KongaTreeNode var7 = (KongaTreeNode)var4.getChild(var2, var6);
            this.processNode(var1, var7, false);
         }
      }
   }

   private boolean shouldExpand(KongaTreeNode var1, boolean var2) {
      return var2 && this.alwaysExpandRoot || this.condition.apply(var1);
   }
}
