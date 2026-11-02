package com.etlforge.integration.client.ui.interchange.project.share;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.RecentProjects;
import org.etlforge.integration.client.ui.project.browser.ProjectBrowser;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.wait.WaitLock;

public final class ProjectItemImporter implements Disposable {
   private final ApplicationWindow appWin;
   private final ProjectManager projectManager;
   private final ShareProjectDataModel model;
   private final ProjectItemImportUi ui;
   private final EntityImporter worker;

   public ProjectItemImporter(ApplicationWindow var1, ProjectManager var2, ManagedProject var3) {
      this.appWin = var1;
      this.projectManager = var2;
      this.model = new ShareProjectDataModel(var2, var3);
      this.worker = new EntityImporter(this.model);
      this.ui = new ProjectItemImportUi(var1, this, this.model);
      this.worker.setWaitService(this.ui.getWaitService());
   }

   @Override
   public void dispose() {
      this.model.dispose();
   }

   public UiProvider getUi() {
      return this.ui;
   }

   public void selectSourceProject() {
      new ProjectItemImporter.SourceProjectSelector().start();
   }

   public EntityImporter getWorker() {
      return this.worker;
   }

   public void importItem(IntegrationEntity var1, IntegrationEntity var2) {
      this.worker.importItem(var1, var2);
   }

   private class SourceProjectSelector {
      private SourceProjectSelector() {
      }

      public void start() {
         final WaitLock var1 = ProjectItemImporter.this.ui.getWaitService().startWait(125);
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SourceProjectSelector.this.openBrowser(var1);
            }
         });
      }

      private void openBrowser(WaitLock var1) {
         ProjectBrowser var2 = this.createBrowser();
         KongaDialog var3 = this.createDialog(var1, var2);
         this.listenToBrowser(var2, var3);
         var3.setVisible(true);
         var3.removeFromLocationManager();
      }

      private ProjectBrowser createBrowser() {
         RecentProjects var1 = ProjectItemImporter.this.projectManager.getRecentProjects();
         ProjectBrowser var2 = new ProjectBrowser(var1, true);
         var2.setCurrentProjectLocation(ProjectItemImporter.this.projectManager.getCurrentProjectLocation());
         var2.setDefaultSearchFolder(ProjectItemImporter.this.projectManager.getProjectDirectory().getLocation());
         return var2;
      }

      private KongaDialog createDialog(final WaitLock var1, ProjectBrowser var2) {
         KongaDialog var3 = new KongaDialog(ProjectItemImporter.this.appWin.getFrame(), "Select a Project", true) {
            @Override
            protected void onDialogOpened() {
               var1.release();
               super.onDialogOpened();
            }
         };
         var3.standardLayout(var2, null);
         var3.manageLocation(ProjectItemImporter.class);
         return var3;
      }

      private void listenToBrowser(ProjectBrowser var1, final KongaDialog var2) {
         var1.setOpenHandler(new Receiver<ProjectLocation>() {
            public void handle(ProjectLocation var1) {
               var2.dispose();
               if (SourceProjectSelector.this.isValidSelection(var1)) {
                  SourceProjectSelector.this.importProject(var1);
               }
            }
         });
         var1.addCancelListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               var2.dispose();
            }
         });
      }

      private boolean isValidSelection(ProjectLocation var1) {
         if (var1.getLocation().equals(ProjectItemImporter.this.model.getTargetProject().getLocation())) {
            Alert.error("The selected project is the same as the currently open project. Please select a different project.", "Cannot Import Into Itself");
            return false;
         } else {
            return true;
         }
      }

      private void importProject(ProjectLocation var1) {
         WaitLock var2 = ProjectItemImporter.this.ui.getWaitService().startWait(125);
         new SourceProjectLoader(ProjectItemImporter.this.model).load(var1, var2);
      }
   }
}
