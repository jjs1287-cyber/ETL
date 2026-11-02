package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public final class ResetGraphLayoutAction extends AbstractAction {
   private final OperationGraphController controller;

   public ResetGraphLayoutAction(OperationGraphController var1) {
      super(Strings.get("ResetGraphLayout.Name"));
      this.controller = var1;
      this.putValue("ShortDescription", Strings.get("ResetGraphLayout.ToolTip"));
      this.putValue("AcceleratorKey", ResetLayoutAction.SHORTCUT);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.controller.resetLayout();
   }
}
