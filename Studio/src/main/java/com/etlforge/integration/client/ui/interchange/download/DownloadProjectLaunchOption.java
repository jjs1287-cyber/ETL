package com.etlforge.integration.client.ui.interchange.download;

import com.etlforge.integration.client.admin.project.ProjectDownloaderCallback;
import com.etlforge.integration.client.ui.interchange.resources.EnterpriseInterchangeResources;
import com.etlforge.integration.client.ui.project.download.console.DownloadProjectConsole;
import com.etlforge.integration.client.ui.project.download.console.DownloadProjectConsoleFactory;
import com.etlforge.integration.client.ui.project.download.resources.ProjectDownloadResources;
import java.awt.EventQueue;
import javax.swing.JLabel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.ProjectUiState;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.component.launcher.AbstractLaunchOption;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleModel;
import org.etlforge.integration.client.ui.project.loading.LoadingIssuesPresenter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;

public final class DownloadProjectLaunchOption extends AbstractLaunchOption {
   private final ProjectManager projectManager;
   private volatile IntegrationProject project;
   private final DownloadProjectConsoleFactory consoleFactory;
   private DownloadProjectConsole console;
   private ProjectConsoleModel consoleModel;
   private boolean hasBeenActivated;
   private final KongaLayeredPane layeredPane;

   public DownloadProjectLaunchOption(ApplicationWindow var1, ProjectManager var2, DownloadProjectConsoleFactory var3) {
      super(EnterpriseInterchangeResources.getString("Launcher.Download.Name"), ProjectDownloadResources.ICONS.getIcon("Download.32"));
      this.projectManager = var2;
      this.consoleFactory = var3;
      this.console = this.createConsole();
      this.layeredPane = new KongaLayeredPane();
      this.console.setWaitService(this.layeredPane);
   }

   private DownloadProjectConsole createConsole() {
      this.consoleModel = new ProjectConsoleModel();
      DownloadProjectConsole var1 = this.consoleFactory.newConsole(this.consoleModel, new DownloadProjectLaunchOption.DownloadCallbackImpl(), null);
      var1.setDownloadAutomatically(false);
      var1.setDownloadProjectActionName(EnterpriseInterchangeResources.getString("Launcher.Download"));
      return var1;
   }

   @Override
   public void activated() {
      if (this.hasBeenActivated) {
         this.console.requestFocus();
      } else {
         this.hasBeenActivated = true;
         this.console.downloadProjectList();
      }
   }

   @Override
   public void hibernate() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (DownloadProjectLaunchOption.this.console != null) {
               DownloadProjectLaunchOption.this.consoleModel.removeProjects();
               DownloadProjectLaunchOption.this.console.dispose();
               DownloadProjectLaunchOption.this.layeredPane.setBaseComponent(null);
               DownloadProjectLaunchOption.this.project = null;
               DownloadProjectLaunchOption.this.console = null;
               DownloadProjectLaunchOption.this.consoleModel = null;
               DownloadProjectLaunchOption.this.hasBeenActivated = false;
            }
         }
      });
   }

   @Override
   public void wakeUp() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DownloadProjectLaunchOption.this.console = DownloadProjectLaunchOption.this.createConsole();
            DownloadProjectLaunchOption.this.layoutConsole();
            UiUtils.revalidate(DownloadProjectLaunchOption.this.layeredPane);
         }
      });
   }

   @Override
   public IntegrationProject getProject() {
      return this.project;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.layoutConsole();
      var1.display(this.layeredPane);
   }

   private void layoutConsole() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
      JLabel var2 = this.createCaption();
      var1.north(var2).center(this.console);
      var1.withBackground(Colors.NEUTRAL_BACKGROUND);
      this.layeredPane.setBaseComponent(var1.container());
   }

   @Override
   public void requestFocus() {
      this.console.requestFocus();
   }

   private class DownloadCallbackImpl extends ProjectDownloaderCallback.Adapter {
      private DownloadCallbackImpl() {
      }

      @Override
      public void downloadCompleted(ProjectLocation var1) {
         try {
            DownloadProjectLaunchOption.this.project = this.reloadProject(var1);
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  DownloadProjectLaunchOption.this.launchProject();
               }
            });
         } catch (Exception var3) {
            this.reportError(var3);
         }
      }

      private void reportError(Exception var1) {
         ErrorLog.attention(
            DownloadProjectLaunchOption.class, EnterpriseInterchangeResources.getFormattedMessage("Launcher.Download.Error", var1.getMessage()), var1
         );
      }

      private IntegrationProject reloadProject(ProjectLocation var1) throws Exception {
         IntegrationProject var2 = DownloadProjectLaunchOption.this.projectManager
            .loadProject(var1.getProjectName(), var1.getLocation(), new LoadingIssuesPresenter());
         this.setInitialUiState(var2);
         return var2;
      }

      private void setInitialUiState(IntegrationProject var1) {
         ManagedProject var2 = var1.getExtensionObject(ManagedProject.class);
         ProjectUiState var3 = var2.getUiState();
         var3.setExpandedOnStartUp(ProjectUtils.getRootFolderDescriptors(var2.getProject()));
      }
   }
}
