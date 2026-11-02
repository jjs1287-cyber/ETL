package org.etlforge.integration.server.engine.connector.endpoint;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerAccessRuntimeException;

public class ConnectorWebServiceUrlConfig {
   static ConnectorWebServiceUrlConfig config = new ConnectorWebServiceUrlConfig();
   private static final String section = "ConnectorsInfo";
   final Map<String, String> urlMap = Maps.newConcurrentMap();
   private static final String HTTPLOCALHOST = "http://127.0.0.1";

   ConnectorWebServiceUrlConfig() {
      this.readConfiggedEndpointTypes();
   }

   private synchronized void readConfiggedEndpointTypes() {
      this.urlMap.clear();
      String var1 = ServerConfig.getOption("ConnectorsInfo", "MSCRMUrl", "http://127.0.0.1:8000/CRM/");
      this.urlMap.put("MSCRM", var1);
      var1 = ServerConfig.getOption("ConnectorsInfo", "MSAXUrl", "http://127.0.0.1:8002/AX/");
      this.urlMap.put("MSAX", var1);
   }

   public static String getBaseUrl(String var0) {
      String var1 = config.urlMap.get(var0);
      if (Strings.isNullOrEmpty(var1)) {
         String var2 = "Cannot get the url from the configuration for the connector " + var0;
         throw new ConnectorServerAccessRuntimeException(var2);
      }

      if (!var1.endsWith("/")) {
         var1 = var1 + "/";
      }

      return var1;
   }
}
