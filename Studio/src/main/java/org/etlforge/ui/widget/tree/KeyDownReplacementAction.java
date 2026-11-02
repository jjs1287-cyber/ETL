package org.etlforge.ui.widget.tree;

import org.etlforge.ui.util.KeyUtils;

public abstract class KeyDownReplacementAction extends AbstractTreeReplacementAction {
   public KeyDownReplacementAction(KongaTree var1) {
      super(var1, KeyUtils.DOWN);
   }

   @Override
   protected final boolean isSpecialCase() {
      int var1 = this.getSelectedRow();
      int var2 = this.component.getRowCount();
      return var1 == var2 - 1;
   }
}
