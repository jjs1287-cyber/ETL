package org.etlforge.integration.client.ui.debug.action;

import java.awt.event.ActionEvent;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.DebuggingState;

public final class StopDebuggingAction extends AbstractDebugAction {
   public static final String ID = "action.debug.Stop";

   public StopDebuggingAction(DebugService var1) {
      super("action.debug.Stop", var1);
   }

   @Override
   protected void handleNewState(DebuggingState var1) {
      this.setEnabled(var1 != DebuggingState.IDLE);
   }

   @Override
   public void execute(ActionEvent var1) {
      synchronized (this.getDebugService()) {
         if (this.getState() != DebuggingState.IDLE) {
            this.stopDebugging();
            return;
         }
      }

      this.setEnabled(false);
   }

   private void stopDebugging() {
      DebugSession var1 = this.getDebugService().getCurrentSession();
      var1.getController().stop();
   }
}
