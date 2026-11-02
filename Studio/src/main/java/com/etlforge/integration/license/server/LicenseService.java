package com.etlforge.integration.license.server;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.client.server.login.LoginCredentials;

public interface LicenseService extends ServerCall {
   void getLicenseKeyGenerationInfo(LoginCredentials var1, GetLicenseKeyGenerationInfoCallback var2);

   void getLicenseAgreement(String var1, LoginCredentials var2, GetLicenseAgreementCallback var3);

   void activateLicense(String var1, LoginCredentials var2, ActivateLicenseCallback var3);

   void checkLicense(LoginCredentials var1, CheckLicenseCallback var2);
}
