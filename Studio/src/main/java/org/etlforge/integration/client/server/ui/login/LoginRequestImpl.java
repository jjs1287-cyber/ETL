package org.etlforge.integration.client.server.ui.login;

import java.util.concurrent.CountDownLatch;
import org.etlforge.integration.client.server.login.LoginRequest;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.system.callback.CallbackResultWrapper;

final class LoginRequestImpl implements LoginRequest {
   private final CallbackResult callback;
   private final CountDownLatch latch;
   private boolean loginSucceeded;

   LoginRequestImpl(CallbackResult var1) {
      this.callback = new CallbackResultWrapper(var1);
      this.latch = new CountDownLatch(1);
   }

   void granted() {
      this.loginSucceeded = true;
      this.latch.countDown();
      this.callback.succeeded(null);
   }

   void denied(Object var1) {
      this.latch.countDown();
      this.callback.failed(var1);
   }

   void cancelled() {
      this.latch.countDown();
      this.callback.cancelled(null);
   }

   @Override
   public boolean isGranted() {
      try {
         this.latch.await();
         return this.loginSucceeded;
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
         var2.printStackTrace();
         return false;
      }
   }
}
