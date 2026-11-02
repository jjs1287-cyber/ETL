package org.etlforge.integration.server.engine.connector.ebs;

import com.google.common.base.Strings;
import java.util.Properties;
import org.etlforge.integration.server.engine.connector.ebs.adapter.ConnectorEndpointEbsAdapter;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointAbstract;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointParameters;

public class ConnectorEndpointEbs extends ConnectorEndpointAbstract {
   public ConnectorEngineEbs newConnectorEngine() {
      return new ConnectorEngineEbs(this);
   }

   @Override
   public boolean testConnection() {
      ConnectorEndpointEbsAdapter var1 = this.getConnectorEndpointEbsAdapter();
      return var1.testConnection();
   }

   public ConnectorEndpointEbsAdapter getConnectorEndpointEbsAdapter() {
      ConnectorEndpointParameters var1 = this.getEndpointParameters();
      Properties var2 = var1.getParameters();
      String var3 = var2.getProperty("connecturl".toLowerCase());
      if (Strings.isNullOrEmpty(var3)) {
         throw new ConnectorInvalidInputRuntimeException("no input for connectionurl");
      } else {
         String var4 = var1.getUser();
         if (Strings.isNullOrEmpty(var4)) {
            throw new ConnectorInvalidInputRuntimeException("no input for username");
         } else {
            return new ConnectorEndpointEbsAdapter(var3, var4, var1.getPassword());
         }
      }
   }
}
