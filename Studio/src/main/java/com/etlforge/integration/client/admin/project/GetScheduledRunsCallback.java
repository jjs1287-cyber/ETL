package com.etlforge.integration.client.admin.project;

import java.util.Date;
import java.util.List;
import org.etlforge.integration.client.scheduler.ScheduledRun;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;

public interface GetScheduledRunsCallback extends ServerCallback {
   void scheduledRunsDownloaded(ServerInfo var1, Date var2, List<ScheduledRun> var3);
}
