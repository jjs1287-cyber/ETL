package org.etlforge.integration.server.backend.webservice.mscrm;

import com.google.common.base.Strings;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.Properties;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.backend.webservice.connector.WsEndpointParameters;
import org.etlforge.integration.server.backend.webservice.connector.WsNameValuePair;
import org.etlforge.integration.server.backend.webservice.connector.WsStatus;
import org.etlforge.integration.server.engine.connector.ConnectorEndpointParametersAdapter;
import org.etlforge.integration.server.engine.connector.ConnectorEndpointTab;
import org.etlforge.integration.server.engine.connector.endpoint.ConnectorEndpointTypeDef;
import org.etlforge.integration.server.engine.connector.mscrm.ConnectorEndpointMsCrm;
import org.etlforge.integration.server.engine.connector.mscrm.ConnectorEngineMsCrm;
import org.etlforge.integration.server.ws.WsUtils;

abstract class DesignTask<T> {
   private final String name;
   private final String etlforgeUser;
   private final String etlforgePassword;
   private final WsEndpointParameters endpointParams;

   protected DesignTask(String var1, String var2, String var3, WsEndpointParameters var4) {
      this.name = var1;
      this.etlforgeUser = var2;
      this.etlforgePassword = var3;
      this.endpointParams = var4;
   }

   protected abstract T runTask(ConnectorEngineMsCrm var1) throws Exception;

   public final T execute() throws RemoteException {
      ExternalApiSupport var1 = null;

      try {
         var1 = ExternalApiSupport.forCall(this.etlforgeUser, this.etlforgePassword);
         ConnectorEngineMsCrm var2 = this.createEngine(var1.getTranDbConnection());
         var1.dispose();
         return this.runTask(var2);
      } catch (Exception var8) {
         WsUtils.convertError(var8, this.getClass(), this.name);
      } finally {
         ExternalApiSupport.safeDispose(var1);
      }

      return null;
   }

   private ConnectorEngineMsCrm createEngine(Connection var1) {
      ConnectorEndpointMsCrm var2 = this.getEndpoint(var1);
      return var2.newConnectorEngine();
   }

   private ConnectorEndpointMsCrm getEndpoint(Connection var1) {
      Properties var2 = new Properties();

      for (WsNameValuePair var6 : this.endpointParams.getAdditionalParameters()) {
         var2.put(var6.getName().toLowerCase(), var6.getValue());
      }

      String var7 = this.endpointParams.getPassword();
      boolean var8 = this.endpointParams.isIsPasswordEncrypted();
      if (Strings.isNullOrEmpty(var7)) {
         String var9 = this.endpointParams.getEndpointGUID();
         var7 = ConnectorEndpointTab.getEndpointPassword(var9, ConnectorEndpointTypeDef.getMscrmSystem(), var1);
         var8 = true;
      }

      ConnectorEndpointParametersAdapter var10 = ConnectorEndpointParametersAdapter.forAuthenticatedETLForgeUser(
         this.endpointParams.getEndpointGUID(),
         this.endpointParams.getEndpointTypeName(),
         this.endpointParams.getHost(),
         this.endpointParams.getUser(),
         var7,
         var8,
         var2
      );
      ConnectorEndpointMsCrm var11 = new ConnectorEndpointMsCrm();
      var11.setEndpointParameters(var10.getEndpointParameters());
      return var11;
   }

   protected static final WsStatus successStatus() {
      return new WsStatus(0, "");
   }
}
