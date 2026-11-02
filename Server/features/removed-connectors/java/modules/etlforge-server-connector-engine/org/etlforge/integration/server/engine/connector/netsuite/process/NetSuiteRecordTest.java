package org.etlforge.integration.server.engine.connector.netsuite.process;

import org.etlforge.integration.design.netsuite.adapter.NetSuiteObject;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteDefinitionExecution;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.util.net.ProxySettings;

public class NetSuiteRecordTest {
   private static NetSuitePassport dummy = new NetSuitePassport(ProxySettings.NONE, false, "email", "password", "account", null);

   public static void main(String[] var0) {
      NetSuiteDefinitionExecution var1 = NetSuiteExecutionContext.INSTANCE.getExecution(dummy);
      ConnectorNetSuiteNamespaces var2 = var1.getConnectorNetSuiteNamespaces();
      NetSuiteRecordRefWrapper var3 = new NetSuiteRecordRefWrapper(var1.getDefinitionConnector().getRecordRefDeriveType());
      String var4 = "Customer";
      XsdGlobalElementName var5 = var1.getDefinitionConnector().getRecordDerivedObjectExists(var4);
      NetSuiteObject var6 = new NetSuiteObject(var5);
      NetSuiteRecordRequest var7 = new NetSuiteRecordRequest(var2, var6, var3);
      String var8 = "Z:/etlforge/connectors/etlforgeconnectorsample/NETSUITE/Sample/recordref/";
      String var9 = var8 + "upsertRecordReqJB.xml";
      String var10 = var8 + "upsertRecordReqJB_NS.xml";
      var7.transform(var9, var10);
      boolean var11 = false;
      var9 = var8 + "searchRecordResNS.xml";
      var10 = var8 + "searchRecordResNS_JB.xml";
      NetSuiteRecordResponse.transform(var3, var2, var6, var9, var10, var11);
   }
}
