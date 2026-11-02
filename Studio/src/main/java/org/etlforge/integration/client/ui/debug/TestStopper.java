package org.etlforge.integration.client.ui.debug;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.event.DebugServiceAdapter;
import org.etlforge.integration.debug.client.event.DebugServiceEvent;
import org.etlforge.system.callback.CallbackResult;

final class TestStopper {
   private final DebugService service;
   private final CallbackResult callback;
   private TestStopper.StopListener listener;
   private CountDownLatch latch;

   public TestStopper(DebugService var1, CallbackResult var2) {
      this.service = var1;
      this.callback = var2;
   }

   public void stop() {
      synchronized (this.service) {
         DebugSession var2 = this.service.getCurrentSession();
         if (var2 == null) {
            this.callback.succeeded(null);
            return;
         }

         this.latch = new CountDownLatch(1);
         this.listener = new TestStopper.StopListener();
         this.service.addListener(this.listener);
         var2.getController().stop();
      }

      this.waitForTermination();
   }

   private void waitForTermination() {
      label37: {
         try {
            boolean var1 = this.latch.await(5L, TimeUnit.SECONDS);
            if (!var1) {
               break label37;
            }

            this.callback.succeeded(null);
         } catch (InterruptedException var5) {
            Thread.currentThread().interrupt();
            break label37;
         } finally {
            this.service.removeListener(this.listener);
         }

         return;
      }

      this.callback.cancelled(null);
   }

   private class StopListener extends DebugServiceAdapter {
      private StopListener() {
      }

      @Override
      public void debugSessionEnded(DebugServiceEvent var1) {
         TestStopper.this.service.removeListener(this);
         TestStopper.this.latch.countDown();
      }
   }
}
