package org.etlforge.integration.server.engine.connector;

import com.google.common.base.Strings;
import java.sql.Connection;
import java.util.Properties;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.trandb.ProxySettingsSupplier;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointParameters;
import org.etlforge.util.net.ProxySettings;

public final class ConnectorEndpointParametersAdapter {
   final String etlforgeUser;
   final String etlforgeMd5Pwd;
   final String endpointGUID;
   private String ConnectorPassword;
   final ConnectorEndpointParameters paras;

   public static ConnectorEndpointParametersAdapter forAuthenticatedETLForgeUser(
      String var0, String var1, String var2, String var3, String var4, boolean var5, Properties var6
   ) {
      return new ConnectorEndpointParametersAdapter(false, null, null, var0, var1, var2, var3, var4, var5, var6);
   }

   public ConnectorEndpointParametersAdapter(
      boolean var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, boolean var9, Properties var10
   ) {
      var5 = var5.toUpperCase();
      this.etlforgeUser = var2;
      this.etlforgeMd5Pwd = var3;
      this.endpointGUID = var4;
      this.ConnectorPassword = var9 ? ConnectorExecutionSystem.descryptPassword(var8) : var8;
      if (var1) {
         ProxySettings var11 = this.validateCallandUpdatePassword(var5);
         this.paras = new ConnectorEndpointParameters(var5, var6, var7, this.ConnectorPassword, var10, var11);
      } else {
         this.paras = new ConnectorEndpointParameters(var5, var6, var7, this.ConnectorPassword, var10, ProxySettings.NONE);
      }
   }

   public ConnectorEndpointParameters getEndpointParameters() {
      return this.paras;
   }

   public String getEndpointString() {
      return this.getEndpointParameters().getEndpointString();
   }

   private ProxySettings validateCallandUpdatePassword(String var1) {
      ExternalApiSupport var2 = null;

      try {
         var2 = ExternalApiSupport.forCall(this.etlforgeUser, this.etlforgeMd5Pwd);
         Connection var3 = var2.getTranDbConnection();
         boolean var4 = !Strings.isNullOrEmpty(this.endpointGUID) && Strings.isNullOrEmpty(this.ConnectorPassword);
         if (var4) {
            String var5 = ConnectorEndpointTab.getEndpointPassword(this.endpointGUID, var1, var3);
            if (var5 != null) {
               this.ConnectorPassword = ConnectorDefaultValueResolver.resolve(ConnectorExecutionSystem.descryptPassword(var5));
            }
         }

         ProxySettingsSupplier var13 = new ProxySettingsSupplier(var3);
         return (ProxySettings)var13.get();
      } catch (ServerAccessException var11) {
         throw new ConnectorServerRuntimeException(var11);
      } finally {
         ExternalApiSupport.safeDispose(var2);
      }
   }
}
