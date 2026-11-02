package org.etlforge.integration.client.ui.interchange.core.action;

import java.util.ResourceBundle;

final class PackageResources {
   private static final ResourceBundle STRINGS = ResourceBundle.getBundle("org.etlforge.integration.client.ui.interchange.core.action.resources.Strings");

   private static String getString(String var0) {
      return STRINGS.getString(var0);
   }

   static class DeleteConfirmer {
      static final String TITLE = PackageResources.getString("DeleteConfirmer.Title");
      static final String MESSAGE = PackageResources.getString("DeleteConfirmer.Message");
   }
}
