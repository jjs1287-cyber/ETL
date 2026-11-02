package com.etlforge.integration.license.server;

import com.etlforge.integration.license.LicenseInfo;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.login.LoginCredentials;

public interface CheckLicenseCallback extends ServerCallback {
   void receivedInfo(LicenseInfo var1, LoginCredentials var2);
}
