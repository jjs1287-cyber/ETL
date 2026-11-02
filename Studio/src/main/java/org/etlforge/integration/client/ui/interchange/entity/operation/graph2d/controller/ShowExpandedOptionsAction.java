package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ExpandedOptionsDialog;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.ExpandedOptionsOverlay;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.page.OperationPage;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.ui.widget.overlay.OverlayContainer;

public final class ShowExpandedOptionsAction extends AbstractAction {
   private static final boolean SUPPORTS_OVERLAY = false;
   private final OperationGraphController controller;
   private final OperationWrapperNode opNode;

   public ShowExpandedOptionsAction(OperationGraphController var1, OperationWrapperNode var2) {
      super("Expanded Options...");
      this.controller = var1;
      this.opNode = var2;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      OverlayContainer var2 = this.getOverlayContainer();
      if (var2 != null) {
      }

      this.showInDialog();
   }

   private OverlayContainer getOverlayContainer() {
      Operation var1 = this.controller.getMainOperation();
      OperationPage var2 = (OperationPage)UiCompanion.of(var1).findOpenPage();
      return var2.getOverlayContainer();
   }


   private void showInDialog() {
      ExpandedOptionsDialog var1 = new ExpandedOptionsDialog(this.controller.getGraph().getWindowOwner(), this.controller, this.opNode);
      var1.show();
   }
}
