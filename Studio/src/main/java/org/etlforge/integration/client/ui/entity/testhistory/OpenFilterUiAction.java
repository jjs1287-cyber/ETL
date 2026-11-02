package org.etlforge.integration.client.ui.entity.testhistory;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.data.test.TestResultFilter;
import org.etlforge.system.callback.Receiver;

final class OpenFilterUiAction extends AbstractAction {
   private final Receiver<TestResultFilter> callback;
   private OutcomeFilterUi filterUi;

   public OpenFilterUiAction(Receiver<TestResultFilter> var1) {
      super("Filter...");
      this.callback = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            OpenFilterUiAction.this.openFilterUi();
         }
      });
   }

   private void openFilterUi() {
      if (this.filterUi == null) {
         this.filterUi = new OutcomeFilterUi();
      }

      TestResultFilter var1 = this.filterUi.openSelector();
      if (var1 != null) {
         this.callback.handle(var1);
      }
   }
}
