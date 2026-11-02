package com.etlforge.integration.client.deploy.api;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.project.IntegrationProject;

public interface ServerDeployChecker extends ServerCall {
   void checkProjectForDeploy(IntegrationProject var1, ServerDeployCheckCallback var2);
}
