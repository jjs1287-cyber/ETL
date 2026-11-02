package org.etlforge.integration.server.backend.webservice.connector;

import java.util.Properties;
import org.etlforge.integration.server.engine.connector.ConnectorEndpointParametersAdapter;
import org.etlforge.integration.server.engine.connector.ConnectorEngineExecutionWrapper;

public class ConnectorEngineExecutionWrapperFactory {
   public static ConnectorEngineExecutionWrapper obtainExecutionWrapper(boolean var0, WsEndpointParameters var1, boolean var2) {
      if (!var2) {
         ConnectorDefaultValueResolverAdapter.resolve(var1);
      }

      Properties var3 = new Properties();

      for (WsNameValuePair var7 : var1.getAdditionalParameters()) {
         var3.put(var7.getName().toLowerCase(), var7.getValue());
      }

      ConnectorEndpointParametersAdapter var8 = new ConnectorEndpointParametersAdapter(
         var0,
         var1.getEtlforgeUser(),
         var1.getEtlforgeMd5Pwd(),
         var1.getEndpointGUID(),
         var1.getEndpointTypeName().toUpperCase(),
         var1.getHost(),
         var1.getUser(),
         var1.getPassword(),
         var1.isIsPasswordEncrypted(),
         var3
      );
      return new ConnectorEngineExecutionWrapper(var8.getEndpointParameters());
   }
}
