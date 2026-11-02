package org.etlforge.application.ui.window.editor.service;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;

public final class EditorServiceUiModePreference {
   public static final EnumPreference<EditorServiceUiMode> INSTANCE = create();

   public static EditorServiceUiMode get() {
      return INSTANCE.get();
   }

   private static EnumPreference<EditorServiceUiMode> create() {
      String[] var0 = new String[]{"General", "UI"};
      String var1 = "EditorDisplayMode2";
      EditorServiceUiMode var2 = EditorServiceUiMode.MULTI_TABBED;
      return ApplicationConfiguration.getPreferenceFactory().newEnumPreference(var0, var1, var2, EditorServiceUiMode.class);
   }

   private EditorServiceUiModePreference() {
   }
}
