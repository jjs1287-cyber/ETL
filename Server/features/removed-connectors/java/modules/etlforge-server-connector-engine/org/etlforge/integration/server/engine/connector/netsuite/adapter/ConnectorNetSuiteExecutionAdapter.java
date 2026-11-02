package org.etlforge.integration.server.engine.connector.netsuite.adapter;

import org.etlforge.integration.design.netsuite.adapter.NetSuiteObject;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteObjectsCreator;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteSelectedObject;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerNotSupportedRuntimeException;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteDefinitionExecution;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.process.NetSuiteBaseRefRequest;
import org.etlforge.integration.server.engine.connector.netsuite.process.NetSuiteBaseRefResponse;
import org.etlforge.integration.server.engine.connector.netsuite.process.NetSuiteRecordRefWrapper;
import org.etlforge.integration.server.engine.connector.netsuite.process.NetSuiteRecordRequest;
import org.etlforge.integration.server.engine.connector.netsuite.search.NetSuiteSearchOperation;
import org.etlforge.integration.server.engine.connector.netsuite.search.NetSuiteSearchPreference;
import org.etlforge.integration.server.engine.connector.netsuite.ws.NetSuiteSoapServiceExecution;
import org.etlforge.io.FileUtils;

public class ConnectorNetSuiteExecutionAdapter {
   final NetSuitePassport passport;
   private final NetSuiteSelectedObject selectedObject;
   private final ConnectorNetSuiteNamespaces ns;
   private final NetSuiteRecordRefWrapper recordRef;

   public ConnectorNetSuiteExecutionAdapter(NetSuitePassport var1, NetSuiteSelectedObject var2) {
      this.passport = var1;
      this.selectedObject = var2;
      NetSuiteDefinitionExecution var3 = NetSuiteExecutionContext.INSTANCE.getExecution(var1);
      this.ns = var3.getConnectorNetSuiteNamespaces();
      this.recordRef = new NetSuiteRecordRefWrapper(var3.getDefinitionConnector().getRecordRefDeriveType());
   }

   public void executeConnectorFileUtf8(String var1, String var2, String var3) {
      switch (this.selectedObject.getOperationEntity()) {
         case NetSuiteCreate:
            this.executeConnectorFileUtf8_NonDelete(var2, var3);
            break;
         case NetSuiteUpdate:
            this.executeConnectorFileUtf8_NonDelete(var2, var3);
            break;
         case NetSuiteUpsert:
            this.executeConnectorFileUtf8_NonDelete(var2, var3);
            break;
         case NetSuiteDelete:
            this.executeConnectorFileUtf8_Delete(var2, var3);
            break;
         case NetSuiteQuery:
            this.executeConnectorFileUtf8_Search(var1, var2, var3);
            break;
         default:
            throw new ConnectorServerNotSupportedRuntimeException("Not supported netsuite operation " + this.selectedObject.getOperationEntity());
      }
   }

   private void executeConnectorFileUtf8_NonDelete(String var1, String var2) {
      NetSuiteObjectsCreator var3 = newNetSuiteObjectsCreator(this.passport);
      NetSuiteObject var4 = var3.getNSConnectorObjects(this.selectedObject).asRequestObject();
      String var5 = FileUtils.getPostfixFilenameXml(var1, "_req");
      NetSuiteRecordRequest var6 = new NetSuiteRecordRequest(this.ns, var4, this.recordRef);
      var6.transform(var1, var5);
      this.executeConnectorFileUtf8Res(var5, var2);
   }

   private void executeConnectorFileUtf8_Delete(String var1, String var2) {
      String var3 = FileUtils.getPostfixFilenameXml(var1, "_req");
      NetSuiteBaseRefRequest var4 = new NetSuiteBaseRefRequest(this.ns);
      var4.transform(var1, var3);
      this.executeConnectorFileUtf8Res(var3, var2);
   }

   private void executeConnectorFileUtf8Res(String var1, String var2) {
      String var3 = FileUtils.getPostfixFilenameXml(var2, "_res");
      NetSuiteSoapServiceExecution var4 = new NetSuiteSoapServiceExecution(this.passport);
      var4.executeFile(this.selectedObject.getNetSuiteOperation(), var1, var3);
      NetSuiteBaseRefResponse.transform(this.ns, var3, var2);
   }

   private void executeConnectorFileUtf8_Search(String var1, String var2, String var3) {
      NetSuiteSearchPreference var4 = new NetSuiteSearchPreference(var1);
      NetSuiteSearchOperation var5 = new NetSuiteSearchOperation(this.passport, var4, this.selectedObject.responseIsSearchRow());
      NetSuiteObjectsCreator var6 = newNetSuiteObjectsCreator(this.passport);
      NetSuiteObject var7 = var6.getNSConnectorObjects(this.selectedObject).asCustomObject();
      var5.search(this.recordRef, var7, var2, var3);
   }

   public static NetSuiteObjectsCreator newNetSuiteObjectsCreator(NetSuitePassport var0) {
      NetSuiteDefinitionExecution var1 = NetSuiteExecutionContext.INSTANCE.getExecution(var0);
      return new NetSuiteObjectsCreator(var1.getDefinitionConnector(), var1.getNetSuiteConfig());
   }
}
