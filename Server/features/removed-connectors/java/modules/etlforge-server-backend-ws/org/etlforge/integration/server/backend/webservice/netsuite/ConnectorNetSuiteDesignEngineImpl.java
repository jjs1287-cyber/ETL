package org.etlforge.integration.server.backend.webservice.netsuite;

import org.apache.log4j.Logger;
import org.etlforge.integration.server.backend.webservice.connector.ConnectorEngineWsExceptionUtils;
import org.etlforge.integration.server.backend.webservice.connector.WsEndpointParameters;
import org.etlforge.integration.server.backend.webservice.connector.WsStatus;

public class ConnectorNetSuiteDesignEngineImpl {
   private static final Logger logger = Logger.getLogger("ConnectorNetSuiteDesignEngine");

   public static WsNetSuiteDefinitionContents getNetSuiteDefinitionContents(WsEndpointParameters var0) {
      try {
         return ConnectorNetSuiteDesignEngineAdapter.getNetSuiteDefinitionContents(var0);
      } catch (Throwable var5) {
         var5.printStackTrace();
         ConnectorEngineWsExceptionUtils var2 = new ConnectorEngineWsExceptionUtils();
         WsStatus var3 = var2.obtainBadWsStatus(var5);
         WsNetSuiteDefinitionContents var4 = new WsNetSuiteDefinitionContents();
         var4.setStatus(var3);
         return var4;
      }
   }

   public static WsNetSuiteObjectCustomFieldsNew getNetSuiteCustomFieldsNew(WsEndpointParameters var0, boolean var1, String var2) {
      try {
         return ConnectorNetSuiteDesignEngineAdapter.getNetSuiteCustomFieldsNew(var0, var1, var2);
      } catch (Throwable var7) {
         var7.printStackTrace();
         ConnectorEngineWsExceptionUtils var4 = new ConnectorEngineWsExceptionUtils();
         WsStatus var5 = var4.obtainBadWsStatus(var7);
         WsNetSuiteObjectCustomFieldsNew var6 = new WsNetSuiteObjectCustomFieldsNew();
         var6.setStatus(var5);
         return var6;
      }
   }

   public static WsNetSuiteSavedSearch getSavedSearch(WsEndpointParameters var0, String var1) {
      try {
         return ConnectorNetSuiteDesignEngineAdapter.getSavedSearch(var0, var1);
      } catch (Throwable var6) {
         var6.printStackTrace();
         ConnectorEngineWsExceptionUtils var3 = new ConnectorEngineWsExceptionUtils();
         WsStatus var4 = var3.obtainBadWsStatus(var6);
         WsNetSuiteSavedSearch var5 = new WsNetSuiteSavedSearch();
         var5.setStatus(var4);
         return var5;
      }
   }
}
