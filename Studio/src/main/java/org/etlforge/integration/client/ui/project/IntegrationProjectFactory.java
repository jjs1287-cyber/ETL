package org.etlforge.integration.client.ui.project;

import java.io.File;
import org.etlforge.integration.client.project.ProjectLoadingCancelledException;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.ui.project.loading.LoadingIssuesPresenter;
import org.etlforge.integration.client.ui.project.location.ProjectLocationBrowser;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.util.version.UnsupportedVersionException;

public final class IntegrationProjectFactory {
   private final ProjectManager projectManager;

   public IntegrationProjectFactory(ProjectManager var1) {
      this.projectManager = var1;
   }

   public IntegrationProject createNewProject(String var1, File var2) throws IntegrationProjectFactoryException {
      try {
         IntegrationProjectLock.acquire();
         IntegrationProject var3 = this.projectManager.createNewProject(var1, var2);
         ProjectLocationBrowser.setNewProjectLocation(var2.getParentFile());
         return var3;
      } catch (Exception var8) {
         throw convert(var1, var2, var8);
      } finally {
         IntegrationProjectLock.release();
      }
   }

   public IntegrationProject loadExistingProject(String var1, File var2) throws IntegrationProjectFactoryException, ProjectLoadingCancelledException, UnsupportedVersionException {
      try {
         IntegrationProjectLock.acquire();
         return this.projectManager.loadProject(var1, var2, new LoadingIssuesPresenter());
      } catch (Exception var8) {
         throw convert(var1, var2, var8);
      } finally {
         IntegrationProjectLock.release();
      }
   }

   private static IntegrationProjectFactoryException convert(String var0, File var1, Exception var2) {
      String var3 = "Failed to load the project \"" + var0 + "\" from " + var1.getAbsolutePath() + ".";
      String var4 = var2.getMessage();
      if (var4 != null && var4.length() > 0) {
         var3 = var3 + "\n\nReason: " + var4;
      }

      return new IntegrationProjectFactoryException(var3, var2);
   }

   public IntegrationProject loadExistingProject(ProjectLocation var1) throws IntegrationProjectFactoryException, ProjectLoadingCancelledException, UnsupportedVersionException {
      return this.loadExistingProject(var1.getProjectName(), var1.getLocation());
   }
}
