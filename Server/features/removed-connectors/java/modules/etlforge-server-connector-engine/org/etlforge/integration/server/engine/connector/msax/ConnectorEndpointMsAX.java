package org.etlforge.integration.server.engine.connector.msax;

import com.google.common.base.Strings;
import java.util.Properties;
import org.etlforge.integration.server.engine.connector.endpoint.ConnectorWebServiceUrlConfig;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointAbstract;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointParameters;
import org.etlforge.integration.server.engine.connector.msax.adapter.ConnectorEndpointMsAXAdapter;
import org.etlforge.util.net.ProxySettings;

public class ConnectorEndpointMsAX extends ConnectorEndpointAbstract {
   public ConnectorEngineMsAX newConnectorEngine() {
      return new ConnectorEngineMsAX(this);
   }

   @Override
   public boolean testConnection() {
      String var1 = this.getEndpointParameters().getParameters().getProperty("wsdl_urls");
      ConnectorEndpointMsAXAdapter var2 = this.getConnectorEndpointMsAXAdapter();
      return var2.testConnection(var1);
   }

   public ConnectorEndpointMsAXAdapter getConnectorEndpointMsAXAdapter() {
      ConnectorEndpointParameters var1 = this.getEndpointParameters();
      Properties var2 = this.getEndpointParameters().getParameters();
      String var3 = var2.getProperty("service_url");
      String var4 = this.getEndpointParameters().getEndpointTypeName();
      String var5 = ConnectorWebServiceUrlConfig.getBaseUrl(var4);
      StringBuilder var6 = new StringBuilder();
      String var7 = var1.getUser();
      String var8 = var1.getPassword();
      String var9 = var2.getProperty("domain");
      if (Strings.isNullOrEmpty(var7)) {
         var6.append("User,");
      }

      boolean var10 = var6.length() > 0;
      if (var10) {
         String var13 = "No input for " + var6.substring(0, var6.length() - 1);
         throw new ConnectorInvalidInputRuntimeException(var13);
      } else {
         ProxySettings var11 = var1.getProxySettings();
         return new ConnectorEndpointMsAXAdapter(var5, var3, var7, var8, var9, var11);
      }
   }
}
