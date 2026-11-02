package com.etlforge.integration.client.deploy.api;

import org.etlforge.integration.client.server.ServerCallback;

public interface ServerDeployCheckCallback extends ServerCallback {
   void deployCheckComplete(ServerDeployCheckResult var1);
}
