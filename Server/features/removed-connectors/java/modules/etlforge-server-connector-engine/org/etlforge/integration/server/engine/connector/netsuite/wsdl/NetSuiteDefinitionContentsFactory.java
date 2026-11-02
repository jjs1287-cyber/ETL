package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteDefinitionContents;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteWsdlContents;
import org.etlforge.util.net.ProxySettings;

public class NetSuiteDefinitionContentsFactory {
   public static String getConfigXml() {
      return ConnectorExecutionSystem.readFileUTF8(NetSuiteWsdlFile.getNetSuiteConfigFile());
   }

   public static NetSuiteDefinitionContents createFromOnline(NetSuitePassport var0) {
      return createFromOnline(var0.getProxySettings(), var0.getNetsuiteWsdlDownloadUrl());
   }

   public static NetSuiteDefinitionContents createFromOnline(ProxySettings var0, String var1) {
      String var2 = getConfigXml();
      NetSuiteWsdlContents var3 = NetSuiteWsdlContentsCache.getNetSuiteWsdlContents(var0, var1);
      return new NetSuiteDefinitionContents(var3, var2);
   }
}
