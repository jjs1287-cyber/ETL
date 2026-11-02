package org.etlforge.integration.client.ui.interchange.component.launcher;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.view.ViewLauncher;
import org.etlforge.application.ui.view.ViewManager;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.ProjectLoadingCancelledException;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.event.ProjectManagerEvent;
import org.etlforge.integration.client.project.event.ProjectManagerListener;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.event.ActionListeners;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.widget.Label;

public final class InterchangeLauncher implements ViewLauncher, LaunchOptionContainer {
   private final ApplicationWindow appWin;
   private final ProjectManager projectManager;
   private ImmutableList<LaunchOption> launchOptions = ImmutableList.of();
   private final ActionListeners launchListeners = new ActionListeners();
   private LaunchScreen launchScreen;
   private final InterchangeLauncher.InterchangeViewDisplayer viewDisplayer = new InterchangeLauncher.InterchangeViewDisplayer();

   public InterchangeLauncher(ApplicationWindow var1, ProjectManager var2) {
      this.appWin = Preconditions.checkNotNull(var1, "appWin");
      this.projectManager = Preconditions.checkNotNull(var2, "projectManager");
      var2.addProjectManagerListener(this.viewDisplayer);
   }

   public void setLaunchOptions(List<LaunchOption> var1) {
      this.launchOptions = ImmutableList.copyOf(var1);

      for (LaunchOption var3 : var1) {
         var3.setContainer(this);
      }
   }

   public ApplicationWindow getWindow() {
      return this.appWin;
   }

   @Override
   public void activate(UiCanvas var1) {
      UiProvider var2 = this.getUi();
      var2.decorate(var1);
      var2.requestFocus();
   }

   @Override
   public void decorate(Label var1) {
      var1.setIcon(ClientIcons.INTEGRATION_24);
      var1.setText("Design");
      var1.setToolTip("The design view is where you work with your integration projects");
   }

   public UiProvider getUi() {
      return new UiProvider() {
         @Override
         public void decorate(UiCanvas var1) {
            if (InterchangeLauncher.this.launchScreen == null) {
               InterchangeLauncher.this.launchScreen = new LaunchScreen(InterchangeLauncher.this.launchOptions);
            }

            var1.display(InterchangeLauncher.this.launchScreen.getContentPane());
         }

         @Override
         public void requestFocus() {
            InterchangeLauncher.this.launchScreen.requestFocus();
         }
      };
   }

   @Override
   public void launchProject(LaunchOption var1) {
      WaitLock var2 = this.getWindow().startWait();
      this.launchListeners.call().actionPerformed(new ActionEvent(this, 1001, "on_ok"));
      InterchangeLauncher.ProjectLoader var3 = new InterchangeLauncher.ProjectLoader(var1, var2);
      var3.start();
   }

   public void addLaunchListener(ActionListener var1) {
      this.launchListeners.add(var1);
   }

   public void removeLaunchListener(ActionListener var1) {
      this.launchListeners.remove(var1);
   }

   @Override
   public void dismiss() {
      this.hibernate();
   }

   public void hibernate() {
      for (LaunchOption var2 : this.launchOptions) {
         var2.hibernate();
      }
   }

   public void wakeUp() {
      for (LaunchOption var2 : this.launchOptions) {
         var2.wakeUp();
      }

      if (this.launchScreen != null) {
         this.launchScreen.selectDefaultOption();
      }
   }

   private class InterchangeViewDisplayer implements ProjectManagerListener {
      private InterchangeViewDisplayer() {
      }

      @Override
      public void projectOpened(ProjectManagerEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ViewManager var1x = InterchangeLauncher.this.appWin.getViewManager();
               var1x.showView("interchange");
            }
         });
      }

      @Override
      public void projectClosed(ProjectManagerEvent var1) {
      }
   }

   private class ProjectLoader implements Runnable {
      private final LaunchOption launchOption;
      private final WaitLock waitLock;

      public ProjectLoader(LaunchOption var2, WaitLock var3) {
         this.launchOption = var2;
         this.waitLock = var3;
      }

      public void start() {
         Application.getWorker().submitForParallel(this);
      }

      @Override
      public void run() {
         try {
            boolean var1 = this.closeCurrentProject();
            if (var1) {
               IntegrationProject var2 = this.launchOption.getProject();
               this.displayProject(var2);
            }

            InterchangeLauncher.this.hibernate();
         } catch (Exception var6) {
            this.handleError(var6);
         } finally {
            this.waitLock.release();
         }
      }

      private boolean closeCurrentProject() {
         if (InterchangeLauncher.this.projectManager.getCurrentProject() != null) {
            try {
               InterchangeLauncher.this.projectManager.closeCurrentProject();
            } catch (CloseProjectVetoException var2) {
               return false;
            }
         }

         return true;
      }

      private void displayProject(IntegrationProject var1) throws Exception {
         if (var1 != null) {
            InterchangeLauncher.this.projectManager.setCurrentProject(var1);
         }
      }

      private void handleError(Exception var1) {
         if (this.isOfInterestForTheUser(var1)) {
            ErrorLog.attention(InterchangeLauncher.class, var1.getMessage(), var1);
         }
      }

      private boolean isOfInterestForTheUser(Exception var1) {
         return !(var1.getCause() instanceof ProjectLoadingCancelledException);
      }
   }
}
