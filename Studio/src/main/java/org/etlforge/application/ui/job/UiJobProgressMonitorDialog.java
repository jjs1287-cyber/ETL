package org.etlforge.application.ui.job;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;

public final class UiJobProgressMonitorDialog extends KongaDialog implements UiJobProgressMonitor {
   private static final boolean RUN_IN_BACKGROUND_SUPPORTED = ApplicationConfiguration.getBoolean("UiJob.RunInBackground", true);
   private UiJob job;
   private DefaultUiJobProgressMonitor displayer;
   private final long delay;
   private boolean done;
   private Timer delayTimer;

   private UiJobProgressMonitorDialog(Window var1, UiJob var2, long var3) {
      super(var1, var2.getName());
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "<init>");
      this.job = var2;
      this.delay = var3;
      this.createDialog();
      this.setLocationRelativeTo(null);
   }

   @Override
   protected void onCancel() {
      ApplicationUi.getJobManager().cancelJob(this.job);
      super.onCancel();
   }

   public static UiJobProgressMonitorDialog createDisplayer(UiJob var0) {
      return createDisplayer(var0, -1);
   }

   public static UiJobProgressMonitorDialog createDisplayer(final UiJob var0, final int var1) {
      return UiThreadSupplier.call(new Supplier<UiJobProgressMonitorDialog>() {
         public UiJobProgressMonitorDialog get() {
            return new UiJobProgressMonitorDialog(UiUtils.getActiveWindow(), var0, var1);
         }
      });
   }

   @Override
   public void ended() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            UiJobProgressMonitorDialog.this.done = true;
            UiJobProgressMonitorDialog.this.displayer.ended();
            if (UiJobProgressMonitorDialog.this.delayTimer != null) {
               UiJobProgressMonitorDialog.this.delayTimer.stop();
               UiJobProgressMonitorDialog.this.delayTimer = null;
            }

            UiJobProgressMonitorDialog.this.job = null;
            UiJobProgressMonitorDialog.this.onOk();
         }
      });
   }

   @Override
   public void setCanBeCancelled(final boolean var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            UiJobProgressMonitorDialog.this.getCancelButton().setEnabled(var1);
         }
      });
   }

   @Override
   public void setCompletionLevel(int var1) {
      this.displayer.setCompletionLevel(var1);
   }

   @Override
   public void setMessage(String var1) {
      this.displayer.setMessage(var1);
   }

   @Override
   public void started(UiJob var1) {
      if (var1 == this.job) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               UiJobProgressMonitorDialog.this.update();
               UiJobProgressMonitorDialog.this.displayer.started(UiJobProgressMonitorDialog.this.job);
               if (UiJobProgressMonitorDialog.this.delay <= 0L) {
                  UiJobProgressMonitorDialog.this.setVisible(true);
               } else {
                  UiJobProgressMonitorDialog.this.scheduleDelayedVisibility();
               }
            }
         });
      }
   }

   private void scheduleDelayedVisibility() {
      this.delayTimer = new Timer((int)this.delay, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (!UiJobProgressMonitorDialog.this.done) {
               UiJobProgressMonitorDialog.this.setVisible(true);
            }
         }
      });
      this.delayTimer.setRepeats(false);
      this.delayTimer.start();
   }

   private void update() {
      if (!this.job.canBeCancelled()) {
         this.getCancelButton().setEnabled(false);
      }
   }

   private void createDialog() {
      this.setFixedButtonSize(false);
      this.displayer = new DefaultUiJobProgressMonitor(this.job, false);
      if (RUN_IN_BACKGROUND_SUPPORTED) {
         this.standardLayout(this.displayer, OK_CANCEL);
         this.getOKButton().setText(Strings.get("Job.ProgressDialog.RunInBackground"));
      } else {
         this.standardLayout(this.displayer, CANCEL_ONLY);
      }
   }
}
