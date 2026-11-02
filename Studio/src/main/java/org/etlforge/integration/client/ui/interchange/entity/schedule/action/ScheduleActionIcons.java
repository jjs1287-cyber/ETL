package org.etlforge.integration.client.ui.interchange.entity.schedule.action;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;
import org.etlforge.integration.client.ui.icons.ClientIcons;

public class ScheduleActionIcons extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.smallIcon("action.ic.TestSchedule"), ClientIcons.getIcon("TestSchedule.16")},
      {ActionDictionaryKeys.mediumIcon("action.ic.TestSchedule"), ClientIcons.getIcon("TestSchedule.24")}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
