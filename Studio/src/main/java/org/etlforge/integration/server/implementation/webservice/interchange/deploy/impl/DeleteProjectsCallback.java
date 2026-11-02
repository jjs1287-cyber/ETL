package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import com.etlforge.integration.client.admin.project.DeleteIntegrationProjectResult;
import java.util.List;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;

public interface DeleteProjectsCallback extends ServerCallback {
   void projectsDeleted(List<DeleteIntegrationProjectResult> var1, ServerInfo var2);
}
