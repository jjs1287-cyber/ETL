package org.etlforge.application.ui.job;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import org.etlforge.application.Application;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.lang.Intervals;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.system.thread.Cancelable;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public abstract class UiJob implements Cancelable {
   public static final int UNDEFINED_COMPLETION = -1;
   private final String name;
   private boolean mayBeInterrupted;
   private boolean canBeCancelled;
   private long cancelTimeout;
   private int completionLevel = -1;
   private boolean isRunning;
   private boolean isCancelled;
   private boolean isDone;
   private String message;
   private long startTime;
   private long endTime;
   private ExceptionHandler<Throwable> uncaughtExceptionHandler;
   private Throwable uncaughtException;
   private final Object lock = new Object();
   private volatile boolean exclusive;
   private volatile boolean blockUntilDone;
   private boolean useProgressDialog;
   private int progressDialogDelay = -1;
   private final List<UiJobProgressMonitor> monitors;
   private final List<JobStatusListener> statusListeners;
   private JobStatus status;
   private final Object statusLock = new Object();

   protected UiJob(String var1) {
      this.name = var1;
      this.monitors = new CopyOnWriteArrayList<>();
      this.statusListeners = new CopyOnWriteArrayList<>();
      this.canBeCancelled = true;
      this.mayBeInterrupted = true;
      this.cancelTimeout = 250L;
   }

   public final Future<?> submit() {
      UiJobManager var1 = ApplicationUi.getJobManager();
      if (this.useProgressDialog) {
         this.createProgressMonitorDialog();
      }

      return this.exclusive ? var1.submitForSequentialProcessing(this) : var1.submitForParallellProcessing(this);
   }

   private void createProgressMonitorDialog() {
      UiJobProgressMonitorDialog var1 = UiJobProgressMonitorDialog.createDisplayer(this, this.progressDialogDelay);
      this.addProgressMonitor(var1);
   }

   public final void submit(WaitService var1, int var2) {
      this.waitWhileRunning(var1, var2);
      this.submit();
   }

   public final void setBlockUntilDone(boolean var1) {
      this.blockUntilDone = var1;
   }

   public final void setExclusive(boolean var1) {
      this.exclusive = var1;
   }

   public final void setUseProgressDialog(int var1) {
      this.useProgressDialog = true;
      this.progressDialogDelay = var1;
   }

   public final String getName() {
      return this.name;
   }

   public final boolean isDone() {
      synchronized (this.lock) {
         return this.isDone;
      }
   }

   public final boolean canBeCancelled() {
      synchronized (this.lock) {
         return this.canBeCancelled;
      }
   }

   protected final void setCanBeCancelled(boolean var1) {
      synchronized (this.lock) {
         this.canBeCancelled = var1;
      }

      this.notifyMonitorsOnCanBeCancelled(var1);
   }

   @Override
   public final boolean cancel() {
      boolean var1 = false;
      boolean var2 = false;
      synchronized (this.lock) {
         if (this.canBeCancelled()) {
            if (!this.isCancelled) {
               var2 = true;
               this.isCancelled = true;
               this.setMessage(UiJobMessages.CANCELLED);
               this.setStatus(JobStatus.CANCEL);
            }

            var1 = true;
         }
      }

      if (var2) {
         this.onJobCancelled();
      }

      return var1;
   }

   protected void onJobCancelled() {
   }

   public final boolean isCancelled() {
      synchronized (this.lock) {
         return this.isCancelled;
      }
   }

   public final long getCancelTimeout() {
      return this.cancelTimeout;
   }

   protected final void setCancelTimeout(long var1) {
      this.cancelTimeout = var1;
   }

   public final boolean mayBeInterrupted() {
      synchronized (this.lock) {
         return this.mayBeInterrupted;
      }
   }

   protected final void setMayBeInterrupted(boolean var1) {
      synchronized (this.lock) {
         this.mayBeInterrupted = var1;
      }
   }

   public final String getMessage() {
      synchronized (this.lock) {
         return this.message;
      }
   }

   protected final void setMessage(String var1) {
      synchronized (this.lock) {
         this.message = var1;
      }

      this.notifyMonitorsOfNewMessage(var1);
   }

   public final int getCompletionLevel() {
      synchronized (this.lock) {
         return this.completionLevel;
      }
   }

   protected final void setCompletionValue(int var1) {
      var1 = Intervals.valueWithin(var1, -1, 100);
      synchronized (this.lock) {
         if (var1 != this.completionLevel) {
            this.completionLevel = var1;
            this.notifyMonitorsOfNewCompletionLevel(var1);
         }
      }
   }

   private void notifyMonitorsOfNewCompletionLevel(int var1) {
      for (UiJobProgressMonitor var3 : this.monitors) {
         var3.setCompletionLevel(var1);
      }
   }

   public final boolean isRunning() {
      synchronized (this.lock) {
         return this.isRunning;
      }
   }

   public final void waitWhileRunning(final WaitService var1, final int var2) {
      synchronized (this.lock) {
         if (this.isRunning()) {
            throw new IllegalStateException("waitWhileRunning must be called before the job has started");
         }

         this.addJobStatusListener(new JobStatusAdapter() {
            private WaitLock waitLock;

            @Override
            public void jobEnded(JobStatusChangedEvent var1x) {
               this.waitLock.release();
            }

            @Override
            public void jobStarted(JobStatusChangedEvent var1x) {
               this.waitLock = var1.startWait(var2);
            }
         });
      }
   }

   public final long getElapsedTime() {
      return System.currentTimeMillis() - this.startTime;
   }

   public final long getFinalExecutionTime() {
      return this.endTime - this.startTime;
   }

   public final void setUncaughtExceptionHandler(ExceptionHandler<Throwable> var1) {
      this.uncaughtExceptionHandler = var1;
   }

   protected final Throwable getUncaughtException() {
      return this.uncaughtException;
   }

   final void run() {
      try {
         UiJobLogger.LOGGER.fine("Entered run() in the job [" + this.name + "]");
         synchronized (this.lock) {
            this.isRunning = true;
         }

         this.startTime = System.currentTimeMillis();
         this.setStatus(JobStatus.RUNNING);
         this.fireJobStarted();
         this.notifyMonitorsOnStart();
         this.callRunImpl();
         this.runCompletedOnUiThread();
         if (!this.isCancelled() && this.getStatus() != JobStatus.ERROR) {
            this.setStatus(JobStatus.OK);
         }
      } catch (InterruptedException var19) {
         Thread.currentThread().interrupt();
         System.err.println(var19.getMessage());
      } catch (Throwable var20) {
         this.handleUncaughtException(var20);
      } finally {
         synchronized (this.lock) {
            this.postJobCleanup();
         }
      }
   }

   private void callRunImpl() throws InterruptedException {
      if (!this.isCancelled()) {
         if (this.blockUntilDone) {
            final InterruptedException[] var1 = new InterruptedException[1];
            Runnable var2 = new Runnable() {
               @Override
               public void run() {
                  try {
                     if (!UiJob.this.isCancelled()) {
                        UiJob.this.runImpl();
                     }
                  } catch (InterruptedException var2x) {
                     var1[0] = var2x;
                  }
               }
            };
            Application.getWorker().submitForParallelAndWait(var2, this.getBlockingErrorHandler());
            if (var1[0] != null) {
               throw var1[0];
            }
         } else {
            this.runImpl();
         }
      }
   }

   private ExceptionHandler<Throwable> getBlockingErrorHandler() {
      return this.uncaughtExceptionHandler != null
         ? this.uncaughtExceptionHandler
         : new LoggingExceptionHandler(this.getClass(), "An unexpected error occurred");
   }

   private void runCompletedOnUiThread() throws InterruptedException {
      try {
         EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
               UiJob.this.completed();
            }
         });
      } catch (InvocationTargetException var2) {
         var2.printStackTrace();
      }
   }

   private void notifyMonitorsOnStart() {
      UiJobLogger.LOGGER.fine("[" + this.name + "]: notifyMonitorsOnStart - number of monitors: " + this.monitors.size());
      int var1 = 1;

      for (UiJobProgressMonitor var3 : this.monitors) {
         UiJobLogger.LOGGER.fine("[" + this.name + "]: jobStarted sent to monitor #" + var1 + ": " + var3.getClass().getName());
         var3.started(this);
         var1++;
      }
   }

   private void notifyMonitorsOnWhenDone() {
      UiJobLogger.LOGGER.fine("[" + this.name + "]: notifyMonitorsOnWhenDone - number of monitors: " + this.monitors.size());
      int var1 = 1;

      for (UiJobProgressMonitor var3 : this.monitors) {
         UiJobLogger.LOGGER.fine("[" + this.name + "]: jobEnded sent to monitor #" + var1 + ": " + var3.getClass().getName());
         var3.ended();
         var1++;
      }
   }

   private void notifyMonitorsOnCanBeCancelled(boolean var1) {
      UiJobLogger.LOGGER.fine("[" + this.name + "]: notifyMonitorsOnCanBeCancelled - number of monitors: " + this.monitors.size());
      int var2 = 1;

      for (UiJobProgressMonitor var4 : this.monitors) {
         UiJobLogger.LOGGER.fine("[" + this.name + "]: canBeCancelled flag sent to monitor #" + var2 + ": " + var4.getClass().getName());
         var4.setCanBeCancelled(var1);
         var2++;
      }
   }

   private void notifyMonitorsOfNewMessage(String var1) {
      UiJobLogger.LOGGER.fine("[" + this.name + "]: notifyMonitorsOfNewMessage - number of monitors: " + this.monitors.size());
      int var2 = 1;

      for (UiJobProgressMonitor var4 : this.monitors) {
         UiJobLogger.LOGGER.fine("[" + this.name + "]: new message sent to monitor #" + var2 + ": " + var4.getClass().getName());
         var4.setMessage(var1);
         var2++;
      }
   }

   private void handleUncaughtException(Throwable var1) {
      this.uncaughtException = var1;
      if (this.uncaughtExceptionHandler != null) {
         this.uncaughtExceptionHandler.caught(var1);
      }

      this.setStatus(JobStatus.ERROR);
   }

   private void postJobCleanup() {
      this.isRunning = false;
      this.isDone = true;
      this.fireJobEnded();
      this.endTime = System.currentTimeMillis();
      this.notifyMonitorsOnWhenDone();
      this.monitors.clear();
      this.statusListeners.clear();
   }

   public final void addJobStatusListener(JobStatusListener var1) {
      this.statusListeners.add(var1);
   }

   public final void removeJobStatusListener(JobStatusListener var1) {
      this.statusListeners.remove(var1);
   }

   public final void addProgressMonitor(UiJobProgressMonitor var1) {
      synchronized (this.lock) {
         if (!this.isDone()) {
            this.monitors.add(var1);
         }
      }
   }

   public final void removeProgressMonitor(UiJobProgressMonitor var1) {
      this.monitors.remove(var1);
   }

   public final JobStatus getStatus() {
      synchronized (this.statusLock) {
         return this.status;
      }
   }

   protected final void setStatus(JobStatus var1) {
      UiJobLogger.LOGGER.fine("[" + this.name + "] = setStatus --> " + var1);
      if (var1 == null) {
         throw new NullPointerException("status");
      }

      synchronized (this.statusLock) {
         if (var1 == this.status) {
            return;
         }

         this.status = var1;
      }

      this.fireStatusChanged(var1);
   }

   private void fireJobStarted() {
      if (!this.statusListeners.isEmpty()) {
         JobStatusChangedEvent var1 = new JobStatusChangedEvent(this, JobStatus.RUNNING);
         UiJobLogger.LOGGER.fine("[" + this.name + "]: fireJobStarted - number of listeners: " + this.statusListeners.size());
         int var2 = 1;

         for (JobStatusListener var4 : this.statusListeners) {
            UiJobLogger.LOGGER.fine("[" + this.name + "]: jobStarted sent to listener #" + var2 + ": " + var4.getClass().getName());
            var4.jobStarted(var1);
            var2++;
         }

         UiJobLogger.LOGGER.fine("[" + this.name + "]: fireJobStarted - done");
      }
   }

   private void fireJobEnded() {
      if (!this.statusListeners.isEmpty()) {
         JobStatusChangedEvent var1 = new JobStatusChangedEvent(this, this.status);
         UiJobLogger.LOGGER.fine("[" + this.name + "]: fireJobEnded - number of listeners: " + this.statusListeners.size());
         int var2 = 1;

         for (JobStatusListener var4 : this.statusListeners) {
            UiJobLogger.LOGGER.fine("[" + this.name + "]: jobEnded sent to listener #" + var2 + ": " + var4.getClass().getName());
            var4.jobEnded(var1);
            var2++;
         }

         UiJobLogger.LOGGER.fine("[" + this.name + "]: fireJobEnded - done");
      }
   }

   private void fireStatusChanged(JobStatus var1) {
      if (!this.statusListeners.isEmpty()) {
         JobStatusChangedEvent var2 = new JobStatusChangedEvent(this, var1);
         UiJobLogger.LOGGER.fine("[" + this.name + "]: fireJobStatusChanged - new status: " + var1 + " - number of listeners: " + this.statusListeners.size());
         int var3 = 1;

         for (JobStatusListener var5 : this.statusListeners) {
            UiJobLogger.LOGGER.fine("[" + this.name + "]: jobStatusChanged sent to listener #" + var3 + ": " + var5.getClass().getName());
            var5.statusChanged(var2);
            var3++;
         }
      }
   }

   protected abstract void runImpl() throws InterruptedException;

   protected void completed() {
   }
}
