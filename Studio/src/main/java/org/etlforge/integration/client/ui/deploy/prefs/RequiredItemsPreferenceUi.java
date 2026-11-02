package org.etlforge.integration.client.ui.deploy.prefs;

import org.etlforge.application.ui.prefs.FixedSetPreferenceUi;
import org.etlforge.application.ui.prefs.TitledPreferenceUi;
import org.etlforge.integration.client.deploy.RequiredItemsMode;
import org.etlforge.integration.client.deploy.prefs.DeployRequiredItemsPreference;
import org.etlforge.ui.input.RadioButtonGroupPanel;

public class RequiredItemsPreferenceUi extends TitledPreferenceUi {
   public RequiredItemsPreferenceUi() {
      super(new RequiredItemsPreferenceUi.Impl(), "Single Item Deploy", "When deploying single items, also deploy all items they depend on:");
   }

   private static class Impl extends FixedSetPreferenceUi<RequiredItemsMode> {
      public Impl() {
         super(DeployRequiredItemsPreference.INSTANCE, null, false);
      }

      @Override
      protected RadioButtonGroupPanel.Layout getRadioButtonLayout() {
         return RadioButtonGroupPanel.Layout.ONE_COLUMN;
      }

      protected String getDisplayString(RequiredItemsMode var1) {
         switch (var1) {
            case ALWAYS_DEPLOY:
               return "Always";
            case DEPLOY_IF_NECESSARY:
               return "Only if the required items themselves have changed";
            default:
               return var1.toString();
         }
      }
   }
}
