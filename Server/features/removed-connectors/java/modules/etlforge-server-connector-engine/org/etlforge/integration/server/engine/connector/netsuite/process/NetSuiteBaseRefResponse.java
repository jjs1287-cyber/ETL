package org.etlforge.integration.server.engine.connector.netsuite.process;

import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;

public class NetSuiteBaseRefResponse {
   public static void transform(ConnectorNetSuiteNamespaces var0, String var1, String var2) {
      NetSuiteRecordRefWrapper var3 = new NetSuiteRecordRefWrapper();
      NetSuiteResponseTransformer var4 = new NetSuiteResponseTransformer(var3, var0, var0.getBaseRefElementNamespace(), "baseRef");
      var4.transform(var1, var2);
   }

   private NetSuiteBaseRefResponse() {
   }

   public static void main(String[] var0) {
      String var1 = "Z:/etlforge/connectors/etlforgeconnectorsample/NETSUITE/Sample/SampleRecordConversion/";
      String var2 = var1 + "delete_jb_oneof_ns.xml";
      String var3 = var1 + "delete_jb_oneof_ns_jb.xml";
   }
}
