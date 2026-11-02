package org.etlforge.integration.client.ui.interchange.component.tree;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.ui.util.KeyUtils;

final class ShowTreeFilterAction extends AbstractAction {
   private final SingleTreeContentViewer viewer;

   public ShowTreeFilterAction(SingleTreeContentViewer var1) {
      super("Filter");
      this.viewer = var1;
      this.putValue("AcceleratorKey", KeyUtils.getCtrlShiftDown(70));
      this.setEnabled(false);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.viewer.giveFocusToFilterField();
   }
}
