package com.etlforge.integration.client.admin.project;

import java.util.List;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;

public interface DeleteProjectsCallback extends ServerCallback {
   void projectsWereDeleted(List<DeleteIntegrationProjectResult> var1, ServerInfo var2);
}
