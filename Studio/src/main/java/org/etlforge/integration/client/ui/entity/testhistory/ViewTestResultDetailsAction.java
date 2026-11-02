package org.etlforge.integration.client.ui.entity.testhistory;

import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.test.EntityTestResult;
import org.etlforge.ui.util.KeyUtils;

public final class ViewTestResultDetailsAction extends SingleTestResultAction {
   private IntegrationEntityLookup entityLookup;

   public ViewTestResultDetailsAction() {
      ActionDecorators.VIEW_DETAILS.decorate(this);
      this.putValue("ShortDescription", "View the details for the currently selected test result");
      this.putValue("AcceleratorKey", KeyUtils.ENTER);
      this.setEnabled(false);
   }

   @Override
   protected void actOn(EntityTestResult var1) {
      this.displayTestResultDetails(var1);
   }

   public void setEntityLookup(IntegrationEntityLookup var1) {
      this.entityLookup = var1;
      this.setEnabled(var1 != null);
   }

   private void displayTestResultDetails(EntityTestResult var1) {
      EntityTestResultDetailsDialog var2 = new EntityTestResultDetailsDialog(var1, this.entityLookup);
      var2.display();
   }
}
