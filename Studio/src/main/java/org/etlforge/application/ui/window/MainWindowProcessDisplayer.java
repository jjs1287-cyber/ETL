package org.etlforge.application.ui.window;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.job.JobConsoleWindowProviderFactory;
import org.etlforge.application.ui.job.JobStatusAdapter;
import org.etlforge.application.ui.job.JobStatusChangedEvent;
import org.etlforge.application.ui.job.ProcessConsoleIcons;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.util.UiUtils;

public final class MainWindowProcessDisplayer implements QuickLauncher {
   private final ApplicationWindow window;
   private final MainWindowProcessDisplayer.ShowConsoleAction launchAction;
   private final QuickLauncher quickLauncher;
   private final Set<UiJob> runningProcesses;
   private boolean processesExist;

   public MainWindowProcessDisplayer(ApplicationWindow var1) {
      Preconditions.checkNotNull(var1, "window");
      this.window = var1;
      this.launchAction = new MainWindowProcessDisplayer.ShowConsoleAction();
      this.quickLauncher = new ActionBasedApplicationWindowQuickLauncher(this.launchAction);
      this.runningProcesses = new LinkedHashSet<>();
      this.processesExist = true;
      this.onNoProcessRunning();
      ApplicationUi.getJobManager().addGlobalJobStatusListener(new MainWindowProcessDisplayer.JobStatusListenerImpl());
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.quickLauncher.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.quickLauncher.requestFocus();
   }

   @Override
   public void publish() {
      this.quickLauncher.publish();
   }

   @Override
   public void hide() {
   }

   private void onNoProcessRunning() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (MainWindowProcessDisplayer.this.processesExist) {
               MainWindowProcessDisplayer.this.processesExist = false;
               MainWindowProcessDisplayer.this.launchAction.setOn(false);
            }
         }
      });
   }

   private void onProcessesRunning() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (!MainWindowProcessDisplayer.this.processesExist) {
               MainWindowProcessDisplayer.this.processesExist = true;
               MainWindowProcessDisplayer.this.launchAction.setOn(true);
            }
         }
      });
   }

   private final class JobStatusListenerImpl extends JobStatusAdapter {
      private final Object lock = new Object();

      private JobStatusListenerImpl() {
      }

      @Override
      public void jobEnded(JobStatusChangedEvent var1) {
         UiJob var2 = var1.getJob();
         synchronized (this.lock) {
            MainWindowProcessDisplayer.this.runningProcesses.remove(var2);
            if (MainWindowProcessDisplayer.this.runningProcesses.isEmpty()) {
               MainWindowProcessDisplayer.this.onNoProcessRunning();
            }
         }
      }

      @Override
      public void jobStarted(JobStatusChangedEvent var1) {
         UiJob var2 = var1.getJob();
         synchronized (this.lock) {
            MainWindowProcessDisplayer.this.runningProcesses.add(var2);
            MainWindowProcessDisplayer.this.onProcessesRunning();
         }
      }
   }

   private final class ShowConsoleAction extends AbstractAction {
      public ShowConsoleAction() {
         super("");
         this.putValue("ShortDescription", ApplicationResources.STRINGS.getString("MainWindowProcessDisplayer.ShowConsole.ToolTip"));
         this.setOn(false);
      }

      public void setOn(boolean var1) {
         if (var1) {
            this.putValue("SmallIcon", ProcessConsoleIcons.PROCESS_ON_16);
            this.putValue("KongaMediumIconKey", ProcessConsoleIcons.PROCESS_ON_24);
         } else {
            this.putValue("SmallIcon", ProcessConsoleIcons.PROCESS_OFF_16);
            this.putValue("KongaMediumIconKey", ProcessConsoleIcons.PROCESS_OFF_24);
         }
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         WindowSectionContentProvider var2 = JobConsoleWindowProviderFactory.newContentProvider();
         MainWindowProcessDisplayer.this.window.displayContent(var2, ApplicationWindow.SOUTH, null, null);
      }
   }
}
