package org.etlforge.integration.server.netsuite.wsdl;

public class NetSuiteDefinitionContents {
   private final String configXml;
   private final NetSuiteWsdlContents wsdlContents;

   public NetSuiteDefinitionContents(NetSuiteWsdlContents var1, String var2) {
      this.wsdlContents = var1;
      this.configXml = var2;
   }

   public NetSuiteDefinitionContents(String var1, String var2) {
      this.wsdlContents = new NetSuiteWsdlContents(var1);
      this.configXml = var2;
   }

   public String getConfigXml() {
      return this.configXml;
   }

   public void setXsdSchema(String var1, String var2) {
      this.wsdlContents.setXsdSchema(var1, var2);
   }

   public NetSuiteWsdlContents getNetSuiteWsdlContents() {
      return this.wsdlContents;
   }
}
