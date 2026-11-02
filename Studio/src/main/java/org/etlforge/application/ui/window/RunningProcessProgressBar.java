package org.etlforge.application.ui.window;

import java.awt.EventQueue;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JProgressBar;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.job.JobStatusAdapter;
import org.etlforge.application.ui.job.JobStatusChangedEvent;
import org.etlforge.application.ui.job.JobStatusListener;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public class RunningProcessProgressBar implements UiProvider {
   private final Set<UiJob> runningJobs = new HashSet<>();
   private final JProgressBar progressBar = new JProgressBar();
   private JobStatusListener jobListener;
   private boolean hideWhenNoProcesses;

   public RunningProcessProgressBar() {
      this.progressBar.setBackground(Colors.NEUTRAL_BACKGROUND);
      this.hideWhenNoProcesses = true;
      this.progressBar.setIndeterminate(false);
      this.progressBar.setVisible(false);
   }

   public void publish() {
      if (this.jobListener == null) {
         this.jobListener = new JobStatusAdapter() {
            @Override
            public void jobEnded(final JobStatusChangedEvent var1) {
               EventQueue.invokeLater(new Runnable() {
                  @Override
                  public void run() {
                     RunningProcessProgressBar.this.onJobEnded(var1.getJob());
                  }
               });
            }

            @Override
            public void jobStarted(final JobStatusChangedEvent var1) {
               EventQueue.invokeLater(new Runnable() {
                  @Override
                  public void run() {
                     RunningProcessProgressBar.this.onJobStarted(var1.getJob());
                  }
               });
            }
         };
         ApplicationUi.getJobManager().addGlobalJobStatusListener(this.jobListener);
      }
   }

   public void uninstall(final boolean var1) {
      if (this.jobListener != null) {
         ApplicationUi.getJobManager().removeGlobalJobStatusListener(this.jobListener);
         this.jobListener = null;
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               RunningProcessProgressBar.this.progressBar.setIndeterminate(false);
               if (var1) {
                  RunningProcessProgressBar.this.progressBar.setVisible(false);
               }
            }
         });
      }
   }

   public void setHideWhenNoProcesses(boolean var1) {
      this.hideWhenNoProcesses = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.progressBar);
   }

   @Override
   public void requestFocus() {
      this.progressBar.requestFocus();
   }

   private void onJobStarted(UiJob var1) {
      boolean var2 = this.runningJobs.isEmpty();
      this.runningJobs.add(var1);
      this.updateToolTipText();
      if (var2) {
         this.progressBar.setIndeterminate(true);
         this.progressBar.setVisible(true);
      }
   }

   private void onJobEnded(UiJob var1) {
      this.runningJobs.remove(var1);
      this.updateToolTipText();
      if (this.runningJobs.isEmpty()) {
         if (this.hideWhenNoProcesses) {
            this.progressBar.setVisible(false);
         }

         this.progressBar.setIndeterminate(false);
      }
   }

   private void updateToolTipText() {
      int var2 = this.runningJobs.size();
      String var1;
      switch (var2) {
         case 0:
            var1 = null;
            break;
         case 1:
            var1 = ApplicationResources.STRINGS.getString("RunningProcessProgressBar.ToolTip.OneProcess");
            break;
         default:
            var1 = ApplicationResources.format("RunningProcessProgressBar.ToolTip.ManyProcesses", var2);
      }

      this.progressBar.setToolTipText(var1);
   }
}
