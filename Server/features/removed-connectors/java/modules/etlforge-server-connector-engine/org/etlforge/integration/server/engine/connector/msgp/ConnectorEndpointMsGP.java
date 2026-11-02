package org.etlforge.integration.server.engine.connector.msgp;

import com.google.common.base.Strings;
import java.util.Properties;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointAbstract;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointParameters;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;
import org.etlforge.integration.server.engine.connector.msgp.adapter.MsGPConnectorEndpointAdapter;

public class ConnectorEndpointMsGP extends ConnectorEndpointAbstract {
   @Override
   public ConnectorEngine newConnectorEngine() {
      return new ConnectorEngineMsGP(this);
   }

   @Override
   public boolean testConnection() {
      MsGPConnectorEndpointAdapter var1 = this.getMsGPEndpointAdapter();
      return var1.testConnection();
   }

   public MsGPConnectorEndpointAdapter getMsGPEndpointAdapter() {
      ConnectorEndpointParameters var1 = this.getEndpointParameters();
      Properties var2 = this.getEndpointParameters().getParameters();
      StringBuilder var3 = new StringBuilder();
      String var4 = var2.getProperty("service_url".toLowerCase());
      String var5 = var1.getHost();
      String var6 = var2.getProperty("databasename".toLowerCase());
      if (Strings.isNullOrEmpty(var4)) {
         var3.append("ServiceUrl,");
      }

      if (Strings.isNullOrEmpty(var5)) {
         var3.append("Database server,");
      }

      if (Strings.isNullOrEmpty(var6)) {
         var3.append("databasename,");
      }

      boolean var7 = var3.length() > 0;
      if (var7) {
         String var8 = "No input for " + var3.substring(0, var3.length() - 1);
         throw new ConnectorInvalidInputRuntimeException(var8);
      } else {
         return new MsGPConnectorEndpointAdapter(var4, var5, var6);
      }
   }
}
