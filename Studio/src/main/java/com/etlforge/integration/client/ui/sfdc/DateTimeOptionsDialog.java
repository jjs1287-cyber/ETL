package com.etlforge.integration.client.ui.sfdc;

import com.google.common.base.Optional;
import java.awt.Component;
import javax.swing.JComponent;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.ui.dialog.KongaDialog;

public final class DateTimeOptionsDialog {
   private final Optional<DateConversionParams> initialValue;

   public DateTimeOptionsDialog(Optional<DateConversionParams> var1) {
      this.initialValue = var1;
   }

   public Optional<DateConversionParams> open(Optional<Component> var1) {
      DateTimeOptionsUi var2 = new DateTimeOptionsUi(this.initialValue).withoutBorder();
      KongaDialog var3 = this.createDialog(var1, var2);
      var3.setVisible(true);
      return var3.wasOkPressed() ? var2.getConversionParams() : this.initialValue;
   }

   private KongaDialog createDialog(Optional<Component> var1, DateTimeOptionsUi var2) {
      JComponent var3 = WizardStyleDialog.wrapUi(var2);
      WizardStyleDialog var4 = new WizardStyleDialog(ApplicationUi.getWindowAncestor(var1), "Date Conversion Settings", var3);
      var4.setFocusedComponent(var2);
      var4.manageLocation(DateTimeOptionsDialog.class);
      var4.setOkButtonAsDefault();
      var4.setResizable(false);
      this.installUndoRedo(var2, var4);
      return var4;
   }

   private void installUndoRedo(DateTimeOptionsUi var1, KongaDialog var2) {
   }
}
