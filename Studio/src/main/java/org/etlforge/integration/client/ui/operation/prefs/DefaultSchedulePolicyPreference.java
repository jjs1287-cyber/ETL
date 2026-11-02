package org.etlforge.integration.client.ui.operation.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;
import org.etlforge.application.prefs.PreferenceFactory;
import org.etlforge.integration.data.entity.operation.SchedulePolicy;

public final class DefaultSchedulePolicyPreference {
   private static final EnumPreference<SchedulePolicy> INSTANCE = createPref();

   public static EnumPreference<SchedulePolicy> instance() {
      return INSTANCE;
   }

   public static SchedulePolicy get() {
      return instance().get();
   }

   private static EnumPreference<SchedulePolicy> createPref() {
      PreferenceFactory var0 = ApplicationConfiguration.getPreferenceFactory();
      String[] var1 = new String[]{"Operations"};
      String var2 = "SchedulePolicy";
      return var0.newEnumPreference(var1, var2, SchedulePolicy.getDefault(), SchedulePolicy.class);
   }

   private DefaultSchedulePolicyPreference() {
   }
}
