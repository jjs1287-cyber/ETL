package org.etlforge.integration.client.ui.interchange.entity.operation;

import javax.swing.JComponent;
import javax.swing.JFrame;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.ui.dialog.KongaDialog;

final class NewOperationCallback extends UiEntityFactoryCallback.Adapter {
   @Override
   public Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
      {
         OperationActivitySelector var3 = new OperationActivitySelector();

         WizardStyleDialog var4 = this.createDialog(var3);
         var4.setVisible(true);
         if (var4.wasCancelled()) {
            return Result.cancel();
         }

         OperationType var5 = var3.getSelection();
         this.configureOperation(var1, var5);
      }

      return Result.continueProcess(var2);
   }

   private WizardStyleDialog createDialog(OperationActivitySelector var1) {
      JFrame var2 = ApplicationUi.getAppWinFrame();
      JComponent var3 = WizardStyleDialog.wrapUi(var1);
      WizardStyleDialog var4 = new WizardStyleDialog(var2, "New Operation", var3, KongaDialog.CONTINUE_CANCEL, true) {
         @Override
         protected String getCaptionTitle() { return null; }
      };
      var4.setFocusedComponent(var1);
      var4.setResizable(false);
      var4.manageLocation(this.getClass());
      var4.setContinueButtonAsDefault();
      return var4;
   }

   private void configureOperation(IntegrationEntity var1, OperationType var2) {
      Operation var3 = (Operation)var1;
      var3.setOperationType(var2);
      OperationPipeline var4 = var2.createNewTemplate();
      var3.setPipeline(var4);
   }
}



