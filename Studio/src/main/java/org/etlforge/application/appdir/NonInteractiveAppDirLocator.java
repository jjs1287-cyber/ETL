package org.etlforge.application.appdir;

import java.io.File;

public final class NonInteractiveAppDirLocator implements AppDirLocationProvider {
   @Override
   public File getLocation() {
      return ApplicationTempFolder.newUniqueFolder(true);
   }
}
