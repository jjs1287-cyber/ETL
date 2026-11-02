package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.List;
import java.util.concurrent.CountDownLatch;

final class PostLoadingService {
   private final CountDownLatch latch = new CountDownLatch(1);
   private final List<Runnable> jobs = Lists.newArrayList();
   private final Object lock = new Object();

   public PostLoadingService() {
   }

   public void pageIsLoaded() {
      synchronized (this.lock) {
         if (this.latch.getCount() != 0L) {
            this.latch.countDown();

            for (Runnable var3 : this.jobs) {
               this.runJob(var3);
            }

            this.jobs.clear();
         }
      }
   }

   private void runJob(Runnable var1) {
      EventQueue.invokeLater(var1);
   }

   public void waitUntilLoaded() throws InterruptedException {
      this.latch.await();
   }

   public void addJob(Runnable var1) {
      synchronized (this.lock) {
         if (this.latch.getCount() == 0L) {
            this.runJob(var1);
         } else {
            this.jobs.add(var1);
         }
      }
   }
}
