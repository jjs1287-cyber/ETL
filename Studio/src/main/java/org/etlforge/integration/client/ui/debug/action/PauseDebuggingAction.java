package org.etlforge.integration.client.ui.debug.action;

import java.awt.event.ActionEvent;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.DebuggingState;

public final class PauseDebuggingAction extends AbstractDebugAction {
   public static final String ID = "action.debug.Pause";

   public PauseDebuggingAction(DebugService var1) {
      super("action.debug.Pause", var1);
   }

   @Override
   protected void handleNewState(DebuggingState var1) {
      this.setEnabled(var1 == DebuggingState.RUNNING);
   }

   @Override
   public void execute(ActionEvent var1) {
      synchronized (this.getDebugService()) {
         if (this.getDebugService().getState() == DebuggingState.RUNNING) {
            this.pause();
            return;
         }
      }

      this.setEnabled(false);
   }

   private void pause() {
      DebugSession var1 = this.getDebugService().getCurrentSession();
      var1.getController().pause();
   }
}
