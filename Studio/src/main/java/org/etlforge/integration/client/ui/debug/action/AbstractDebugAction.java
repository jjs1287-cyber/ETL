package org.etlforge.integration.client.ui.debug.action;

import java.awt.EventQueue;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebuggingState;
import org.etlforge.integration.debug.client.event.DebugServiceEvent;
import org.etlforge.integration.debug.client.event.DebugServiceListener;
import org.etlforge.integration.debug.client.event.DebugSessionEvent;
import org.etlforge.integration.debug.client.event.DebugSessionListener;
import org.etlforge.system.Disposable;

public abstract class AbstractDebugAction extends AbstractApplicationAction implements Disposable {
   private final DebugService debugService;
   private final AbstractDebugAction.Listener listener;

   public AbstractDebugAction(String var1, DebugService var2) {
      super(var1, DebugActionDictionary.DICTIONARY);
      this.debugService = var2;
      this.listener = new AbstractDebugAction.Listener();
      var2.addListener(this.listener);
      this.handleNewState(this.getState());
   }

   protected abstract void handleNewState(DebuggingState var1);

   protected final DebugService getDebugService() {
      return this.debugService;
   }

   protected final DebuggingState getState() {
      return this.debugService.getState();
   }

   @Override
   public final void dispose() {
      this.debugService.removeListener(this.listener);
   }

   private class Listener implements DebugServiceListener, DebugSessionListener {
      private Listener() {
      }

      @Override
      public void debugSessionEnded(DebugServiceEvent var1) {
         this.setNewState(DebuggingState.IDLE);
      }

      @Override
      public void newDebugSessionStarted(DebugServiceEvent var1) {
         var1.getSession().addListener(this);
      }

      @Override
      public void sessionParametersChanged(DebugSessionEvent var1) {
      }

      @Override
      public void sessionStateChanged(DebugSessionEvent var1) {
         this.setNewState(var1.getState());
      }

      private void setNewState(final DebuggingState var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AbstractDebugAction.this.handleNewState(var1);
            }
         });
      }
   }
}
