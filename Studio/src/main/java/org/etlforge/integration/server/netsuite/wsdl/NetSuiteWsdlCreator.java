package org.etlforge.integration.server.netsuite.wsdl;

public class NetSuiteWsdlCreator {
   private final NetSuiteWsdlContents contents;
   NetSuiteWsdl wsdl = new NetSuiteWsdl();

   public NetSuiteWsdlCreator(NetSuiteDefinitionContents var1) {
      this.contents = var1.getNetSuiteWsdlContents();
   }

   public NetSuiteWsdl create() {
      this.wsdl = new NetSuiteWsdl();
      NetSuiteWsdlParser var1 = new NetSuiteWsdlParser(this.wsdl);
      var1.parseString(this.contents.getNetsuiteWsdl());
      this.parseSchemas();
      return this.wsdl;
   }

   void parseSchemas() {
      for (String var2 : this.contents.getXsdSchemaKeys()) {
         this.parseSchemas(var2, this.contents.getXsdSchema(var2));
      }
   }

   void parseSchemas(String var1, String var2) {
      NetSuiteXsdParser var3 = new NetSuiteXsdParser(var1, this.wsdl.getDefinitions());
      var3.parseString(var2);
   }

   public static void main(String[] var0) {
   }
}
