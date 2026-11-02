package org.jitterbit.integration.client.dl.migration;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import org.apache.commons.lang.SystemUtils;
import org.jitterbit.application.errorlog.ErrorLog;
import org.jitterbit.integration.client.project.ProjectDirectory;
import org.jitterbit.integration.client.project.ProjectLocation;
import org.jitterbit.integration.client.project.RecentProjects;
import org.jitterbit.integration.client.runtime.PostPlatformInitializedJob;
import org.jitterbit.integration.client.runtime.StartupProgressMonitor;
import org.jitterbit.io.FileUtils;
import org.jitterbit.io.Folder;
import org.jitterbit.system.KeyValue;
import org.jitterbit.util.preference.BooleanKeyValue;

public final class ProjectMigrator implements PostPlatformInitializedJob {
   private static final String HAS_RUN = "hasRun";
   private final ProjectDirectory projectDirectory;
   private final RecentProjects recentProjects;
   private final KeyValue<String, Boolean> history;

   public ProjectMigrator(ProjectDirectory var1, RecentProjects var2) {
      this(var1, var2, new BooleanKeyValue(ProjectMigrator.class));
   }

   public ProjectMigrator(ProjectDirectory var1, RecentProjects var2, KeyValue<String, Boolean> var3) {
      this.projectDirectory = Preconditions.checkNotNull(var1, "projectDirectory");
      this.recentProjects = Preconditions.checkNotNull(var2, "recentProjects");
      this.history = Preconditions.checkNotNull(var3);
   }

   @Override
   public void run(StartupProgressMonitor var1) {
      if (!this.hasAlreadyRun()) {
         try {
            var1.publishMessage("Looking for Data Loader project");
            Optional var2 = this.locateDataLoaderProject();
            if (var2.isPresent()) {
               var1.publishMessage("Copying Data Loader project");
               Folder var3 = this.findDestination((ProjectLocation)var2.get());
               this.copyProject((ProjectLocation)var2.get(), var3);
               this.addToRecentProjects((ProjectLocation)var2.get(), var3);
               return;
            }
         } catch (Exception var7) {
            ErrorLog.log(this.getClass(), "Failed to migrate the Data Loader project.", var7);
            return;
         } finally {
            this.history.set("hasRun", Boolean.TRUE);
         }
      }
   }

   private Optional<ProjectLocation> locateDataLoaderProject() {
      Folder var1 = new Folder(SystemUtils.USER_HOME, ".jitterbit_dataloader");
      DataLoaderProjectLocator var2 = DataLoaderProjectLocator.forApplicationFolder(var1);
      return var2.locateLatestDataLoaderProject();
   }

   private boolean hasAlreadyRun() {
      return this.history.get("hasRun", Boolean.FALSE);
   }

   private Folder findDestination(ProjectLocation var1) {
      String var2 = var1.getProjectName();
      ImmutableSet var3 = ImmutableSet.copyOf(FileUtils.listNames(this.projectDirectory.getLocation()));

      for (int var4 = 1; var4 <= Integer.MAX_VALUE; var4++) {
         String var5 = var4 == 1 ? var2 : var2 + " " + var4;
         if (!var3.contains(var5)) {
            return new Folder(this.projectDirectory.getLocation(), var5);
         }
      }

      throw new RuntimeException("The number of files in " + this.projectDirectory.getLocation() + " is astounding.");
   }

   private void copyProject(ProjectLocation var1, Folder var2) throws IOException {
      Folder var3 = new Folder(var1.getLocation());
      var3.copyTo(var2);
   }

   private void addToRecentProjects(ProjectLocation var1, Folder var2) {
      ProjectLocation var3 = new ProjectLocation(var1.getProjectName(), var2.asJavaFile());
      this.recentProjects.addProjectLocation(var3);
   }
}
