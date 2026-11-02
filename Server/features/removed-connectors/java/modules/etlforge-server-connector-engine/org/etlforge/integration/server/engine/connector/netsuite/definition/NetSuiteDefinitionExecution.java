package org.etlforge.integration.server.engine.connector.netsuite.definition;

import org.etlforge.integration.design.netsuite.config.NetSuiteConfig;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionCommon;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionConnector;
import org.etlforge.integration.server.engine.connector.netsuite.ws.NetSuiteServerWsdlUrl;

public class NetSuiteDefinitionExecution {
   private final NetSuiteDefinitionCommon definitionCommon;
   private final NetSuiteServerWsdlUrl serverWsdlurl;
   private final NetSuiteConfig config;

   public NetSuiteDefinitionExecution(NetSuiteConfig var1, String var2, NetSuiteDefinitionCommon var3) {
      this.config = var1;
      this.serverWsdlurl = new NetSuiteServerWsdlUrl(var2);
      this.definitionCommon = var3;
   }

   public ConnectorNetSuiteNamespaces getConnectorNetSuiteNamespaces() {
      return this.definitionCommon.getConnectorNetSuiteNamespaces();
   }

   public NetSuiteDefinitionConnector getDefinitionConnector() {
      return this.definitionCommon.getDefinitionConnector();
   }

   public NetSuiteServerWsdlUrl getNetSuiteServerWsdlUrl() {
      return this.serverWsdlurl;
   }

   public NetSuiteConfig getNetSuiteConfig() {
      return this.config;
   }
}
