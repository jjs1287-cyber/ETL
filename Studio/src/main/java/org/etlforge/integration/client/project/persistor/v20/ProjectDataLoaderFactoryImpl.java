package org.etlforge.integration.client.project.persistor.v20;

import com.google.common.base.Preconditions;
import java.util.logging.Logger;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.client.project.ProjectDataLoader;
import org.etlforge.integration.client.project.ProjectDataLoaderFactory;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.version.UnsupportedVersionException;
import org.etlforge.util.version.Version;

public final class ProjectDataLoaderFactoryImpl implements ProjectDataLoaderFactory {
   private Logger logger = Logger.getAnonymousLogger();

   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1, "logger");
   }

   @Override
   public ProjectDataLoader newLoader(IntegrationProject var1, ProjectFile var2) throws UnsupportedVersionException {
      checkVersion(var2);
      return new ProjectDataLoaderImpl(var1, var2, this.logger);
   }

   private static void checkVersion(ProjectFile var0) throws UnsupportedVersionException {
      Version var1 = var0.getVersionSavedWith();
      Version var2 = VersionFactory.newVersion(2, 0, 0, 5);
      if (var1.isEarlierThan(var2)) {
         throw new UnsupportedVersionException(
            var1, "The project was saved in an early version of ETLForge, that is no longer supported. Please contact ETLForge Support."
         );
      }
   }
}
