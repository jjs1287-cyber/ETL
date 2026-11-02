package org.etlforge.integration.server.engine.connector.netsuite.process;

import org.etlforge.integration.design.netsuite.adapter.NetSuiteObject;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.server.engine.connector.netsuite.search.NetSuiteSearchNames;

public class NetSuiteRecordResponse {
   public static void transform(NetSuiteRecordRefWrapper var0, ConnectorNetSuiteNamespaces var1, NetSuiteObject var2, String var3, String var4, boolean var5) {
      String var6 = var2 == null ? null : var2.getObjectElementName();
      NetSuiteSearchNames var7 = new NetSuiteSearchNames(var5, var1);
      NetSuiteResponseTransformer var8 = new NetSuiteResponseTransformer(var0, var1, var7.getRecordResponseNamespace(), var7.getRecordLocalName(), var6, var5);
      var8.transform(var3, var4);
   }

   public static void main(String[] var0) {
      String var1 = "Z:/etlforge/connectors/etlforgeconnectorsample/NETSUITE/Sample/search/advancedSearch/transform/";
      String var2 = var1 + "advanceSearchRequest_res_ns.xml";
      String var3 = var1 + "advanceSearchRequest_res_jb.xml";
   }
}
