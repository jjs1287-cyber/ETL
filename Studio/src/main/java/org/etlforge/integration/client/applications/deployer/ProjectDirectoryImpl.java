package org.etlforge.integration.client.applications.deployer;

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
      throw new UnsupportedOperationException();
   }

   @Override
   public String getSuggestedNewProjectName(File var1) {
      throw new UnsupportedOperationException();
   }
}
