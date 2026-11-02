package org.etlforge.integration.client.server;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.concurrent.CountDownLatch;

public abstract class ResultProducingServerCallback<T> implements ServerCallback {
   private final CountDownLatch latch = new CountDownLatch(1);
   private T result;
   private Exception error;

   public final Optional<T> awaitResult() throws Exception {
      try {
         this.latch.await();
         if (this.error == null) {
            return Optional.fromNullable(this.result);
         } else {
            throw this.error;
         }
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
         return Optional.absent();
      }
   }

   protected final void set(T var1) {
      this.result = Preconditions.checkNotNull((T)var1, "result");
      this.latch.countDown();
   }

   @Override
   public final void caught(Throwable var1) {
      Preconditions.checkNotNull(var1, "t");
      if (var1 instanceof Exception) {
         this.error = (Exception)var1;
      } else {
         this.error = new Exception(var1);
      }

      this.latch.countDown();
   }

   @Override
   public final void cancelled() {
      this.latch.countDown();
   }
}
