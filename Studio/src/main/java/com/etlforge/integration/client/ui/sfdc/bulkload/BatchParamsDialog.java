package com.etlforge.integration.client.ui.sfdc.bulkload;

import com.google.common.base.Optional;
import java.awt.Component;
import javax.swing.JComponent;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.salesforce.bulkload.BatchFileParams;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.InputPanelDialogMediator;

public final class BatchParamsDialog {
   private final BatchFileParams initialValue;

   public BatchParamsDialog(BatchFileParams var1) {
      this.initialValue = var1;
   }

   public BatchFileParams open(Optional<Component> var1) {
      BatchFileOptionsUi var2 = new BatchFileOptionsUi(this.initialValue).withoutBorder();
      var2.withoutBorder();
      KongaDialog var3 = this.createDialog(var1, var2);
      var3.setVisible(true);
      return var3.wasOkPressed() ? var2.getBatchParams() : this.initialValue;
   }

   private KongaDialog createDialog(Optional<Component> var1, BatchFileOptionsUi var2) {
      JComponent var3 = WizardStyleDialog.wrapUi(var2);
      WizardStyleDialog var4 = new WizardStyleDialog(ApplicationUi.getWindowAncestor(var1), "Batch Settings", var3);
      var4.setFocusedComponent(var2);
      var4.manageLocation(BatchParamsDialog.class);
      var4.setOkButtonAsDefault();
      var4.setResizable(false);
      this.installUndoRedo(var2, var4);
      return var4;
   }

   private void installUndoRedo(BatchFileOptionsUi var1, KongaDialog var2) {
      InputPanel var3 = new InputPanel();
      var1.addToParent(var3);
      var3.setSendUndoEvents(true);
      InputPanelDialogMediator.bind(var3, var2);
   }
}
