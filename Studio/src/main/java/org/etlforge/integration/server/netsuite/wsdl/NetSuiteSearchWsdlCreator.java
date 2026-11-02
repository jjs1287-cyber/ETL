package org.etlforge.integration.server.netsuite.wsdl;

import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteSearchWsdl;

public class NetSuiteSearchWsdlCreator {
   static NetSuiteSearchWsdl newNetSuiteSearchWsdl(NetSuiteWsdlContents var0, ConnectorNetSuiteNamespaces var1) {
      NetSuiteSearchWsdl var2 = new NetSuiteSearchWsdl(var1);

      for (String var4 : var0.getXsdSchemaKeys()) {
         NetSuiteSearchXsdParser var5 = new NetSuiteSearchXsdParser(var2);
         var5.parseString(var0.getXsdSchema(var4));
      }

      var2.validateUniqueName();
      return var2;
   }

   public static void main(String[] var0) {
   }
}
