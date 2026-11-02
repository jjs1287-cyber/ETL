package org.etlforge.integration.server.engine.connector.netsuite.ws;

import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;

public class NetSuiteServerWsdlUrl {
   private final NetSuiteWsdlUrl wsdlUrl;
   private final NetSuiteWsdlUrl sandboxWsdlUrl;
   private static final String SandBoxReplaceString = ".netsuite.com";

   public NetSuiteServerWsdlUrl(String var1) {
      this.wsdlUrl = new NetSuiteWsdlUrl(var1);
      this.sandboxWsdlUrl = new NetSuiteWsdlUrl(getSandboxUrl(var1));
   }

   public NetSuiteWsdlUrl getNetSuiteWsdlUrl(boolean var1) {
      return var1 ? this.sandboxWsdlUrl : this.wsdlUrl;
   }

   private static String getSandboxUrl(String var0) {
      if (var0.contains(".netsuite.com")) {
         return var0.replace(".netsuite.com", ".sandbox.netsuite.com");
      } else {
         throw new ConnectorNetSuiteDefinitionError(
            "Sandbox url cannot be identified since the server url [" + var0 + "] does not follow the identification pattern"
         );
      }
   }
}
