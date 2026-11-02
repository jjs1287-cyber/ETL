package org.etlforge.integration.server.engine.connector.netsuite.search;

import java.util.Map;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteObjectsCreator;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteDefinitionExecution;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.ws.NetSuiteSoapServiceExecution;

public class GetSavedSearch {
   private static final String getSavedSearch = "getSavedSearch";

   private static String getRequestXml(String var0, ConnectorNetSuiteNamespaces var1) {
      return "<getSavedSearch xmlns=\"" + var1.getMessageNamespace() + "\"><record searchType=\"" + var0.toLowerCase() + "\"/></getSavedSearch>";
   }

   public static Map<String, String> getSavedSearch(NetSuitePassport var0, String var1) {
      return getSavedSearchImpl(var0, var1);
   }

   public static Map<String, String> getSavedSearchImpl(NetSuitePassport var0, String var1) {
      NetSuiteConnectorObject var2 = new NetSuiteConnectorObject(var1);
      NetSuiteDefinitionExecution var3 = NetSuiteExecutionContext.INSTANCE.getExecution(var0);
      NetSuiteObjectsCreator var4 = new NetSuiteObjectsCreator(var3.getDefinitionConnector(), var3.getNetSuiteConfig());
      String var5 = var4.getSearchObjectName(var2);
      ConnectorNetSuiteNamespaces var6 = var3.getConnectorNetSuiteNamespaces();
      String var7 = getRequestXml(var5.toLowerCase(), var6);
      NetSuiteSoapServiceExecution var8 = new NetSuiteSoapServiceExecution(var0);
      String var9 = var8.executeString("getSavedSearch", var7, true);
      return GetSavedSearchResponse.getSavedSearches(var9);
   }
}
