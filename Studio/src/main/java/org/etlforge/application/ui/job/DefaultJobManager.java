package org.etlforge.application.ui.job;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

final class DefaultJobManager implements UiJobManager {
   private final Set<UiJob> jobs;
   private final Map<UiJob, Future<?>> futures;
   private final ExecutorService sequentialService;
   private final ExecutorService parallellService;
   private final List<JobStatusListener> globalStatusListeners = new CopyOnWriteArrayList<>();
   private final Object lock = new Object();

   public DefaultJobManager() {
      UiJobConsole.getConsole(this);
      this.jobs = new HashSet<>();
      this.futures = Maps.newHashMap();
      this.sequentialService = Executors.newSingleThreadExecutor();
      this.parallellService = Executors.newCachedThreadPool();
   }

   @Override
   public Future<?> submitForSequentialProcessing(UiJob var1) {
      Preconditions.checkState(!var1.isRunning(), "job is already running");
      this.prepareSubmittedJob(var1);
      synchronized (this.lock) {
         DefaultJobManager.UiJobWrapper var3 = new DefaultJobManager.UiJobWrapper(var1);
         this.jobs.add(var1);
         Future var4 = this.submitSequentialImpl(var3);
         this.futures.put(var1, var4);
         return var4;
      }
   }

   @Override
   public Future<?> submitForParallellProcessing(UiJob var1) {
      UiJobLogger.LOGGER.fine("Received job for parallell processing: [" + var1.getName() + "]");
      Preconditions.checkState(!var1.isRunning(), "job is already running");
      this.prepareSubmittedJob(var1);
      synchronized (this.lock) {
         DefaultJobManager.UiJobWrapper var3 = new DefaultJobManager.UiJobWrapper(var1);
         this.jobs.add(var1);
         Future var4 = this.submitParallellImpl(var3, var1.getName());
         this.futures.put(var1, var4);
         return var4;
      }
   }

   @Override
   public boolean cancelJob(UiJob var1) {
      synchronized (this.lock) {
         if (!this.jobs.contains(var1)) {
            return false;
         }
      }

      if (isCancelRequestEffective(var1)) {
         this.cancelJobImpl(var1);
         return true;
      } else {
         return false;
      }
   }

   private static boolean isCancelRequestEffective(UiJob var0) {
      return var0 != null && !var0.isDone() && var0.canBeCancelled() && var0.cancel();
   }

   private void cancelJobImpl(UiJob var1) {
      Future var2 = this.getFutureForJob(var1);
      if (var2 != null) {
         long var3 = var1.getCancelTimeout();
         boolean var5 = var1.mayBeInterrupted();
         if (var3 > 0L && var5) {
            DefaultJobManager.CancelTask.schedule(var1, var2, var3);
         }
      }
   }

   private Future<?> getFutureForJob(UiJob var1) {
      synchronized (this.lock) {
         return this.futures.get(var1);
      }
   }

   @Override
   public void addGlobalJobStatusListener(JobStatusListener var1) {
      this.globalStatusListeners.add(var1);
   }

   @Override
   public void removeGlobalJobStatusListener(JobStatusListener var1) {
      this.globalStatusListeners.remove(var1);
   }

   private void prepareSubmittedJob(UiJob var1) {
      for (JobStatusListener var3 : this.globalStatusListeners) {
         var1.addJobStatusListener(var3);
      }

      var1.setStatus(JobStatus.SUBMITTED);
   }

   private void removeJob(UiJob var1) {
      synchronized (this.lock) {
         this.jobs.remove(var1);
         this.futures.remove(var1);
      }
   }

   private Future<?> submitSequentialImpl(Runnable var1) {
      UiJobLogger.LOGGER.fine("Passing job to sequential service");
      return this.sequentialService.submit(var1);
   }

   private Future<?> submitParallellImpl(Runnable var1, String var2) {
      UiJobLogger.LOGGER.fine("Passing job to parallell service: [" + var2 + "]");
      return this.parallellService.submit(var1);
   }

   private static final class CancelTask extends TimerTask {
      private final UiJob job;
      private final Future<?> future;

      private CancelTask(UiJob var1, Future<?> var2) {
         this.job = var1;
         this.future = var2;
      }

      @Override
      public void run() {
         if (this.job.isRunning()) {
            this.future.cancel(true);
         }
      }

      public static void schedule(UiJob var0, Future<?> var1, long var2) {
         Timer var4 = new Timer(var0.getName());
         DefaultJobManager.CancelTask var5 = new DefaultJobManager.CancelTask(var0, var1);
         var4.schedule(var5, var2);
      }
   }

   private class UiJobWrapper implements Runnable {
      private final UiJob job;

      public UiJobWrapper(UiJob var2) {
         this.job = var2;
      }

      @Override
      public void run() {
         try {
            UiJobLogger.LOGGER.fine("Now starting the job [" + this.job.getName() + "]");
            this.job.run();
         } finally {
            UiJobLogger.LOGGER.fine("The job [" + this.job.getName() + "] has completed");
            DefaultJobManager.this.removeJob(this.job);
         }
      }
   }
}
