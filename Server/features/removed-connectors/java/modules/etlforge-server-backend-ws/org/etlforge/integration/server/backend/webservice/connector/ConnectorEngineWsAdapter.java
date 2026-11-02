package org.etlforge.integration.server.backend.webservice.connector;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.server.engine.connector.ConnectorEngineExecutionWrapper;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;

public class ConnectorEngineWsAdapter {
   private static final WsStatus OKStatus = new WsStatus();
   final boolean checkValidCall;

   ConnectorEngineWsAdapter(boolean var1) {
      this.checkValidCall = var1;
   }

   public WsStatus testConnection(WsEndpointParameters var1) {
      ConnectorEngineExecutionWrapper var2 = this.obtainExecutionWrapper(false, var1);
      boolean var3 = var2.testConnection();
      return var3 ? OKStatus : null;
   }

   public WsConnectorObjectList getConnectorObjectList(WsConnectorInput var1, String var2) {
      ConnectorEngineExecutionWrapper var3 = this.obtainExecutionWrapper(false, var1);
      ConnectorObjectList var4 = var3.getConnectorObjectList(this.selectedObjectNamesToList(var1), var2);
      Map var5 = var4.getObjectList();
      WsConnectorObject[] var6 = new WsConnectorObject[var5.size()];
      int var7 = 0;

      for (Object __etlItem33 : var5.entrySet()) {
         Entry var9 = (Entry) __etlItem33;
         var6[var7++] = new WsConnectorObject((String)var9.getKey(), (String)var9.getValue());
      }

      return new WsConnectorObjectList(OKStatus, var6);
   }

   private WsConnectorSchemaFile[] convert(Map<String, String> var1) {
      WsConnectorSchemaFile[] var2 = new WsConnectorSchemaFile[var1.size()];
      int var3 = 0;

      for (Entry var5 : var1.entrySet()) {
         var2[var3++] = new WsConnectorSchemaFile((String)var5.getKey(), (String)var5.getValue());
      }

      return var2;
   }

   public WsConnectorSchemaFiles getConnectorSchemaFiles(boolean var1, WsConnectorInput var2) throws IOException {
      ConnectorEngineExecutionWrapper var3 = this.obtainExecutionWrapper(false, var2);
      ConnectorXsdSchemaFiles var4 = var3.getConnectorSchemaFiles(var1, this.selectedObjectNamesToList(var2));
      Map var5 = var4.getSchemaFileMaps();
      WsConnectorSchemaFile[] var6 = this.convert(var5);
      return new WsConnectorSchemaFiles(
         OKStatus,
         var4.getRequestMainSchemaFilename(),
         var4.getRequestRootElementExpandedName(),
         var4.getResponseMainSchemaFilename(),
         var4.getResponseRootElementExpandedName(),
         var6
      );
   }

   public WsConnectorExecutionResponse executeConnector(WsConnectorInput var1, String var2) {
      ConnectorEngineExecutionWrapper var3 = this.obtainExecutionWrapper(false, var1);
      String var4 = var3.executeConnector(this.selectedObjectNamesToList(var1), var2);
      return new WsConnectorExecutionResponse(OKStatus, var4);
   }

   public WsStatus executeConnectorFileUtf8(WsConnectorInput var1, String var2, String var3) {
      ConnectorEngineExecutionWrapper var4 = this.obtainExecutionWrapper(true, var1);
      var4.executeConnectorFileUtf8(this.selectedObjectNamesToList(var1), var2, var3);
      return OKStatus;
   }

   public WsConnectorAutoDeskItemInfo getAutoDeskItemVersionId(WsEndpointParameters var1, String var2, String var3) {
      ConnectorEngineExecutionWrapper var4 = this.obtainExecutionWrapper(true, var1);
      String var5 = var4.getAutoDeskItemVersionId(var2, var3);
      return new WsConnectorAutoDeskItemInfo(OKStatus, var5);
   }

   public WsConnectorSessionResult login(WsEndpointParameters var1) {
      ConnectorEngineExecutionWrapper var2 = this.obtainExecutionWrapper(true, var1);
      String var3 = var2.login();
      return new WsConnectorSessionResult(OKStatus, var3);
   }

   public WsStatus logout(WsEndpointParameters var1) {
      ConnectorEngineExecutionWrapper var2 = this.obtainExecutionWrapper(true, var1);
      var2.logout();
      return OKStatus;
   }

   private ConnectorEngineExecutionWrapper obtainExecutionWrapper(boolean var1, WsConnectorInput var2) {
      return this.obtainExecutionWrapper(var1, var2.getEndpointParameters());
   }

   private ConnectorEngineExecutionWrapper obtainExecutionWrapper(boolean var1, WsEndpointParameters var2) {
      return ConnectorEngineExecutionWrapperFactory.obtainExecutionWrapper(this.checkValidCall, var2, var1);
   }

   private List<String> selectedObjectNamesToList(WsConnectorInput var1) {
      return Lists.newArrayList(var1.getSelectedObjectNames());
   }
}
