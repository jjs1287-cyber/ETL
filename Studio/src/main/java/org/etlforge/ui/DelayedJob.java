package org.etlforge.ui;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.etlforge.ui.util.UiUtils;

public final class DelayedJob {
   private final Runnable task;
   private final int delay;
   private Timer timer;

   public static DelayedJob createAndSchedule(int var0, Runnable var1) {
      DelayedJob var2 = new DelayedJob(var0, var1);
      var2.schedule();
      return var2;
   }

   public DelayedJob(int var1, Runnable var2) {
      this.task = var2;
      this.delay = var1;
   }

   public void schedule() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DelayedJob.this.scheduleImpl();
         }
      });
   }

   public void cancel() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DelayedJob.this.cancelImpl();
         }
      });
   }

   public void runNow() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DelayedJob.this.cancelImpl();
            DelayedJob.this.task.run();
         }
      });
   }

   private void scheduleImpl() {
      Preconditions.checkState(this.timer == null, "This job is already scheduled for execution");
      this.timer = new Timer(this.delay, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            DelayedJob.this.task.run();
            DelayedJob.this.timer = null;
         }
      });
      this.timer.setRepeats(false);
      this.timer.start();
   }

   private void cancelImpl() {
      if (this.timer != null) {
         this.timer.stop();
         this.timer = null;
      }
   }
}
