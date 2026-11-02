package org.etlforge.integration.activity.client;

import java.util.Date;
import org.etlforge.integration.activity.client.filter.ActivityEntryFilter;

public interface ActivityEntry {
   long getID();

   boolean occurredWithinTimeWindow(Date var1, Date var2);

   boolean passes(ActivityEntryFilter var1);
}
