  
                             
   
                                        
                                  
   
package org.etlforge.system.thread;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.concurrent.Future;

public final class PendingFutures {
    private final Set<Future<?>> jobs = Sets.newHashSet();
    private final Set<Future<?>> alreadyRemoved = Sets.newHashSet();
    private final Object lock = new Object();

      
                                                                         
       
    public void addJob(Future<?> future) {
        Object object = this.lock;
        synchronized (object) {
            if (this.alreadyRemoved.contains(future)) {
                return;
            }
            this.jobs.add(future);
        }
    }

      
                                                                         
       
    public void jobDone(Future<?> future) {
        Object object = this.lock;
        synchronized (object) {
            this.jobs.remove(future);
            this.alreadyRemoved.add(future);
        }
    }

      
                                                                         
       
    public void cancelAll() {
        Object object = this.lock;
        synchronized (object) {
            for (Future<?> future : this.jobs) {
                future.cancel(true);
            }
            this.jobs.clear();
            this.alreadyRemoved.clear();
        }
    }
}

