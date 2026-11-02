package org.etlforge.integration.client.ui.debug.action;

import java.awt.event.ActionEvent;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.DebuggingState;

public final class ResumeDebuggingAction extends AbstractDebugAction {
   public static final String ID = "action.debug.Resume";

   public ResumeDebuggingAction(DebugService var1) {
      super("action.debug.Resume", var1);
   }

   @Override
   protected void handleNewState(DebuggingState var1) {
      this.setEnabled(var1 == DebuggingState.PAUSED);
   }

   @Override
   public void execute(ActionEvent var1) {
      synchronized (this.getDebugService()) {
         if (this.getState() == DebuggingState.PAUSED) {
            this.resume();
            return;
         }
      }

      this.setEnabled(false);
   }

   private void resume() {
      DebugSession var1 = this.getDebugService().getCurrentSession();
      var1.getController().resume(var1.getParams());
   }
}
