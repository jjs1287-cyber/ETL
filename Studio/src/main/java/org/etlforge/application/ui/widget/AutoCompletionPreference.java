package org.etlforge.application.ui.widget;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;
import org.etlforge.application.prefs.PreferenceFactory;
import org.etlforge.ui.text.contentassist.AutoCompletionMode;

public final class AutoCompletionPreference {
   private static final EnumPreference<AutoCompletionMode> pref = createPref();

   public static EnumPreference<AutoCompletionMode> instance() {
      return pref;
   }

   public static boolean isAutomatic() {
      return pref.get() == AutoCompletionMode.AUTOMATIC;
   }

   public static boolean isCodeEditorStyle() {
      return pref.get() == AutoCompletionMode.CODE_EDITOR;
   }

   private static EnumPreference<AutoCompletionMode> createPref() {
      String[] var0 = new String[]{"Edit", "Page"};
      String var1 = "AutoCompletionMode";
      PreferenceFactory var2 = ApplicationConfiguration.getPreferenceFactory();
      return var2.newEnumPreference(var0, var1, AutoCompletionMode.AUTOMATIC, AutoCompletionMode.class);
   }

   private AutoCompletionPreference() {
   }
}
