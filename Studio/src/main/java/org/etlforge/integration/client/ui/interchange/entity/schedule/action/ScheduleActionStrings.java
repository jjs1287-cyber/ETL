package org.etlforge.integration.client.ui.interchange.entity.schedule.action;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;

public class ScheduleActionStrings extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.name("action.ic.TestSchedule"), "Test Schedule"},
      {ActionDictionaryKeys.tooltip("action.ic.TestSchedule"), "Test this schedule definition by simulating when it will run on the server"}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
