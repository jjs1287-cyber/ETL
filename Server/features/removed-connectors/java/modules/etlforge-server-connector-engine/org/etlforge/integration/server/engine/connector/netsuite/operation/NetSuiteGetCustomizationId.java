package org.etlforge.integration.server.engine.connector.netsuite.operation;

import java.util.List;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.ws.NetSuiteResponseStatusHandler;
import org.etlforge.integration.server.engine.connector.netsuite.ws.NetSuiteSoapServiceExecution;

public class NetSuiteGetCustomizationId {
   private static String getCustomizationId = "getCustomizationId";
   private final NetSuitePassport passport;
   private static final String statusRelativePathRoot = "Envelope/Body/getCustomizationIdResponse/getCustomizationIdResult/status";

   public NetSuiteGetCustomizationId(NetSuitePassport var1) {
      this.passport = var1;
   }

   private static String getCustomizationIdRequest(ConnectorNetSuiteNamespaces var0, String var1, boolean var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("<" + getCustomizationId + " xmlns=\"" + var0.getMessageNamespace() + "\">");
      var3.append("<customizationType getCustomizationType=\"" + var1 + "\"/>");
      if (var2) {
         var3.append("<includeInactives>true</includeInactives>");
      } else {
         var3.append("<includeInactives>false</includeInactives>");
      }

      var3.append("</" + getCustomizationId + ">");
      return var3.toString();
   }

   public List<NetSuiteCustomizationRef> getCustomizationId(String var1) {
      String var2 = this.getCustomizationIdResponse(var1);
      return NetSuiteGetCustomizationIdResponse.parseResponse(var2);
   }

   private String getCustomizationIdResponse(String var1) {
      return this.getCustomizationIdResponse(var1, false);
   }

   private String getCustomizationIdResponse(String var1, boolean var2) {
      NetSuiteSoapServiceExecution var3 = new NetSuiteSoapServiceExecution(this.passport);
      ConnectorNetSuiteNamespaces var4 = NetSuiteExecutionContext.INSTANCE.getExecution(this.passport).getConnectorNetSuiteNamespaces();
      String var5 = getCustomizationIdRequest(var4, var1, var2);
      String var6 = var3.executeString(getCustomizationId, var5, true);
      NetSuiteResponseStatusHandler.validateResponse("Envelope/Body/getCustomizationIdResponse/getCustomizationIdResult/status", var6);
      return var6;
   }
}
