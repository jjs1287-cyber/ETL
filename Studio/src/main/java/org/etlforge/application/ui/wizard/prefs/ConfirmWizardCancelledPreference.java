package org.etlforge.application.ui.wizard.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.prefs.PreferenceFactory;

public final class ConfirmWizardCancelledPreference {
   private static final BooleanPreference INSTANCE = createPref();

   public static BooleanPreference instance() {
      return INSTANCE;
   }

   public static boolean cancelSilently() {
      return !instance().get();
   }

   private static BooleanPreference createPref() {
      String[] var0 = new String[]{"Wizards"};
      String var1 = "ConfirmCancel";
      PreferenceFactory var2 = ApplicationConfiguration.getPreferenceFactory();
      return var2.newBooleanPreference(var0, var1, Boolean.TRUE);
   }

   private ConfirmWizardCancelledPreference() {
   }
}
