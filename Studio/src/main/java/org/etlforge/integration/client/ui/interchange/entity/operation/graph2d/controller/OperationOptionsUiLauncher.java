package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.page.OperationPage;
import org.etlforge.integration.client.ui.operation.options.OperationOptionsDialog;
import org.etlforge.integration.client.ui.operation.options.OperationOptionsOverlay;
import org.etlforge.integration.client.ui.operation.options.OperationOptionsPanel;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.overlay.OverlayContainer;

final class OperationOptionsUiLauncher {
   private final ApplicationWindow appWin;
   private final OperationGraphController controller;

   public OperationOptionsUiLauncher(ApplicationWindow var1, OperationGraphController var2) {
      this.appWin = var1;
      this.controller = var2;
   }

   public void showOptions(OperationWrapperNode var1) {
      OverlayContainer var2 = this.getOverlayContainer();
      if (var2 != null) {
         this.showInOverlay(var2, var1);
      } else {
         this.showInDialog(var1);
      }
   }

   private OverlayContainer getOverlayContainer() {
      Operation var1 = this.controller.getMainOperation();
      OperationPage var2 = (OperationPage)UiCompanion.of(var1).findOpenPage();
      return var2.getOverlayContainer();
   }

   private void showInOverlay(OverlayContainer var1, final OperationWrapperNode var2) {
      Receiver var3 = new Receiver<OperationOptions>() {
         public void handle(OperationOptions var1) {
            if (var1 != null) {
               OperationOptionsUiLauncher.this.controller.setOperationOptions(var2, var1);
            }

            OperationOptionsUiLauncher.this.controller.getGraph().requestFocus();
         }
      };
      OperationOptionsPanel var4 = this.createPanel(var2);
      OperationOptionsOverlay var5 = new OperationOptionsOverlay(var4, var3);
      var5.show(var1);
   }

   private OperationOptionsPanel createPanel(OperationWrapperNode var1) {
      return new OperationOptionsPanel(var1.getDataObject(), this.controller.getExplorerSupport());
   }

   private void showInDialog(OperationWrapperNode var1) {
      OperationOptionsPanel var2 = this.createPanel(var1);
      OperationOptionsDialog var3 = new OperationOptionsDialog(this.appWin.getFrame(), var2);
      OperationOptions var4 = var3.show();
      if (var4 != null) {
         this.controller.setOperationOptions(var1, var4);
      }
   }
}
