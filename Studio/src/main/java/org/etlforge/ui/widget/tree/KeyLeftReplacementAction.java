package org.etlforge.ui.widget.tree;

import java.util.List;
import javax.swing.tree.TreePath;
import org.etlforge.ui.util.KeyUtils;

public abstract class KeyLeftReplacementAction extends AbstractTreeReplacementAction {
   public KeyLeftReplacementAction(KongaTree var1) {
      super(var1, KeyUtils.LEFT);
   }

   @Override
   protected final boolean isSpecialCase() {
      List var1 = this.component.getSelectedNodes();
      if (var1.size() == 1) {
         KongaTreeNode var2 = (KongaTreeNode)var1.get(0);
         TreePath var3 = this.component.getPath(var2);
         if (this.component.isCollapsed(var3)) {
            if (this.component.isRootVisible()) {
               KongaTreeNode var5 = this.component.getModel().getRoot();
               return var2 == var5;
            }

            KongaTreeNode var4 = var2.getParent();
            return var4 == this.component.getModel().getRoot();
         }
      }

      return false;
   }
}
