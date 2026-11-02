package org.etlforge.integration.client.ui.entity.testhistory;

import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.test.EntityTestResult;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;

final class EntityTestResultDetailsDialog {
   private final KongaDialog dialog = this.createEmptyDialog();

   public EntityTestResultDetailsDialog(EntityTestResult var1, IntegrationEntityLookup var2) {
      EntityTestResultDetailsViewer var3 = new EntityTestResultDetailsViewer(var1, var2);
      this.layoutDialog(this.dialog, var3);
   }

   public void display() {
      if (!this.dialog.isVisible()) {
         this.dialog.pack();
         this.dialog.manageLocation(EntityTestResultDetailsDialog.class);
         this.dialog.setCloseButtonAsDefault();
         this.dialog.setLocationRelativeTo(this.dialog.getOwner());
         this.dialog.setVisible(true);
      }
   }

   private KongaDialog createEmptyDialog() {
      return new KongaDialog(UiUtils.getActiveWindow(), "Test Result Details");
   }

   private void layoutDialog(KongaDialog var1, UiProvider var2) {
      var1.standardLayout(var2, KongaDialog.CLOSE_ONLY);
   }
}
