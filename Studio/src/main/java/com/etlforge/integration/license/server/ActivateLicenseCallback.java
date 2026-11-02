package com.etlforge.integration.license.server;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;

public interface ActivateLicenseCallback extends ServerCallback {
   void licenseWasActivated(ServerInfo var1);

   void activationError(String var1);

   void invalidCredentials(InvalidCredentialsException var1);
}
