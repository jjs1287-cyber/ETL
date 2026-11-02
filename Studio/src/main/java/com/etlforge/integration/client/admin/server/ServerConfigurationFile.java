package com.etlforge.integration.client.admin.server;

import org.etlforge.integration.client.server.ServerInfo;

public interface ServerConfigurationFile {
   ServerInfo getServerInfo();

   String getContents();
}
