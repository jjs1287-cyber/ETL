package org.etlforge.integration.client.ui.debug;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.debug.client.DebugController;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.DebuggingException;
import org.etlforge.integration.debug.client.SessionRequester;
import org.etlforge.integration.debug.client.TestInProgressStrategy;
import org.etlforge.system.callback.CallbackResultAdapter;

public final class InteractiveTestInProgressStrategy implements TestInProgressStrategy {
   private final IntegrationEntityLookup entityLookup;

   public InteractiveTestInProgressStrategy(IntegrationEntityLookup var1) {
      this.entityLookup = var1;
   }

   @Override
   public void handleNewTestRequest(DebugService var1, String var2, IntegrationEntityId var3, DebugController var4, SessionRequester var5) throws DebuggingException {
      final InteractiveTestInProgressStrategy.Worker var6 = new InteractiveTestInProgressStrategy.Worker(var1, var2, var3, var4, var5, this.entityLookup);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var6.openUiIfSessionStillActive();
         }
      });
   }

   private static class Worker {
      private final DebugService service;
      private final String testProviderId;
      private final IntegrationEntityId debugeeId;
      private final DebugController controller;
      private final SessionRequester requester;
      private final IntegrationEntityLookup entityLookup;

      public Worker(DebugService var1, String var2, IntegrationEntityId var3, DebugController var4, SessionRequester var5, IntegrationEntityLookup var6) {
         this.service = var1;
         this.testProviderId = var2;
         this.debugeeId = var3;
         this.controller = var4;
         this.requester = var5;
         this.entityLookup = var6;
      }

      public void openUiIfSessionStillActive() {
         synchronized (this.service) {
            DebugSession var2 = this.service.getCurrentSession();
            if (var2 == null) {
               this.requestNewSession();
            } else {
               TestInProgressMessage var3 = new TestInProgressMessage(
                  var2,
                  this.entityLookup,
                  new InteractiveTestInProgressStrategy.Worker.ContinueHandler(),
                  new InteractiveTestInProgressStrategy.Worker.StopHandler()
               );
               var3.display();
            }
         }
      }

      private void requestNewSession() {
         try {
            this.service.startNewDebuggingSession(this.testProviderId, this.debugeeId, this.controller, this.requester);
         } catch (DebuggingException var2) {
            ErrorLog.attention(this.getClass(), null, var2);
            this.requester.requestDenied();
         }
      }

      private class ContinueHandler implements ActionListener {
         private ContinueHandler() {
         }

         @Override
         public void actionPerformed(ActionEvent var1) {
            synchronized (Worker.this.service) {
               DebugSession var3 = Worker.this.service.getCurrentSession();
               if (var3 != null) {
                  var3.getController().resume(var3.getParams());
               }
            }
         }
      }

      private class StopHandler implements ActionListener {
         private StopHandler() {
         }

         @Override
         public void actionPerformed(ActionEvent var1) {
            InteractiveTestInProgressStrategy.Worker.Stopper var2 = Worker.this.new Stopper();
            var2.execute();
         }
      }

      private class Stopper extends KongaSwingWorker<Void, Void> {
         private Stopper() {
         }

         protected Void doInBackground() throws Exception {
            TestStopper var1 = new TestStopper(Worker.this.service, new CallbackResultAdapter() {
               @Override
               public void succeeded(Object var1) {
                  Worker.this.requestNewSession();
               }

               @Override
               public void cancelled(Object var1) {
                  Worker.this.requester.requestDenied();
               }
            });
            var1.stop();
            return null;
         }
      }
   }
}
