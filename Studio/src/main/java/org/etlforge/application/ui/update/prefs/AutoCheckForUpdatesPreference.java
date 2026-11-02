package org.etlforge.application.ui.update.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class AutoCheckForUpdatesPreference extends BooleanPreference {
   public static final AutoCheckForUpdatesPreference INSTANCE = new AutoCheckForUpdatesPreference(
      ApplicationConfiguration.getPreferenceFactory().getApplicationClass()
   );

   public AutoCheckForUpdatesPreference(Class<?> var1) {
      super(var1, "Check for Updates", "AutoCheckForUpdates", Boolean.TRUE);
   }

   @Override
   public boolean get() {
      return super.get();
   }
}
