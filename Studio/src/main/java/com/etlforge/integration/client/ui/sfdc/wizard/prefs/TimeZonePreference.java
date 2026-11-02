package com.etlforge.integration.client.ui.sfdc.wizard.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.Preference;
import org.etlforge.integration.salesforce.bulkload.TimeZoneId;

public final class TimeZonePreference extends Preference<TimeZoneId> {
   public static final TimeZonePreference INSTANCE = new TimeZonePreference(
      ApplicationConfiguration.getPreferenceFactory().getApplicationClass(), TimeZoneId.UTC
   );

   public TimeZonePreference(Class<?> var1, TimeZoneId var2) {
      super(var1, new String[]{"Salesforce Wizards"}, "TimeZone", var2);
   }

   public TimeZoneId getTimeZoneId() {
      TimeZoneId var1 = this.getDefaultValue();
      String var2 = this.getNode().get(this.getKey(), var1.getId());
      return TimeZoneId.valueOf(var2);
   }

   public void setTimeZoneId(TimeZoneId var1) {
      TimeZoneId var2 = this.getTimeZoneId();
      if (!var2.equals(var1)) {
         this.getNode().put(this.getKey(), var1.getId());
         this.fireValueChanged(var2, var1);
      }
   }
}
