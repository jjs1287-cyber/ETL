package org.etlforge.system.thread;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Objects.ToStringHelper;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;

public final class Timeout implements Serializable {
   public static final Timeout INFINITE = new Timeout(null);
   private final TimeDuration duration;

   public static Timeout fromDuration(TimeDuration var0) {
      return new Timeout(Preconditions.checkNotNull(var0));
   }

   public static Timeout create(long var0, TimeUnit var2) {
      return new Timeout(new TimeDuration(var0, var2));
   }

   public static Timeout milliseconds(long var0) {
      return create(var0, TimeUnit.MILLISECONDS);
   }

   public static Timeout seconds(long var0) {
      return create(var0, TimeUnit.SECONDS);
   }

   private Timeout(TimeDuration var1) {
      this.duration = var1;
   }

   @Override
   public int hashCode() {
      return this.duration == null ? 0 : this.duration.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (this == INFINITE) {
         return var1 == INFINITE;
      } else if (var1 instanceof Timeout) {
         Timeout var2 = (Timeout)var1;
         return this.duration.equals(var2.duration);
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      ToStringHelper var1 = Objects.toStringHelper(this);
      if (this == INFINITE) {
         var1.addValue("INFINITE");
      } else {
         var1.add("duration", this.duration.getValue()).add("unit", this.duration.getUnit());
      }

      return var1.toString();
   }

   void sleep() throws InterruptedException {
      Preconditions.checkState(this != INFINITE, "Cannot call sleep() on an INFINITE Timeout.");
      this.duration.sleep();
   }

   public boolean tryLock(Lock var1) throws InterruptedException {
      if (this == INFINITE) {
         var1.lock();
         return true;
      } else {
         return var1.tryLock(this.duration.getValue(), this.duration.getUnit());
      }
   }

   public boolean await(java.util.concurrent.locks.Condition var1) throws InterruptedException {
      if (this == INFINITE) {
         var1.await();
         return true;
      } else {
         return var1.await(this.duration.getValue(), this.duration.getUnit());
      }
   }

   public boolean waitFor(Condition var1) throws InterruptedException {
      if (this == INFINITE) {
         var1.waitFor();
         return true;
      } else {
         return var1.waitFor(this.duration.getValue(), this.duration.getUnit());
      }
   }

   public boolean await(CountDownLatch var1) throws InterruptedException {
      if (this == INFINITE) {
         var1.await();
         return true;
      } else {
         return var1.await(this.duration.getValue(), this.duration.getUnit());
      }
   }

   public <T> T getComputedValue(Future<T> var1) throws TimeoutException, ExecutionException, InterruptedException {
      return (T)(this == INFINITE ? var1.get() : var1.get(this.duration.getValue(), this.duration.getUnit()));
   }

   public boolean awaitTermination(ExecutorService var1) throws InterruptedException {
      return this == INFINITE ? var1.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS) : var1.awaitTermination(this.duration.getValue(), this.duration.getUnit());
   }
}
