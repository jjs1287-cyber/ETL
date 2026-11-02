package org.etlforge.ui.wait;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchCallback extends ResourceReleasingCallback {
   private final CountDownLatch latch;

   public CountDownLatchCallback() {
      this(new CountDownLatch(1));
   }

   public CountDownLatchCallback(CountDownLatch var1) {
      this.latch = var1;
   }

   @Override
   protected final void releaseResource() {
      this.latch.countDown();
   }

   public final void waitUntilDone() throws InterruptedException {
      this.latch.await();
   }
}
