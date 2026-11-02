package org.etlforge.integration.client.ui.script.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;
import org.etlforge.integration.client.ui.script.builder.layout.LayoutType;

public final class LayoutTypePreference {
   public static final EnumPreference<LayoutType> INSTANCE = create();

   public static LayoutType get() {
      LayoutType var0 = INSTANCE.get();
      if (!var0.isSupported()) {
         var0 = INSTANCE.getDefaultValue();
      }

      return var0;
   }

   private static EnumPreference<LayoutType> create() {
      String[] var0 = new String[]{"Script", "UI"};
      String var1 = "Layout";
      LayoutType var2 = LayoutType.TABS;
      assert var2.isSupported();
      return ApplicationConfiguration.getPreferenceFactory().newEnumPreference(var0, var1, var2, LayoutType.class);
   }

   private LayoutTypePreference() {
   }
}
