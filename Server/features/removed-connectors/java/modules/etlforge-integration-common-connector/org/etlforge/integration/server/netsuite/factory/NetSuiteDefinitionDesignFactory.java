package org.etlforge.integration.server.netsuite.factory;

import org.etlforge.integration.design.netsuite.config.NetSuiteConfig;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinition;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteDefinitionSearch;
import org.etlforge.integration.server.netsuite.config.NetSuiteConfigFactory;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteDefinitionContents;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteDefinitionSearchFactory;

public class NetSuiteDefinitionDesignFactory {
   public static NetSuiteDefinitionDesign build(NetSuiteDefinitionContents var0) {
      NetSuiteConfig var1 = NetSuiteConfigFactory.create(var0.getConfigXml());
      return build(var0, var1);
   }

   public static NetSuiteDefinitionDesign build(NetSuiteDefinitionContents var0, NetSuiteConfig var1) {
      NetSuiteDefinition var2 = NetSuiteDefinitionFactory.create(var0, var1);
      NetSuiteDefinitionSearch var3 = NetSuiteDefinitionSearchFactory.create(var0.getNetSuiteWsdlContents(), var2.getConnectorNetSuiteNamespaces());
      return new NetSuiteDefinitionDesign(var2, var3, var1);
   }
}
