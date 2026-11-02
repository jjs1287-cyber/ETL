package org.etlforge.integration.client.ui.interchange.entity.project.loader;

import java.io.File;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.ui.project.IntegrationProjectFactory;
import org.etlforge.integration.client.ui.project.IntegrationProjectFactoryException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.wait.WaitLock;

public final class NewProjectCreator implements ApplicationWorker.SafeRunnable {
   private final ProjectManager projectManager;
   private final ProjectLocation projectLoc;
   private final WaitLock waitLock;

   public NewProjectCreator(ProjectManager var1, ProjectLocation var2, WaitLock var3) {
      this.projectManager = var1;
      this.projectLoc = var2;
      this.waitLock = var3;
   }

   @Override
   public void run() {
      if (this.closeCurrentProject()) {
         this.loadNewProject();
      }
   }

   private boolean closeCurrentProject() {
      try {
         this.projectManager.closeCurrentProject();
         return true;
      } catch (Exception var2) {
         this.waitLock.release();
         return false;
      }
   }

   private void loadNewProject() {
      new NewProjectCreator.ProjectLoader(this.projectManager, this.projectLoc.getProjectName(), this.projectLoc.getLocation(), this.waitLock).run();
   }

   private static class ProjectLoader implements Runnable {
      private final ProjectManager projectManager;
      private final String projectName;
      private final File location;
      private final WaitLock waitLock;

      public ProjectLoader(ProjectManager var1, String var2, File var3, WaitLock var4) {
         this.projectManager = var1;
         this.projectName = var2;
         this.location = var3;
         this.waitLock = var4;
      }

      @Override
      public void run() {
         IntegrationProject var1 = this.createNewProject();
         if (var1 != null) {
            this.projectManager.setCurrentProject(var1);
         }
      }

      private IntegrationProject createNewProject() {
         try {
            IntegrationProjectFactory var1 = new IntegrationProjectFactory(this.projectManager);
            return var1.createNewProject(this.projectName, new File(this.location, this.projectName));
         } catch (IntegrationProjectFactoryException var6) {
            this.waitLock.release();
            ErrorLog.attention(DialogBasedNewProjectLoader.class, "Failed to create new project.", var6);
         } finally {
            this.waitLock.release();
         }

         return null;
      }
   }
}
