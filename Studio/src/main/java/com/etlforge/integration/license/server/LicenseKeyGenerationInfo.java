package com.etlforge.integration.license.server;

import org.etlforge.integration.client.server.ServerInfo;

public interface LicenseKeyGenerationInfo {
   ServerInfo getServerInfo();

   String getServerInstallationId();
}
