package org.etlforge.system.thread;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.concurrent.Future;

public final class PendingFutures {
   private final Set<Future<?>> jobs = Sets.newHashSet();
   private final Set<Future<?>> alreadyRemoved = Sets.newHashSet();
   private final Object lock = new Object();

   public void addJob(Future<?> var1) {
      synchronized (this.lock) {
         if (!this.alreadyRemoved.contains(var1)) {
            this.jobs.add(var1);
         }
      }
   }

   public void jobDone(Future<?> var1) {
      synchronized (this.lock) {
         this.jobs.remove(var1);
         this.alreadyRemoved.add(var1);
      }
   }

   public void cancelAll() {
      synchronized (this.lock) {
         for (Future var3 : this.jobs) {
            var3.cancel(true);
         }

         this.jobs.clear();
         this.alreadyRemoved.clear();
      }
   }
}
