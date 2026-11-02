package org.etlforge.integration.client.ui.interchange.location;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;
import org.etlforge.integration.data.location.DataLocationType;

public class LocationTypePreference {
   private final EnumPreference<DataLocationType> pref;

   public LocationTypePreference(String[] var1, String var2, DataLocationType var3) {
      this.pref = ApplicationConfiguration.getPreferenceFactory().newEnumPreference(var1, var2, var3, DataLocationType.class);
   }

   public EnumPreference<DataLocationType> getPreference() {
      return this.pref;
   }
}
