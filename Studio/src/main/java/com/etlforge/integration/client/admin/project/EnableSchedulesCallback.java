package com.etlforge.integration.client.admin.project;

import java.util.Collection;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.data.entity.id.ScheduleId;

public interface EnableSchedulesCallback extends ServerCallback {
   void done(Collection<ScheduleId> var1, boolean var2);
}
