package org.etlforge.integration.client.ui.interchange.entity.schedule.action;

import javax.swing.Action;
import org.etlforge.application.ui.action.popup.SingleItemPopupMenuFactory;
import org.etlforge.integration.data.entity.Schedule;

public final class TestScheduleItemFactory extends SingleItemPopupMenuFactory {
   public TestScheduleItemFactory(Action var1) {
      super(var1, Schedule.class);
   }
}
