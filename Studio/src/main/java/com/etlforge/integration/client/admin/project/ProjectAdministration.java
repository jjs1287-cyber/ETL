package com.etlforge.integration.client.admin.project;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.ScheduleId;

public interface ProjectAdministration extends ServerCall {
   void deleteProjects(List<IntegrationProjectId> var1, DeleteProjectsCallback var2);

   void setSchedulesEnabled(Collection<ScheduleId> var1, boolean var2, EnableSchedulesCallback var3);

   void setOperationSchedulesEnabled(Collection<OperationId> var1, boolean var2, EnableOperationSchedulesCallback var3);

   void getScheduledRuns(Date var1, GetScheduledRunsCallback var2);
}
