package org.etlforge.ui.widget.tree;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.util.KeyUtils;

public final class ExpandEverythingUnderSelectedNodeAction extends AbstractAction {
   private final KongaTree tree;

   public ExpandEverythingUnderSelectedNodeAction(KongaTree var1) {
      this.tree = var1;
      this.putValue("AcceleratorKey", KeyUtils.EXPAND_EVERYTHING);
   }

   public void installShortcut() {
      ActionKeyBinding.install(this, this.tree);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (this.tree.getSelectionCount() == 1) {
         KongaTreeNode var2 = this.tree.getSelectedNodes().get(0);
         this.tree.expandAllUnder(var2);
      }
   }
}
