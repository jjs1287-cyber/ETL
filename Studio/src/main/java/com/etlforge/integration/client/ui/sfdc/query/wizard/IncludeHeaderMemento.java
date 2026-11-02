package com.etlforge.integration.client.ui.sfdc.query.wizard;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class IncludeHeaderMemento {
   private static final String PREF_KEY = "IncludeHeader";

   public static void restore(Wizard var0, KongaCheckBox var1) {
      boolean var2 = var0.getPreferences().getBoolean("IncludeHeader", true);
      var1.setSelected(var2);
   }

   public static void store(Wizard var0, KongaCheckBox var1) {
      boolean var2 = var1.isSelected();
      var0.getPreferences().putBoolean("IncludeHeader", var2);
   }

   private IncludeHeaderMemento() {
   }
}
