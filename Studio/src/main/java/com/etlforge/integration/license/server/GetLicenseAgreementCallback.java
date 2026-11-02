package com.etlforge.integration.license.server;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;

public interface GetLicenseAgreementCallback extends ServerCallback {
   void gotLicenseAgreement(String var1);

   void licenseError(String var1);

   void invalidCredentials(InvalidCredentialsException var1);
}
