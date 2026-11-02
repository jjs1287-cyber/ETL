package org.etlforge.integration.server.engine.connector.mscrm;

import com.google.common.base.Strings;
import java.util.Properties;
import org.etlforge.integration.server.engine.connector.endpoint.ConnectorWebServiceUrlConfig;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointAbstract;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointParameters;
import org.etlforge.integration.server.engine.connector.mscrm.adapter.ConnectorEndpointMsCrmAdapter;
import org.etlforge.util.net.ProxySettings;

public class ConnectorEndpointMsCrm extends ConnectorEndpointAbstract {
   public ConnectorEngineMsCrm newConnectorEngine() {
      return new ConnectorEngineMsCrm(this);
   }

   @Override
   public boolean testConnection() {
      ConnectorEndpointMsCrmAdapter var1 = this.getConnectorEndpointMsCrmAdapter();
      return var1.testConnection();
   }

   public ConnectorEndpointMsCrmAdapter getConnectorEndpointMsCrmAdapter() {
      ConnectorEndpointParameters var1 = this.getEndpointParameters();
      Properties var2 = this.getEndpointParameters().getParameters();
      StringBuilder var3 = new StringBuilder();
      String var4 = var1.getUser();
      String var5 = var1.getPassword();
      String var6 = var1.getHost();
      String var7 = var2.getProperty("organizationUniqueName".toLowerCase());
      String var8 = var2.getProperty("authType".toLowerCase());
      String var9 = var2.getProperty("domain".toLowerCase());
      if (Strings.isNullOrEmpty(var4)) {
         var3.append("User,");
      }

      if (Strings.isNullOrEmpty(var6)) {
         var3.append("discoveryServiceAddress,");
      }

      if (Strings.isNullOrEmpty(var7)) {
         var3.append("organizationUniqueName,");
      }

      if (Strings.isNullOrEmpty(var8)) {
         var3.append("authType,");
      }

      boolean var10 = var3.length() > 0;
      if (var10) {
         String var15 = "No input for " + var3.substring(0, var3.length() - 1);
         throw new ConnectorInvalidInputRuntimeException(var15);
      } else {
         ProxySettings var11 = var1.getProxySettings();
         String var12 = var1.getEndpointTypeName();
         String var13 = ConnectorWebServiceUrlConfig.getBaseUrl(var12);
         return new ConnectorEndpointMsCrmAdapter(var13, var4, var5, var6, var7, var8, var9, var11);
      }
   }
}
