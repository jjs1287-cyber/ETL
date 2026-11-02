package org.etlforge.integration.client.scheduler.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.IntPreference;

public final class NumberOfSimulatedRunsPreference {
   public static final IntPreference INSTANCE = create();

   public static final int get() {
      return INSTANCE.get();
   }

   private static IntPreference create() {
      String[] var0 = new String[]{"Schedules"};
      String var1 = "TestSchedule.NumberOfRuns";
      return ApplicationConfiguration.getPreferenceFactory().newIntPreference(var0, var1, 1, Integer.MAX_VALUE, 10);
   }

   private NumberOfSimulatedRunsPreference() {
   }
}
