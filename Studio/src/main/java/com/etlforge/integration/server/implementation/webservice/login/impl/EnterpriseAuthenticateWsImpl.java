package com.etlforge.integration.server.implementation.webservice.login.impl;

import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.Authenticate;
import org.etlforge.integration.client.server.login.AuthenticateResult;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.server.implementation.webservice.interchange.authenticate.impl.AuthenticateWsImpl;

public final class EnterpriseAuthenticateWsImpl implements Authenticate {
   private final CppWebServiceConfiguration config;

   public EnterpriseAuthenticateWsImpl(CppWebServiceConfiguration var1, ServerManager var2) {
      this.config = var1;
   }

   @Override
   public AuthenticateResult login(LoginCredentials var1) throws InvalidCredentialsException, IntegrationServerException {
      AuthenticateWsImpl var2 = new AuthenticateWsImpl(this.config);
                                                                                
      return var2.login(var1);
   }
}
