package org.etlforge.integration.server.backend.webservice.connector;

public class ConnectorEngineWsAdapterWrapper {
   static final WsConnectorObject[] connectorObjectListDummy = new WsConnectorObject[0];
   final ConnectorEngineWsExceptionUtils exceptionUtil;
   final ConnectorEngineWsAdapter wsadapter;

   ConnectorEngineWsAdapterWrapper(boolean var1) {
      this.wsadapter = new ConnectorEngineWsAdapter(var1);
      this.exceptionUtil = new ConnectorEngineWsExceptionUtils();
   }

   public WsStatus testConnection(WsEndpointParameters var1) {
      try {
         WsStatus var2 = this.wsadapter.testConnection(var1);
         return var2 == null ? new WsStatus(401, "failed to connect with " + this.exceptionUtil.toString(var1)) : var2;
      } catch (Throwable var3) {
         var3.printStackTrace();
         return this.exceptionUtil.obtainBadWsStatus(var3, var1, null, null);
      }
   }

   public WsConnectorObjectList getConnectorObjectList(WsConnectorInput var1, String var2) {
      try {
         return this.wsadapter.getConnectorObjectList(var1, var2);
      } catch (Throwable var4) {
         return new WsConnectorObjectList(this.exceptionUtil.obtainBadWsStatus(var4, var1), connectorObjectListDummy);
      }
   }

   public WsConnectorSchemaFiles getConnectorSchemaFiles(boolean var1, WsConnectorInput var2) {
      try {
         return this.wsadapter.getConnectorSchemaFiles(var1, var2);
      } catch (Throwable var5) {
         WsConnectorSchemaFiles var4 = new WsConnectorSchemaFiles();
         var4.setStatus(this.exceptionUtil.obtainBadWsStatus(var5, var2));
         return var4;
      }
   }

   public WsConnectorExecutionResponse executeConnector(WsConnectorInput var1, String var2) {
      try {
         return this.wsadapter.executeConnector(var1, var2);
      } catch (Throwable var5) {
         WsConnectorExecutionResponse var4 = new WsConnectorExecutionResponse();
         var4.setStatus(this.exceptionUtil.obtainBadWsStatus(var5, var1));
         return var4;
      }
   }

   public WsStatus executeConnectorFileUtf8(WsConnectorInput var1, String var2, String var3) {
      try {
         return this.wsadapter.executeConnectorFileUtf8(var1, var2, var3);
      } catch (Throwable var5) {
         return this.exceptionUtil.obtainBadWsStatus(var5, var1, var2);
      }
   }

   public WsConnectorAutoDeskItemInfo getAutoDeskItemVersionId(WsEndpointParameters var1, String var2, String var3) {
      try {
         return this.wsadapter.getAutoDeskItemVersionId(var1, var2, var3);
      } catch (Throwable var8) {
         WsConnectorAutoDeskItemInfo var5 = new WsConnectorAutoDeskItemInfo();
         String var6 = "workspaceId=" + var2 + ";dmsid=" + var3;
         WsStatus var7 = this.exceptionUtil.obtainBadWsStatus(var8, var1, null, var6);
         var5.setStatus(var7);
         return var5;
      }
   }

   public WsConnectorSessionResult login(WsEndpointParameters var1) {
      try {
         return this.wsadapter.login(var1);
      } catch (Throwable var5) {
         WsConnectorSessionResult var3 = new WsConnectorSessionResult();
         WsStatus var4 = this.exceptionUtil.obtainBadWsStatus(var5, var1, null, null);
         var3.setStatus(var4);
         return var3;
      }
   }

   public WsStatus logout(WsEndpointParameters var1) {
      try {
         return this.wsadapter.logout(var1);
      } catch (Throwable var3) {
         return this.exceptionUtil.obtainBadWsStatus(var3, var1, null, null);
      }
   }
}
