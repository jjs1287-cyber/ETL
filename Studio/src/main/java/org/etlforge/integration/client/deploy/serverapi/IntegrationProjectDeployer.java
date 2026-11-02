package org.etlforge.integration.client.deploy.serverapi;

import org.etlforge.integration.client.server.ServerCall;

public interface IntegrationProjectDeployer extends ServerCall {
   void deployProject(DeployData var1, DeployCallback var2);

   void uploadJitterPack(JitterPackServerBackup var1, UploadJitterPackCallback var2);
}
