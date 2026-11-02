package org.etlforge.integration.client.ui.project.location;

import java.io.File;
import org.etlforge.util.preference.KongaPreferences;

public final class ProjectLocationPreference {
   private static final String NEW_PROJECT_LOCATION_KEY = "NewProjectsDirectory";
   private static final String EXISTING_PROJECT_LOCATION_KEY = "ExistingProjectsDirectory";
   private static final KongaPreferences PREFERENCES = KongaPreferences.getPreferences(ProjectLocationBrowser.class);

   public static File getNewProjectLocation() {
      return PREFERENCES.getFile("NewProjectsDirectory", null);
   }

   static void setNewProjectLocation(File var0) {
      PREFERENCES.putFile("NewProjectsDirectory", var0);
   }

   static File getExistingProjectLocation() {
      return PREFERENCES.getFile("ExistingProjectsDirectory", null);
   }

   static void setExistingProjectLocation(File var0) {
      PREFERENCES.putFile("ExistingProjectsDirectory", var0);
   }

   private ProjectLocationPreference() {
   }
}
