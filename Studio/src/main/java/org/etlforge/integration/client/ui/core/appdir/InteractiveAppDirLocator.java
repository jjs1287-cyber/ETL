package org.etlforge.integration.client.ui.core.appdir;

import java.io.File;
import org.etlforge.application.appdir.AppDirLocationProvider;

public class InteractiveAppDirLocator implements AppDirLocationProvider {
   @Override
   public File getLocation() {
      return AppDirLocationFrame.getDirectory();
   }
}
