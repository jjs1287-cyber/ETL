package org.etlforge.integration.client.project;

import com.google.common.base.Supplier;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.version.UnsupportedVersionException;

class ProjectInspector {
   private static final AtomicInteger counter = new AtomicInteger();
   private final ProjectLoaderWorker loader;
   private final ProjectVersionHandlerManager upgradeHandler;

   public ProjectInspector(ProjectLoaderWorker var1, ProjectVersionHandlerManager var2) {
      this.loader = var1;
      this.upgradeHandler = var2;
   }

   public ManagedProjectImpl inspect(String var1, File var2) throws InterchangeLoadingException, ProjectLoadingCancelledException, UnsupportedVersionException {
      File var3 = this.createCopy(var1, var2);
      ProjectVersionHandlerManager.Job var4 = this.upgradeHandler.getJob(var1, var3);
      if (!var4.preLoading(UpgradeStrategy.AUTOMATIC)) {
         throw new ProjectLoadingCancelledException();
      }

      ManagedProjectImpl var5 = this.loader.loadProject(var1, var3, ProjectLoadingIssuesHandler.IGNORE, new Supplier<ProjectLoadingTracker>() {
         public ProjectLoadingTracker get() {
            return ProjectLoadingTracker.SILENT;
         }
      });
      var4.postLoading(var5);
      var5.setReadOnly(true);
      return var5;
   }

   private File createCopy(String var1, File var2) throws InterchangeLoadingException {
      File var3 = this.getTempFolderForCopy();

      try {
         FileUtils.copyDirectory(var2, var3);
         return var3;
      } catch (IOException var5) {
         throw new InterchangeLoadingException("Failed to create a temporary copy of the project \"" + var1 + "\" The project cannot be inspected.", var5);
      }
   }

   private File getTempFolderForCopy() {
      int var1 = 0;

      do {
         int var2 = counter.incrementAndGet();
         String var3 = "tempProject_" + var2;
         if (this.isTempFolderAvailable(var3)) {
            return ApplicationTempFolder.newFolder(var3, true);
         }
      } while (++var1 < 1000);

      throw new RuntimeException(
         "The application temp folder contains several folders that could not be deleted. The temp folder is located here: '"
            + ApplicationTempFolder.getAbsolutePath()
            + "'. Please delete as many folders as you can from there and then try again."
      );
   }

   private boolean isTempFolderAvailable(String var1) {
      if (ApplicationTempFolder.exists(var1)) {
         File var2 = ApplicationTempFolder.newFile(var1);

         try {
            FileUtils.deleteDirectory(var2);
         } catch (Exception var4) {
            ErrorLog.internal(this.getClass(), "Failed to delete temp folder to clear way for a project inspection.", var4);
            return false;
         }
      }

      return true;
   }

   public IntegrationProject inspectProjectData(String var1, File var2) throws InterchangeLoadingException, UnsupportedVersionException {
      File var3 = this.createCopy(var1, var2);
      return this.loader.loadProjectDataForInspection(var1, var3);
   }
}
