package org.etlforge.integration.server.implementation.webservice.interchange.authenticate.impl;

import com.google.common.base.Preconditions;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisProperties;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerAddress;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.login.Authenticate;
import org.etlforge.integration.client.server.login.AuthenticateResult;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.login.LoginKeyValuesHandler;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.server.implementation.webservice.WebServiceMessages;
import org.etlforge.integration.server.implementation.webservice.WsKeyValue;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;
import org.etlforge.integration.server.implementation.webservice.interchange.authenticate.client.AuthenticateServiceLocator;

public final class AuthenticateWsImpl extends CppWebServiceServerCall implements Authenticate {
   private static final String SERVICE_NAME = "konga_authenticate";
   private volatile LoginKeyValuesHandler keyValuesHandler = LoginKeyValuesHandler.DO_NOTHING;

   public AuthenticateWsImpl(CppWebServiceConfiguration var1) {
      super(var1);
   }

   public void setKeyValuesHandler(LoginKeyValuesHandler var1) {
      Preconditions.checkNotNull(var1);
      this.keyValuesHandler = var1;
   }

   @Override
   public AuthenticateResult login(LoginCredentials var1) throws InvalidCredentialsException, IntegrationServerException {
      ServerAddress var2 = var1.getServerAddress();
      String var3 = var1.getUser();
      String var4 = var1.getPasswordHash();
      String var5 = ClientInfo.getCurrentVersion().toString();
      AuthenticateServiceLocator var6 = new AuthenticateServiceLocator();
      StringBuilder var7 = var1.isUsingHttps() ? new StringBuilder("https://") : new StringBuilder("http://");
      var7.append(var2.toString()).append(this.getConfiguration().getWsPath()).append("konga_authenticate");
      var6.setkonga_authenticateEndpointAddress(var7.toString());

      try {
         WsKeyValuePairs var8 = this.createKeyValuePairsToSendToServer();
         org.etlforge.integration.server.implementation.webservice.interchange.authenticate.client.AuthenticateResult var14 = var6.getkonga_authenticate()
            .loginWithVersionAndKeyValues(var3, var4, var5, var8);
         AuthenticateResultImpl var15 = new AuthenticateResultImpl(var14);
         this.keyValuesHandler.handle(new ServerInfo(var15.serverGuid(), var1.getServer()), var15.keyValues());
         return var15;
      } catch (RemoteException var12) {
         if (InvalidCredentialsException.isInvalidCredentialsError(var12)) {
            throw new InvalidCredentialsException(var12);
         }

         String var9 = var12.getMessage();
         if ("Webservice failed".equals(var9)) {
            String var10 = var9 + ". " + WebServiceMessages.getMessage("Authenticate.UnsupportedServerVersion");
            IntegrationServerException var11 = new IntegrationServerException(var10, var12);
            this.getConfiguration().throwException(var7.toString(), var11);
         } else {
            this.getConfiguration().throwException(var7.toString(), var12);
         }
      } catch (ServiceException var13) {
         this.getConfiguration().throwException(var7.toString(), var13);
      }

      return null;
   }

   private WsKeyValuePairs createKeyValuePairsToSendToServer() {
      return new WsKeyValuePairs(new WsKeyValue[0]);
   }

   static {
      AxisProperties.setProperty("axis.socketSecureFactory", "org.apache.axis.components.net.SunFakeTrustSocketFactory");
   }
}
