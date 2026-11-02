package org.etlforge.ui.widget.tree;

import com.google.common.base.Predicate;

public interface KongaTreeFilter extends Predicate<KongaTreeNode> {
   KongaTreeFilter NONE = new KongaTreeFilter() {
      @Override
      public boolean apply(KongaTreeNode var1) {
         return false;
      }
   };

   boolean apply(KongaTreeNode var1);
}
