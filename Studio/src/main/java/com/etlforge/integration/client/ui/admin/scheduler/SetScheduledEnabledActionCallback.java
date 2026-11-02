package com.etlforge.integration.client.ui.admin.scheduler;

import java.util.Collection;
import org.etlforge.integration.data.entity.id.ScheduleId;

public interface SetScheduledEnabledActionCallback {
   void setTalkingToServer(boolean var1);

   void schedulesUpdated(Collection<ScheduleId> var1, boolean var2);
}
