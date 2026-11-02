package org.etlforge.integration.client.ui.de.global.tree;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.tree.KongaTree;

final class ArrowDownAction extends AbstractAction {
   private final KongaTree tree;

   public ArrowDownAction(KongaTree var1) {
      this.tree = var1;
      this.putValue("AcceleratorKey", KeyUtils.DOWN);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.tree.requestFocusInWindow();
      this.tree.selectNextRow();
   }
}
