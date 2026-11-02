package org.etlforge.integration.client.ui.interchange.util.dialog;

import java.text.MessageFormat;
import org.etlforge.integration.client.ui.interchange.resources.InterchangeResources;

final class PackageResources {
   private static String getString(String var0) {
      return InterchangeResources.STRINGS.getString(var0);
   }

   private static String getFormattedMessage(String var0, Object[] var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private PackageResources() {
      throw new UnsupportedOperationException();
   }

   static class EntitySearch {
      static final String TITLE = PackageResources.getString("Dialog.EntitySearch.Title");
   }

   static class NewProject {
      static final String TITLE = PackageResources.getString("Dialog.NewProject.Title");
      static final String INVALID_PROJECT_NAME = PackageResources.getString("Dialog.NewProject.InvalidProjectName");
      static final String LOCATION_EXISTS_TITLE = PackageResources.getString("Dialog.NewProject.Message.LocationExists.Title");

      static String getLocationExistsMessage(String var0) {
         return PackageResources.getFormattedMessage("Project.LocationExists", new Object[]{var0});
      }
   }
}
