package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

final class OpenSuccessFailureSelectorDialogAction extends AbstractAction {
   private final AbstractOnSuccessFailureSelector<?> selector;

   public OpenSuccessFailureSelectorDialogAction(AbstractOnSuccessFailureSelector<?> var1) {
      super("&Select Existing...");
      this.putValue("ShortDescription", var1.getTitle());
      this.selector = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.openDialog();
   }

   private void openDialog() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SuccessFailureSelectorDialog var1 = new SuccessFailureSelectorDialog(OpenSuccessFailureSelectorDialogAction.this.selector);
            var1.open();
         }
      });
   }
}
