  
                             
   
                                        
                                  
                                                 
                                        
   
package org.etlforge.system.thread;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import org.etlforge.system.thread.Condition;
import org.etlforge.system.thread.TimeDuration;

public final class Timeout
implements Serializable {
    public static final Timeout INFINITE = new Timeout(null);
    private final TimeDuration duration;

    public static Timeout fromDuration(TimeDuration timeDuration) {
        return new Timeout((TimeDuration)Preconditions.checkNotNull((Object)timeDuration));
    }

    public static Timeout create(long l, TimeUnit timeUnit) {
        return new Timeout(new TimeDuration(l, timeUnit));
    }

    public static Timeout milliseconds(long l) {
        return Timeout.create(l, TimeUnit.MILLISECONDS);
    }

    public static Timeout seconds(long l) {
        return Timeout.create(l, TimeUnit.SECONDS);
    }

    private Timeout(TimeDuration timeDuration) {
        this.duration = timeDuration;
    }

    public int hashCode() {
        return this.duration == null ? 0 : this.duration.hashCode();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (this == INFINITE) {
            return object == INFINITE;
        }
        if (object instanceof Timeout) {
            Timeout timeout = (Timeout)object;
            return this.duration.equals(timeout.duration);
        }
        return false;
    }

    public String toString() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper((Object)this);
        if (this == INFINITE) {
            toStringHelper.addValue((Object)"INFINITE");
        } else {
            toStringHelper.add("duration", this.duration.getValue()).add("unit", (Object)this.duration.getUnit());
        }
        return toStringHelper.toString();
    }

    void sleep() throws InterruptedException {
        Preconditions.checkState((this != INFINITE ? 1 : 0) != 0, (Object)"Cannot call sleep() on an INFINITE Timeout.");
        this.duration.sleep();
    }

    public boolean tryLock(Lock lock) throws InterruptedException {
        if (this == INFINITE) {
            lock.lock();
            return true;
        }
        return lock.tryLock(this.duration.getValue(), this.duration.getUnit());
    }

    public boolean await(java.util.concurrent.locks.Condition condition) throws InterruptedException {
        if (this == INFINITE) {
            condition.await();
            return true;
        }
        return condition.await(this.duration.getValue(), this.duration.getUnit());
    }

    public boolean waitFor(Condition condition) throws InterruptedException {
        if (this == INFINITE) {
            condition.waitFor();
            return true;
        }
        return condition.waitFor(this.duration.getValue(), this.duration.getUnit());
    }

    public boolean await(CountDownLatch countDownLatch) throws InterruptedException {
        if (this == INFINITE) {
            countDownLatch.await();
            return true;
        }
        return countDownLatch.await(this.duration.getValue(), this.duration.getUnit());
    }

    public <T> T getComputedValue(Future<T> future) throws TimeoutException, ExecutionException, InterruptedException {
        if (this == INFINITE) {
            return future.get();
        }
        return future.get(this.duration.getValue(), this.duration.getUnit());
    }

    public boolean awaitTermination(ExecutorService executorService) throws InterruptedException {
        if (this == INFINITE) {
            return executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        }
        return executorService.awaitTermination(this.duration.getValue(), this.duration.getUnit());
    }
}

