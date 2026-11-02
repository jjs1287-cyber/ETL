package org.etlforge.system.thread;

import com.google.common.base.Preconditions;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class CountDownLatchCondition implements Condition {
   private final CountDownLatch latch;

   public CountDownLatchCondition(CountDownLatch var1) {
      this.latch = Preconditions.checkNotNull(var1);
   }

   @Override
   public void waitFor() throws InterruptedException {
      this.latch.await();
   }

   @Override
   public boolean waitFor(long var1, TimeUnit var3) throws InterruptedException {
      return this.latch.await(var1, var3);
   }
}
