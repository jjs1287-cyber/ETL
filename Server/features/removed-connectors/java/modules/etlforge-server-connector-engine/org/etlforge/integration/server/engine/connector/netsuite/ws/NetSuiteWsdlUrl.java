package org.etlforge.integration.server.engine.connector.netsuite.ws;

import java.net.URI;

public class NetSuiteWsdlUrl {
   private final String wsdlUrl;

   public NetSuiteWsdlUrl(String var1) {
      this.wsdlUrl = var1;
   }

   public URI newDataCenterUrl(String var1) {
      return NetSuiteSoapServerRedirect.newURI(this.wsdlUrl, var1);
   }

   public String newDataCenterUrlString(String var1) {
      URI var2 = this.newDataCenterUrl(var1);
      return var2.toString();
   }

   public final String getWsdlUrl() {
      return this.wsdlUrl;
   }
}
