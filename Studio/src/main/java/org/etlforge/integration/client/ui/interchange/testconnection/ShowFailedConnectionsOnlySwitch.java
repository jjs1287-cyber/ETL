package org.etlforge.integration.client.ui.interchange.testconnection;

import com.google.common.base.Predicate;
import org.etlforge.integration.data.test.EntityTestResult;
import org.etlforge.integration.data.test.TestResult;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class ShowFailedConnectionsOnlySwitch implements UiProvider {
   private final TestAllConnectionsResultTable resultTable;
   private final Predicate<EntityTestResult> filter = new Predicate<EntityTestResult>() {
      public boolean apply(EntityTestResult var1) {
         return var1.getOutcome() == TestResult.Outcome.ERROR;
      }
   };
   private final KongaCheckBox switchBox;

   public ShowFailedConnectionsOnlySwitch(TestAllConnectionsResultTable var1) {
      this.resultTable = var1;
      this.switchBox = this.createSwitchBox();
   }

   private KongaCheckBox createSwitchBox() {
      KongaCheckBox var1 = new KongaCheckBox("Show only failed connections");
      var1.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            ShowFailedConnectionsOnlySwitch.this.resultTable.setFilter(var1 ? ShowFailedConnectionsOnlySwitch.this.filter : null);
         }
      });
      return var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.switchBox);
   }

   @Override
   public void requestFocus() {
      this.switchBox.requestFocusInWindow();
   }
}
