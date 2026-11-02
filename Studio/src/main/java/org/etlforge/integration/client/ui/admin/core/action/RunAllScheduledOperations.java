package org.etlforge.integration.client.ui.admin.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.admin.util.AllScheduledOperationsRunner;

public final class RunAllScheduledOperations extends AbstractApplicationAction {
   private final ApplicationWindow appWin;

   public RunAllScheduledOperations(ApplicationWindow var1) {
      super("admin.action.RunScheduledOperations", AdminActionDictionary.DICTIONARY);
      this.appWin = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RunAllScheduledOperations.this.submitRequest();
         }
      });
   }

   private void submitRequest() {
      AllScheduledOperationsRunner var1 = new AllScheduledOperationsRunner(this.appWin);
      var1.start();
   }
}
