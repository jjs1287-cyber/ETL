package org.etlforge.integration.client.scheduler.prefs;

import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.IntPreferenceUi;

public class SchedulePreferencesPage extends AbstractPreferencesPage {
   public static final String PATH = "Schedules";

   public SchedulePreferencesPage() {
      super(new String[]{"Schedules"});
      IntPreferenceUi var1 = new IntPreferenceUi(NumberOfSimulatedRunsPreference.INSTANCE, "Number of runs to simulate when performing a Test Schedule:", 4);
      var1.setSelectAllWhenFocused(true);
      this.addControl(var1);
   }
}
