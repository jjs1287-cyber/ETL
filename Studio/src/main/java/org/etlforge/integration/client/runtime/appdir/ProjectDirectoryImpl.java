package org.etlforge.integration.client.runtime.appdir;

import java.io.File;
import org.etlforge.application.appdir.AppDirInitializationException;
import org.etlforge.application.appdir.ApplicationDirectoryBase;
import org.etlforge.integration.client.project.ProjectDirectory;

public final class ProjectDirectoryImpl extends ApplicationDirectoryBase implements ProjectDirectory {
   public ProjectDirectoryImpl(File var1) throws AppDirInitializationException {
      super(var1, "projects");
   }

   @Override
   public String getSuggestedNewProjectName() {
      return this.getSuggestedNewProjectName(this.getLocation());
   }

   @Override
   public String getSuggestedNewProjectName(File var1) {
      String var2 = PackageResources.ProjectDir.NEW_PROJECT_NAME_BASE;
      File var3 = new File(var1, var2);

      for (int var4 = 2; var3.exists(); var4++) {
         var2 = PackageResources.ProjectDir.getNewProjectName(var4);
         var3 = new File(var1, var2);
      }

      return var2;
   }
}
