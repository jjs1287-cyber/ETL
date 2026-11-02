package com.etlforge.integration.client.admin.server;

import org.etlforge.integration.client.server.ServerCallback;

public interface ViewServerConfigurationFileCallback extends ServerCallback {
   void configurationFileReceived(ServerConfigurationFile var1);
}
