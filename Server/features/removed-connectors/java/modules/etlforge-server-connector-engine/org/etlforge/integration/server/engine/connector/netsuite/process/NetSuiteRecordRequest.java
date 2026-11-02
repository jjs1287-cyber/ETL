package org.etlforge.integration.server.engine.connector.netsuite.process;

import org.etlforge.integration.design.netsuite.adapter.NetSuiteObject;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;

public class NetSuiteRecordRequest {
   private final NetSuiteRecordRequestTransformer transformer;

   public NetSuiteRecordRequest(ConnectorNetSuiteNamespaces var1, NetSuiteObject var2, NetSuiteRecordRefWrapper var3) {
      String var4 = var2.getTypeNamespace();
      String var5 = var1.getNamespacePreferedPrefix().getPrefixExists(var4);
      byte var6 = 1;
      this.transformer = new NetSuiteRecordRequestTransformer(var1, var6, var2.getTypeXsdGlobalElementName(), var5, var3);
   }

   public void transform(String var1, String var2) {
      this.transformer.transform(var1, var2);
   }
}
