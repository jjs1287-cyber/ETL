package org.etlforge.ui.widget.tree;

import org.etlforge.ui.util.KeyUtils;

public abstract class KeyUpReplacementAction extends AbstractTreeReplacementAction {
   public KeyUpReplacementAction(KongaTree var1) {
      super(var1, KeyUtils.UP);
   }

   @Override
   protected final boolean isSpecialCase() {
      int var1 = this.getSelectedRow();
      return var1 == 0;
   }
}
