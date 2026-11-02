package org.etlforge.integration.client.ui.interchange.entity.webservice.prefs;

import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.integration.data.entity.EntityType;

public class WebServiceCallPreferencesPage extends AbstractPreferencesPage {
   public static final String PATH = EntityType.WebServiceCall.getPlural();

   public WebServiceCallPreferencesPage() {
      super(new String[]{PATH});
      this.addControl(
         new BooleanPreferenceUi(
            ShowWebServiceStructuresPreference.INSTANCE,
            "Show the request and response structures in the " + EntityType.WebServiceCall.getDisplayName() + " page and wizard"
         )
      );
      this.addControl(
         new BooleanPreferenceUi(
            WsdlEditSideEffectsWarningPreference.INSTANCE,
            "Show a warning if editing a WSDL file will affect other " + EntityType.WebServiceCall.getPlural() + " in the project"
         )
      );
   }
}
