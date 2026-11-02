  
                             
   
                                        
                                        
   
package org.etlforge.system.thread;

import com.google.common.base.Preconditions;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.etlforge.system.thread.Condition;

public final class CountDownLatchCondition
implements Condition {
    private final CountDownLatch latch;

    public CountDownLatchCondition(CountDownLatch countDownLatch) {
        this.latch = (CountDownLatch)Preconditions.checkNotNull((Object)countDownLatch);
    }

    @Override
    public void waitFor() throws InterruptedException {
        this.latch.await();
    }

    @Override
    public boolean waitFor(long l, TimeUnit timeUnit) throws InterruptedException {
        return this.latch.await(l, timeUnit);
    }
}

