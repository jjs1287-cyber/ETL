package org.etlforge.integration.client.ui.interchange.debug;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.ui.util.UiUtils;

public final class TestResultPageDisplayer implements TestResultDisplayer {
   private final IntegrationEntityLookup itemLookup;
   private final EntityPageDisplayer pageDisplayer;

   public TestResultPageDisplayer(IntegrationEntityLookup var1, EntityPageDisplayer var2) {
      this.itemLookup = var1;
      this.pageDisplayer = var2;
   }

   @Override
   public void showTestResult(final TestResult var1) {
      final IntegrationEntityId var2 = var1.getIdOfEntityToDisplay();
      final IntegrationEntity var3 = this.itemLookup.getEntity(var2);
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (var3 != null) {
               TestResultPageDisplayer.this.openEntity(var3, var1);
            } else {
               TestResultPageDisplayer.this.showMissingEntityError(var2);
            }
         }
      });
   }

   private void showMissingEntityError(IntegrationEntityId var1) {
      String var2 = var1.getEntityType().getDisplayName();
      String var3 = String.format(
         "Cannot display breakpoint location in the %s with ID %s because a %s with that ID does not exist in the project.", var2, var1, var2
      );
      Alert.error(var3, "Error");
   }

   private void openEntity(IntegrationEntity var1, TestResult var2) {
      Editor var3 = this.pageDisplayer.openPage(var1);
      if (!(var3 instanceof TestResultDisplayer)) {
         ErrorLog.log(
            this.getClass(),
            "The test result cannot be displayed.",
            new Exception("The UI is not configured correctly to display the result of the test. This is a bug.")
         );
      } else {
         ((TestResultDisplayer)var3).showTestResult(var2);
      }
   }
}
