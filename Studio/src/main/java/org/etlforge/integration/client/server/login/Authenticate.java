package org.etlforge.integration.client.server.login;

import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCall;

public interface Authenticate extends ServerCall {
   AuthenticateResult login(LoginCredentials var1) throws InvalidCredentialsException, IntegrationServerException;
}
