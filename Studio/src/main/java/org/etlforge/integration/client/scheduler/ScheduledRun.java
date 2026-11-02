package org.etlforge.integration.client.scheduler;

import java.util.Date;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;

public interface ScheduledRun {
   Date getTimeOfRun();

   Schedule getSchedule();

   Operation getOperation();
}
