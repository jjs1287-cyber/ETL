package org.etlforge.integration.activity.ui.monitor;

import com.google.common.base.Optional;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.activity.client.log.LogStatus;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.serverapi.log.GetOperationLogCallback;
import org.etlforge.integration.activity.serverapi.log.OperationLogRequest;
import org.etlforge.integration.activity.serverapi.queue.DeleteQueueEntriesCallback;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public final class OperationMonitor implements UiProvider {
   private static final long INTERVAL_IN_MILLIS = 2500L;
   public static final String RUNNING_PROPERTY = "running";
   private final OperationMonitorApis serverApis;
   private final OperationMonitor.DeleteQueueAction deleteQueueAction = new OperationMonitor.DeleteQueueAction();
   private final LogMonitor logMonitor = new LogMonitor();
   private final ProgressTracker progressTracker;
   private Timer timer;
   private final PropertyChangeSupport changeSupport;
   private final List<OperationMonitorListener> listeners = new CopyOnWriteArrayList<>();
   private volatile boolean hideProgressTrackerWhenDone = true;
   private final Object lock = new Object();

   public OperationMonitor(OperationMonitorApis var1) {
      this.serverApis = var1;
      this.progressTracker = var1.isProgressTrackingSupported() ? new ProgressTracker(var1.getProgressProvider()) : null;
      this.changeSupport = new PropertyChangeSupport(this);
   }

   public void addListener(OperationMonitorListener var1) {
      this.listeners.add(var1);
   }

   public void removeListener(OperationMonitorListener var1) {
      this.listeners.remove(var1);
   }

   public Action getDeleteQueueAction() {
      return this.deleteQueueAction;
   }

   public void setHideProgressTrackerWhenDone(boolean var1) {
      this.hideProgressTrackerWhenDone = var1;
   }

   public void stop() {
      synchronized (this.lock) {
         if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
            this.stopProgressMonitor();
            this.deleteQueueAction.setDone();
            this.changeSupport.firePropertyChange("running", true, false);
         }
      }
   }

   private void stopProgressMonitor() {
      if (this.progressTracker != null) {
         this.progressTracker.stop();
         if (this.hideProgressTrackerWhenDone) {
            this.progressTracker.setVisible(false);
         }
      }
   }

   public void start(long var1) {
      synchronized (this.lock) {
         if (this.timer != null) {
            this.timer.cancel();
         }

         this.timer = new Timer();
         this.timer.schedule(new OperationMonitor.RefreshTask(var1), 0L, 2500L);
         this.changeSupport.firePropertyChange("running", false, true);
         this.deleteQueueAction.reset(var1);
         this.showProgressTracker();
      }
   }

   private void showProgressTracker() {
      if (this.progressTracker != null) {
         this.progressTracker.setVisible(true);
      }
   }

   public void reset(long var1) {
      synchronized (this.lock) {
         this.stop();
         this.resetMonitors();
         this.start(var1);
      }
   }

   private void resetMonitors() {
      this.logMonitor.reset();
      if (this.progressTracker != null) {
         this.progressTracker.reset();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(this.logMonitor);
      ProgressTracker var3 = this.getProgressMonitor();
      if (var3 != null) {
         JPanel var4 = BorderLayoutBuilder.fillWith(var3);
         var4.setBorder(Empty.border(2));
         var2.south(var4);
      }

      var2.decorate(var1);
   }

   private ProgressTracker getProgressMonitor() {
      synchronized (this.lock) {
         return this.progressTracker;
      }
   }

   @Override
   public void requestFocus() {
   }

   public void addRunningListener(PropertyChangeListener var1) {
      this.changeSupport.addPropertyChangeListener("running", var1);
   }

   public void removeRunningListener(PropertyChangeListener var1) {
      this.changeSupport.removePropertyChangeListener("running", var1);
   }

   private class DeleteQueueAction extends AbstractAction {
      private volatile boolean working;
      private volatile long opInstanceId = -1L;
      private volatile boolean done;

      public DeleteQueueAction() {
         super(null, ApplicationResources.ICONS.getIcon("Stop.16"));
         this.putValue("ShortDescription", "Stop the operation by removing it from the queue");
         this.setEnabled(false);
      }

      public void reset(long var1) {
         this.opInstanceId = var1;
         this.done = false;
         this.checkEnabledState();
      }

      public void setDone() {
         this.done = true;
         this.checkEnabledState();
      }

      private void checkEnabledState() {
         ActionUtils.setEnabledSafely(this, !this.done && !this.working && this.opInstanceId >= 0L);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.working = true;
         this.setEnabled(false);
         Application.getWorker().submitForParallel(new ApplicationWorker.SafeRunnable() {
            @Override
            public void run() {
               OperationMonitor.this.serverApis.cancelOperation(DeleteQueueAction.this.opInstanceId, new DeleteQueueEntriesCallback() {
                  @Override
                  public void caught(Throwable var1) {
                     this.done();
                     ErrorLog.attention(OperationMonitor.class, "Could not remove the operation from the queue.", var1);
                  }

                  private void done() {
                     DeleteQueueAction.this.working = false;
                     DeleteQueueAction.this.checkEnabledState();
                  }

                  @Override
                  public void cancelled() {
                     this.done();
                  }

                  @Override
                  public void entriesWereDeleted(long[] var1) {
                     this.done();
                  }
               });
            }
         });
      }
   }

   private class RefreshLogCallback implements GetOperationLogCallback {
      private final long opInstanceId;
      private volatile boolean done;

      public RefreshLogCallback(long var2) {
         this.opInstanceId = var2;
      }

      public boolean isDone() {
         return this.done;
      }

      @Override
      public void logWasRetrieved(List<OperationLogEntry> var1) {
         OperationMonitor.this.logMonitor.update(var1);
         this.checkIfDone(var1);
      }

      private void checkIfDone(List<OperationLogEntry> var1) {
         if (var1.size() == 1) {
            OperationLogEntry var2 = (OperationLogEntry)var1.get(0);
            LogStatus var3 = var2.getLogStatus();
            this.done = var3.isDone();
            if (this.done) {
               this.notifyListeners(var2.getOperationName(), Optional.of(var3));
            }
         } else {
            ApplicationLog.getMainLogger().info("Unexpected number of log entries returned in progress tracking: " + var1.size());
            this.done = true;
            this.notifyListeners("", Optional.absent());
         }
      }

      private void notifyListeners(String var1, Optional<LogStatus> var2) {
         for (OperationMonitorListener var4 : OperationMonitor.this.listeners) {
            var4.operationCompleted(var1, this.opInstanceId, var2);
         }
      }

      @Override
      public void cancelled() {
         OperationMonitor.this.stop();
      }

      @Override
      public void caught(Throwable var1) {
      }
   }

   private class RefreshTask extends TimerTask {
      private final long opInstanceId;
      private final OperationMonitor.RefreshLogCallback logCallback;
      private final OperationLogRequest logRequest;

      public RefreshTask(long var2) {
         this.opInstanceId = var2;
         this.logCallback = OperationMonitor.this.new RefreshLogCallback(var2);
         this.logRequest = new OperationLogRequest();
         this.logRequest.opInstanceId = var2;
      }

      @Override
      public void run() {
         this.queryLog();
         this.updateProgress();
         this.checkAutoStop();
      }

      private void updateProgress() {
         if (!this.logCallback.isDone()) {
            ProgressTracker var1 = OperationMonitor.this.getProgressMonitor();
            if (var1 != null) {
               var1.update(this.opInstanceId);
            }
         }
      }

      private void queryLog() {
         OperationMonitor.this.serverApis.getLog(this.logRequest, this.logCallback);
      }

      private void checkAutoStop() {
         if (this.logCallback.isDone()) {
            OperationMonitor.this.stop();
         }
      }
   }
}
