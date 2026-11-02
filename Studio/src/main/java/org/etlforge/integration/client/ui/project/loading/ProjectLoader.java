package org.etlforge.integration.client.ui.project.loading;

import com.google.common.base.Preconditions;
import java.util.logging.Logger;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.ProjectLoadingCancelledException;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.ui.project.IntegrationProjectFactory;
import org.etlforge.integration.client.ui.project.resources.ProjectUiResources;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public final class ProjectLoader {
   private final ProjectManager projectManager;
   private WaitService waitService = WaitService.NONE;

   public ProjectLoader(ProjectManager var1) {
      this.projectManager = Preconditions.checkNotNull(var1, "projectManager");
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public void loadAndDisplay(ProjectLocation var1) {
      if (this.closeCurrentProject()) {
         WaitLock var2 = this.waitService.startWait();
         ProjectLoader.Loader var3 = new ProjectLoader.Loader(var1, var2);
         var3.execute();
      }
   }

   private boolean closeCurrentProject() {
      try {
         this.projectManager.closeCurrentProject();
         return true;
      } catch (CloseProjectVetoException var2) {
         return false;
      }
   }

   private class Loader extends KongaSwingWorker<IntegrationProject, Void> {
      private final ProjectLocation projectLocation;
      private final WaitLock waitLock;

      public Loader(ProjectLocation var2, WaitLock var3) {
         this.projectLocation = var2;
         this.waitLock = var3;
         this.doNotLogErrors();
      }

      protected IntegrationProject doInBackground() throws Exception {
         IntegrationProjectFactory var1 = new IntegrationProjectFactory(ProjectLoader.this.projectManager);
         return var1.loadExistingProject(this.projectLocation);
      }

      protected void onSuccess(IntegrationProject var1) {
         try {
            ProjectLoader.this.projectManager.setCurrentProject(var1);
         } finally {
            this.waitLock.release();
         }
      }

      @Override
      protected void onFailure(Throwable var1) {
         try {
            this.logError(var1 instanceof Exception ? (Exception)var1 : new Exception(var1));
         } finally {
            this.waitLock.release();
         }
      }

      private void logError(Exception var1) {
         Logger var2 = ApplicationLog.getMainLogger();
         if (var1 instanceof ProjectLoadingCancelledException) {
            var2.info("The user cancelled the loading of the project \"" + this.projectLocation.getProjectName() + "\"");
         } else {
            var2.throwing(this.getClass().getName(), "loadProject", var1);
            ErrorLog.attention(this.getClass(), ProjectUiResources.getString("Failed to load project."), var1);
         }
      }
   }
}
