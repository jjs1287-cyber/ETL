package org.etlforge.integration.client.ui.interchange.entity.webservice.xsd;

import java.awt.Component;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.LayeredKongaDialog;
import org.etlforge.ui.property.AbstractBinding;

public final class SoapSchemaDialog {
   private final SoapSchemaModel model;
   private final SoapSchemaController controller;

   public SoapSchemaDialog(SoapSchemaModel var1, SoapSchemaController var2) {
      this.model = var1;
      this.controller = var2;
   }

   public void open(Component var1) {
      SoapSchemaUi var2 = new SoapSchemaUi(this.model, this.controller);
      LayeredKongaDialog var3 = this.createDialog(var1, var2);
      SoapSchemaDialog.DoneBinding var4 = new SoapSchemaDialog.DoneBinding(var3);
      var3.setVisible(true);
      this.controller.cancel();
      var4.dispose();
      var2.dispose();
   }

   private LayeredKongaDialog createDialog(Component var1, SoapSchemaUi var2) {
      LayeredKongaDialog var3 = new LayeredKongaDialog(SwingUtilities.getWindowAncestor(var1), "Generate XSDs");
      var2.setWaitService(var3);
      var2.setCancelButton(var3.createCancelButton());
      var3.getDefaultLayout().center(var2);
      var3.getRootPane().setDefaultButton(var2.getGenerateButton());
      var3.setResizable(false);
      var3.pack();
      var3.setLocationRelativeTo(null);
      return var3;
   }

   private class DoneBinding extends AbstractBinding<Boolean> {
      private final KongaDialog dialog;

      public DoneBinding(KongaDialog var2) {
         super(SoapSchemaDialog.this.model, "done");
         this.dialog = var2;
      }

      protected void updateUiWithValue(Boolean var1) {
         if (var1) {
            this.dialog.dispose();
            NonBlockingAlert.message(this.dialog.getOwner(), new ResultMessage(SoapSchemaDialog.this.model), "Done", Alert.Type.INFORMATION);
         }
      }
   }
}
