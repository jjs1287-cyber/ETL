package org.etlforge.integration.client.ui.interchange.entity.transformation.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;

public final class FormulaBuilderDisplayModePreference {
   public static final EnumPreference<FormulaBuilderDisplayMode> INSTANCE = create();

   public static FormulaBuilderDisplayMode get() {
      return INSTANCE.get();
   }

   private static EnumPreference<FormulaBuilderDisplayMode> create() {
      String[] var0 = new String[]{"Transformations"};
      String var1 = "FormulaBuilderMode";
      FormulaBuilderDisplayMode var2 = FormulaBuilderDisplayMode.DIALOG;
      return ApplicationConfiguration.getPreferenceFactory().newEnumPreference(var0, var1, var2, FormulaBuilderDisplayMode.class);
   }

   private FormulaBuilderDisplayModePreference() {
   }
}
