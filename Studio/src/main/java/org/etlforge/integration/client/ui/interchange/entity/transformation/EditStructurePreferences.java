package org.etlforge.integration.client.ui.interchange.entity.transformation;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class EditStructurePreferences {
   private static final String[] PATH = new String[]{"Transformation", "Edit Structures"};
   public static final BooleanPreference CREATE_BACKUP = ApplicationConfiguration.getPreferenceFactory()
      .newBooleanPreference(PATH, "CreateBackup", Boolean.TRUE);
   public static final BooleanPreference AUTOMATIC_MIGRATION = ApplicationConfiguration.getPreferenceFactory()
      .newBooleanPreference(PATH, "AutomaticMigration", Boolean.TRUE);

   private EditStructurePreferences() {
   }
}
