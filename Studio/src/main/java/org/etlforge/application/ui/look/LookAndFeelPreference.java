package org.etlforge.application.ui.look;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;

public final class LookAndFeelPreference {
   public static final EnumPreference<SupportedLookAndFeel> INSTANCE = create();

   public static SupportedLookAndFeel get() {
      return INSTANCE.get();
   }

   private static EnumPreference<SupportedLookAndFeel> create() {
      String[] var0 = new String[]{"General", "UI"};
      String var1 = "LaF";
      return ApplicationConfiguration.getPreferenceFactory().newEnumPreference(var0, var1, SupportedLookAndFeel.getDefault(), SupportedLookAndFeel.class);
   }

   private LookAndFeelPreference() {
   }
}
