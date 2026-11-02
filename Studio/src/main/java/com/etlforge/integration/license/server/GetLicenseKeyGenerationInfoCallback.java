package com.etlforge.integration.license.server;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;

public interface GetLicenseKeyGenerationInfoCallback extends ServerCallback {
   void licenseKeyGenerationInfoReceived(LicenseKeyGenerationInfo var1);

   void invalidCredentials(InvalidCredentialsException var1);
}
