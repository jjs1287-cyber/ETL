package org.etlforge.ui.widget.tree;

import javax.swing.KeyStroke;
import org.etlforge.ui.widget.AbstractComponentReplacementAction;

public abstract class AbstractTreeReplacementAction extends AbstractComponentReplacementAction<KongaTree> {
   public AbstractTreeReplacementAction(KongaTree var1, KeyStroke var2) {
      super(var1, var2);
   }

   protected final int getSelectedRow() {
      int[] var1 = this.component.getSelectionRows();
      return var1 != null && var1.length == 1 ? var1[0] : -1;
   }
}
