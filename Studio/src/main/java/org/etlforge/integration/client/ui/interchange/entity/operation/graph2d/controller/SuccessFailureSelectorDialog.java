package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.dialog.KongaDialog;

final class SuccessFailureSelectorDialog {
   private final AbstractOnSuccessFailureSelector<?> selector;

   public SuccessFailureSelectorDialog(AbstractOnSuccessFailureSelector<?> var1) {
      this.selector = var1;
   }

   public void open() {
      final KongaDialog var1 = new KongaDialog(ApplicationUi.getAppWin().getFrame(), this.selector.getTitle()) {
         @Override
         protected void onOk() {
            SuccessFailureSelectorDialog.this.selector.apply();
            super.onOk();
         }
      };
      this.selector.setCallback(new Receiver() {
         @Override
         public void handle(Object var1x) {
            var1.dispose();
         }
      });
      var1.standardLayout(this.selector, KongaDialog.OK_CANCEL);
      var1.setOkButtonAsDefault();
      var1.manageLocation(this.getClass());
      var1.setVisible(true);
   }
}
