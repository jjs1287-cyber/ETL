package org.etlforge.application.ui.window.editor;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.IntPreference;

public final class RecentlyClosedEditorsPreference {
   public static final IntPreference INSTANCE = create();

   private static IntPreference create() {
      String[] var0 = new String[]{"Editors"};
      String var1 = "RecentlyClosedEditorsSize";
      return ApplicationConfiguration.getPreferenceFactory().newIntPreference(var0, var1, 0, 50, 10);
   }

   private RecentlyClosedEditorsPreference() {
   }
}
