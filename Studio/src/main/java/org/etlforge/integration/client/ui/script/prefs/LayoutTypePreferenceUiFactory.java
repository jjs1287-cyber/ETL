package org.etlforge.integration.client.ui.script.prefs;

import org.etlforge.application.ui.prefs.FixedSetPreferenceUi;
import org.etlforge.application.ui.prefs.PreferenceUi;
import org.etlforge.application.ui.prefs.TitledPreferenceUi;
import org.etlforge.integration.client.ui.script.builder.layout.LayoutType;

public final class LayoutTypePreferenceUiFactory {
   public static PreferenceUi build(String var0, String var1) {
      PreferenceUi var2 = createSelector();
      return new TitledPreferenceUi(var2, var0, var1);
   }

   private static PreferenceUi createSelector() {
      return new FixedSetPreferenceUi<LayoutType>(LayoutTypePreference.INSTANCE, null, false) {
         protected boolean include(LayoutType var1) {
            return var1.isSupported();
         }
      };
   }

   private LayoutTypePreferenceUiFactory() {
   }
}
