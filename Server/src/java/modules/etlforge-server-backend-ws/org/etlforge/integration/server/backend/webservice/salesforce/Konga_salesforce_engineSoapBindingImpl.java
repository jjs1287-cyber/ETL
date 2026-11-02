package org.etlforge.integration.server.backend.webservice.salesforce;

import com.google.common.base.Strings;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import org.etlforge.integration.common.server.ServerErrors;
import org.etlforge.integration.salesforce.LoginCredentials;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceConnection;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.SalesforceVersion;
import org.etlforge.integration.salesforce.ServerHost;
import org.etlforge.integration.salesforce.wsdl.partner.wsc.WscPartnerConnectionFactory;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.ServerDebugLog;
import org.etlforge.integration.server.crypto.Crypto;
import org.etlforge.integration.server.db.trandb.ProxySettingsSupplier;
import org.etlforge.integration.server.db.trandb.SalesforceConnectorsTab;
import org.etlforge.integration.server.db.trandb.SalesforceConnectorsTabRow;
import org.etlforge.integration.server.ws.WsUtils;

public class Konga_salesforce_engineSoapBindingImpl implements SalesforceEngine {
   private static final WsSalesforceEngineSessionParams dummySessions = new WsSalesforceEngineSessionParams(null, null, null, null);
   private static final WsSalesforceEngineError dummyError = new WsSalesforceEngineError(0, null, null);

   @Override
   public WsSalesforceEngineLoginResult loginToSalesforce(String var1, String var2, WsSalesforceEngineLoginParams var3) throws RemoteException {
      ExternalApiSupport var4 = null;
      WsSalesforceEngineLoginResult var5 = null;

      try {
         var4 = ExternalApiSupport.forCall(var1, var2);
         Connection var6 = var4.getTranDbConnection();
         long var7 = var3.getSalesforceConnectorId();
         OrgIdentifier var9 = null;
         if (var7 > 0L && !LoginCredentials.OAUTH.equals(var3.getAuthType())) {
            SalesforceConnectorsTab var10 = new SalesforceConnectorsTab(var6);
            SalesforceConnectorsTabRow var11 = null;

            try {
               int var12 = (int)var7;
               var11 = var10.query(var12);
            } catch (SQLException var20) {
               return new WsSalesforceEngineLoginResult(dummySessions, new WsSalesforceEngineError(-1, "Fatal Error: Cannot access etlforge database.", null));
            }

            if (var11 == null) {
               String var28 = "Fatal Error: Cannot find the salesforce credential from etlforge database server with saleforceConnectID=." + var7;
               return new WsSalesforceEngineLoginResult(dummySessions, new WsSalesforceEngineError(-2, var28, null));
            }

            var9 = this.getOrgId(var11);
         } else {
            var9 = this.unmarshalOrgId(var3);
         }

         WscPartnerConnectionFactory var25 = new WscPartnerConnectionFactory();
         var25.setProxySettingsSupplier(new ProxySettingsSupplier(var6));
         var25.setLogger(ServerDebugLog.LOG);
         SalesforceConnection var27 = var25.newConnection(var9);

         try {
            var27.open();
            WsSalesforceEngineSessionParams var29 = new WsSalesforceEngineSessionParams();
            var29.setSessionId(var27.getSessionId());
            var29.setUserId(var27.getUserId());
            var29.setServiceEndpoint(var27.getServiceEndpoint());
            var29.setVersion(var9.getVersion().getId());
            var5 = new WsSalesforceEngineLoginResult(var29, dummyError);
         } catch (SalesforceException var19) {
            var5 = new WsSalesforceEngineLoginResult(dummySessions, this.createForException(1, var19, "loginToSalesforce"));
         }
      } catch (Exception var21) {
         var5 = new WsSalesforceEngineLoginResult(dummySessions, this.createForException(-2, var21, "loginToSalesforce"));
      } finally {
         ExternalApiSupport.safeDispose(var4);
      }

      return var5;
   }

   private OrgIdentifier getOrgId(SalesforceConnectorsTabRow var1) {
      if (LoginCredentials.OAUTH.equals(var1.get_auth_type())) {
         LoginCredentials credentials = new LoginCredentials("", "", "", var1.get_sandbox() != 0, LoginCredentials.OAUTH,
            var1.get_instance_url(), var1.get_client_id(), Crypto.decryptString(var1.get_client_secret()));
         return new OrgIdentifier(ServerHost.fromUrl(var1.get_server_host()), SalesforceVersion.fromId(var1.get_api_version()), credentials);
      }
      ServerHost var2 = ServerHost.fromUrl(var1.get_server_host());
      String var3 = var1.get_user_name();
      String var4 = var1.get_salesforce_password();
      String var5 = Crypto.decryptString(var4);
      String var6 = var1.get_security_token();
      String var7 = var6 == null ? "" : Crypto.decryptString(var6);
      boolean var8 = var1.get_sandbox() != 0;
      LoginCredentials var9 = new LoginCredentials(var3, var5, var7, var8);
      String var10 = var1.get_api_version();
      SalesforceVersion var11 = SalesforceVersion.fromId(var10);
      return new OrgIdentifier(var2, var11, var9);
   }

   private OrgIdentifier unmarshalOrgId(WsSalesforceEngineLoginParams var1) {
      if (LoginCredentials.OAUTH.equals(var1.getAuthType())) {
         LoginCredentials credentials = new LoginCredentials("", "", "", var1.isSandbox(), LoginCredentials.OAUTH,
            var1.getInstanceUrl(), var1.getClientId(), Crypto.decryptString(var1.getClientSecret()));
         return new OrgIdentifier(ServerHost.fromUrl(var1.getLoginUrl()), SalesforceVersion.fromId(var1.getVersion()), credentials);
      }
      ServerHost var2 = ServerHost.fromUrl(var1.getLoginUrl());
      String var3 = var1.getUserName();
      String var4 = Crypto.decryptString(var1.getPassword());
      String var5 = Strings.isNullOrEmpty(var1.getSecurityToken()) ? "" : Crypto.decryptString(var1.getSecurityToken());
      boolean var6 = var1.isSandbox();
      LoginCredentials var7 = new LoginCredentials(var3, var4, var5, var6);
      String var8 = var1.getVersion();
      SalesforceVersion var9 = SalesforceVersion.fromId(var8);
      return new OrgIdentifier(var2, var9, var7);
   }

   private WsSalesforceEngineError createForException(int var1, Exception var2, String var3) {
      try {
         var2.printStackTrace();
         StringBuilder var4 = new StringBuilder(WsUtils.escapeExceptionMessageAndGetCause(var2));
         ServerErrors.appendStacktrace(var2, var4);
         return new WsSalesforceEngineError(var1, var4.toString(), "");
      } catch (RuntimeException var5) {
         var5.printStackTrace();
         return new WsSalesforceEngineError(-99, var2.getMessage(), "");
      }
   }

   @Override
   public WsSalesforceEngineError logoutFromSalesforce(String var1, String var2, WsSalesforceEngineSessionParams var3) throws RemoteException {
      return new WsSalesforceEngineError(-1, "Logout has not been implemented yet.", "");
   }
}
