package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;

public final class WsdlSelectorDialog {
   private final WsdlSelectorPanel ui;
   private KongaDialog dialog;
   private PropertyChangeListener okEnabler;

   public WsdlSelectorDialog(WsdlSelectorPanel var1) {
      this.ui = var1;
      this.okEnabler = new WsdlSelectorDialog.OkEnabler();
      var1.addChangeListener(this.okEnabler);
   }

   public boolean open() {
      this.dialog = new KongaDialog(UiUtils.getActiveWindow(), "Select a WSDL") {
         @Override
         protected void onDialogOpened() {
            super.onDialogOpened();
            WsdlSelectorDialog.this.ui.requestFocus();
         }

         @Override
         protected void onDialogClose() {
            WsdlSelectorDialog.this.ui.removeChangeListener(WsdlSelectorDialog.this.okEnabler);
            super.onDialogClose();
         }
      };
      this.dialog.standardLayout(this.ui, KongaDialog.OK_CANCEL);
      this.dialog.setOkButtonAsDefault();
      this.dialog.manageLocation();
      this.checkOkEnabled();
      ActionListener var1 = this.dialog.getOkActionListener();
      this.ui.addActionListener(var1);
      this.dialog.setVisible(true);
      boolean var2 = this.dialog.wasOkPressed();
      this.dialog = null;
      this.ui.removeActionListener(var1);
      this.ui.dispose();
      return var2;
   }

   private void checkOkEnabled() {
      this.dialog.getOKButton().setEnabled(this.ui.inputIsValid());
   }

   private final class OkEnabler implements PropertyChangeListener {
      private OkEnabler() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (WsdlSelectorDialog.this.dialog != null) {
            WsdlSelectorDialog.this.checkOkEnabled();
         }
      }
   }
}
