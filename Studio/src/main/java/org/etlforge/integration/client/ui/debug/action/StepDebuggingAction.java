package org.etlforge.integration.client.ui.debug.action;

import java.awt.event.ActionEvent;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.DebuggingState;

public final class StepDebuggingAction extends AbstractDebugAction {
   public static final String ID = "action.debug.Step";

   public StepDebuggingAction(DebugService var1) {
      super("action.debug.Step", var1);
   }

   @Override
   protected void handleNewState(DebuggingState var1) {
      this.setEnabled(var1 == DebuggingState.PAUSED);
   }

   @Override
   public void execute(ActionEvent var1) {
      synchronized (this.getDebugService()) {
         if (this.getDebugService().getState() == DebuggingState.PAUSED) {
            this.step();
            return;
         }
      }

      this.setEnabled(false);
   }

   private void step() {
      DebugSession var1 = this.getDebugService().getCurrentSession();
      var1.getController().step(var1.getParams());
   }
}
