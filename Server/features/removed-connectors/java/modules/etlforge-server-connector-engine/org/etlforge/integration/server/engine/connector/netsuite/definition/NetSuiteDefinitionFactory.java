package org.etlforge.integration.server.engine.connector.netsuite.definition;

import org.etlforge.integration.design.netsuite.config.NetSuiteConfig;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionCommon;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.server.engine.connector.netsuite.wsdl.NetSuiteDefinitionContentsFactory;
import org.etlforge.integration.server.netsuite.factory.NetSuiteDefinitionDesignFactory;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteDefinitionContents;
import org.etlforge.util.net.ProxySettings;

public class NetSuiteDefinitionFactory {
   public static NetSuiteDefinitionExecution createDefinitionExecution(ProxySettings var0, String var1) {
      NetSuiteDefinitionDesign var2 = NetSuiteExecutionContext.INSTANCE.getDesign(var0, var1);
      NetSuiteDefinitionCommon var3 = var2.getNetSuiteDefinition().getNetSuiteDefinitionCommon();
      String var4 = var2.getNetSuiteDefinition().getWsdlServerUrl();
      NetSuiteConfig var5 = var2.getNetSuiteConfig();
      return new NetSuiteDefinitionExecution(var5, var4, var3);
   }

   public static NetSuiteDefinitionDesign createNetSuiteDefinitionDesign(ProxySettings var0, String var1) {
      NetSuiteDefinitionContents var2 = NetSuiteDefinitionContentsFactory.createFromOnline(var0, var1);
      NetSuiteConfig var3 = NetSuiteConfigInstance.getNetSuiteConfig();
      return NetSuiteDefinitionDesignFactory.build(var2, var3);
   }
}
