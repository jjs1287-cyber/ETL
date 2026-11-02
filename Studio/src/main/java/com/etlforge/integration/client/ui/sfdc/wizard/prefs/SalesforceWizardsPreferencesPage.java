package com.etlforge.integration.client.ui.sfdc.wizard.prefs;

import javax.swing.JLabel;
import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.application.ui.prefs.PreferenceUiGroup;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.wizard.prefs.ConfirmWizardCancelledPreference;

public final class SalesforceWizardsPreferencesPage extends AbstractPreferencesPage {
   public static final String PATH = "Salesforce Wizards";

   public SalesforceWizardsPreferencesPage() {
      this(false);
   }

   public SalesforceWizardsPreferencesPage(boolean var1) {
      super(new String[]{"Salesforce Wizards"});
      this.addControl(this.createDataOptions());
      this.addControl(new NameDisplayPreferences(ObjectNameDisplayPreference.INSTANCE, FieldNameDisplayPreference.INSTANCE));
      if (var1) {
         this.addControl(new BooleanPreferenceUi(ConfirmWizardCancelledPreference.instance(), "Confirm before processing a cancel request"));
      }
   }

   private PreferenceUiGroup createDataOptions() {
      PreferenceUiGroup var1 = new PreferenceUiGroup(
         "Data Options",
         new BooleanPreferenceUi(ReadFilesAsUTF8Preference.INSTANCE, "Read source files as UTF-8"),
         new BooleanPreferenceUi(WriteFilesAsUTF8Preference.INSTANCE, "Write target files as UTF-8"),
         new TimeZonePreferenceUi(TimeZonePreference.INSTANCE),
         new BooleanPreferenceUi(EuropeanDateFormatPreference.INSTANCE, "Use European date format (dd/mm/yyyy)")
      );
      JLabel var2 = new JLabel(
         "<html><b>These settings only affect new operations that are created.<br>To change an existing operation, double-click on it in the tree, and expand the Advanced Options section.</b></html>"
      );
      var2.setIcon(CommonIcons.WARNING_16);
      var2.setVerticalTextPosition(1);
      var1.setCaption(var2);
      return var1;
   }
}
