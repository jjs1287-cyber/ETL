package org.etlforge.integration.client.ui.core.appdir;

import java.io.File;
import javax.swing.Icon;
import org.etlforge.integration.client.ui.Resources;
import org.etlforge.integration.client.ui.core.resources.CoreResources;

final class PackageResources {
   private PackageResources() {
      throw new UnsupportedOperationException();
   }

   static class LocationFrame {
      static final String ERROR_TITLE = Resources.getCommonString("Error");
      static final String TITLE = CoreResources.getString("AppDir.LocationFrame.Title");
      static final String CAPTION = CoreResources.getString("AppDir.LocationFrame.Caption");
      static final String EMPTY_PATH = CoreResources.getString("AppDir.LocationFrame.EmptyPath");

      static String getFileIsPathMessage(File var0) {
         return CoreResources.getFormattedMessage("AppDir.LocationFrame.PathIsFile", new Object[]{var0.getAbsolutePath()});
      }

      static String getFailedToCreatePathMessage(File var0) {
         return CoreResources.getFormattedMessage("AppDir.LocationFrame.PathCouldNotBeCreated", new Object[]{var0.getAbsolutePath()});
      }

      static class Browse {
         static final String LABEL = CoreResources.getString("AppDir.LocationFrame.Browse.Label");
         static final Icon ICON = null;
      }
   }
}
