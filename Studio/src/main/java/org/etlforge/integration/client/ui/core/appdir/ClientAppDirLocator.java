package org.etlforge.integration.client.ui.core.appdir;

import java.io.File;
import org.etlforge.application.NonInteractiveMode;
import org.etlforge.application.appdir.AppDirLocationProvider;
import org.etlforge.application.appdir.NonInteractiveAppDirLocator;
import org.etlforge.integration.client.runtime.appdir.ApplicationDirectoryLocationPreference;

public class ClientAppDirLocator implements AppDirLocationProvider {
   private final AppDirLocationProvider impl = NonInteractiveMode.on() ? new NonInteractiveAppDirLocator() : new InteractiveAppDirLocator();

   @Override
   public File getLocation() {
      File var1 = ApplicationDirectoryLocationPreference.get();
      if (var1 == null || !var1.exists() || !var1.isDirectory()) {
         var1 = this.askForPath();
         if (var1 != null) {
            ApplicationDirectoryLocationPreference.set(var1);
         }
      }

      return var1;
   }

   private File askForPath() {
      return this.impl.getLocation();
   }
}
