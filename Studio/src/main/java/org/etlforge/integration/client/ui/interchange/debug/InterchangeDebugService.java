package org.etlforge.integration.client.ui.interchange.debug;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginService;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebuggingState;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.debug.client.event.DebugServiceEvent;
import org.etlforge.integration.debug.client.event.DebugServiceListener;
import org.etlforge.integration.debug.client.event.DebugSessionEvent;
import org.etlforge.integration.debug.client.event.DebugSessionListener;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.UiUtils;

public final class InterchangeDebugService implements Disposable {
   private final DebugService debugService;
   private final ServerManager serverManager;
   private final TestResultDispatcher resultDispatcher;
   private final InterchangeDebugService.Listener listener = new InterchangeDebugService.Listener();
   private LogoutWhileDebuggingHandler logoutHandler;

   public InterchangeDebugService(DebugService var1, ServerManager var2, TestResultDispatcher var3) {
      this.debugService = var1;
      this.serverManager = var2;
      this.resultDispatcher = var3;
      var1.addListener(this.listener);
   }

   @Override
   public void dispose() {
      this.debugService.removeListener(this.listener);
   }

   public DebugService getPlatformDebugService() {
      return this.debugService;
   }

   public void addTestResultDisplayer(final IntegrationEntityId var1, final TestResultDisplayer var2) {
      Preconditions.checkNotNull(var1, "entityId");
      Preconditions.checkNotNull(var2, "handler");
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            InterchangeDebugService.this.resultDispatcher.addTestResultDisplayer(var1, var2);
         }
      });
   }

   public void removeTestResultDisplayer(final IntegrationEntityId var1, final TestResultDisplayer var2) {
      Preconditions.checkNotNull(var1, "entityId");
      Preconditions.checkNotNull(var2, "handler");
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            InterchangeDebugService.this.resultDispatcher.removeTestResultDisplayer(var1, var2);
         }
      });
   }

   private class Listener implements DebugServiceListener, DebugSessionListener {
      private Listener() {
      }

      @Override
      public void sessionParametersChanged(DebugSessionEvent var1) {
      }

      @Override
      public void sessionStateChanged(DebugSessionEvent var1) {
         DebuggingState var2 = var1.getState();
         if (var2 == DebuggingState.PAUSED || var2 == DebuggingState.DONE) {
            this.displayTestResult(var1);
         }
      }

      @Override
      public synchronized void debugSessionEnded(DebugServiceEvent var1) {
         LoginService var2 = InterchangeDebugService.this.serverManager.getLoginService();
         var2.removeLoginListener(InterchangeDebugService.this.logoutHandler);
         InterchangeDebugService.this.logoutHandler = null;
      }

      @Override
      public synchronized void newDebugSessionStarted(DebugServiceEvent var1) {
         InterchangeDebugService.this.logoutHandler = new LogoutWhileDebuggingHandler(var1.getSource(), var1.getSession());
         LoginService var2 = InterchangeDebugService.this.serverManager.getLoginService();
         var2.addLoginListener(InterchangeDebugService.this.logoutHandler);
         var1.getSession().addListener(this);
      }

      private void displayTestResult(DebugSessionEvent var1) {
         final TestResult var2 = var1.getSource().getResult();
         if (var2 != null) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  InterchangeDebugService.this.resultDispatcher.dispatch(var2);
               }
            });
         }
      }
   }
}
