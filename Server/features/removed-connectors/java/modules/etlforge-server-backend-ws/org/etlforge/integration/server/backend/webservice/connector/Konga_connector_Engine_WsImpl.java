package org.etlforge.integration.server.backend.webservice.connector;

import com.google.common.base.Strings;
import java.io.IOException;
import java.rmi.RemoteException;
import org.etlforge.util.zip.ZipUtils;

public class Konga_connector_Engine_WsImpl {
   private final ConnectorEngineWsAdapterWrapper wrapper = new ConnectorEngineWsAdapterWrapper(true);

   public WsStatus testConnection(WsEndpointParameters var1) {
      return this.wrapper.testConnection(var1);
   }

   public WsConnectorObjectList getConnectorObjectList(WsConnectorInput var1, String var2) {
      return this.wrapper.getConnectorObjectList(var1, var2);
   }

   private String getSchema(String var1, WsConnectorSchemaFiles var2) {
      for (WsConnectorSchemaFile var6 : var2.getSchemaFiles()) {
         if (var1.equals(var6.getSchemaFileName())) {
            return var6.getSchemaContent();
         }
      }

      return "";
   }

   public WsConnectorSchemas getConnectorSchemas(WsConnectorInput var1) {
      WsConnectorSchemaFiles var2 = this.getConnectorSchemaFiles(true, var1);
      String var3 = this.getSchema(var2.getRequestMainSchema(), var2);
      String var4 = this.getSchema(var2.getResponseMainSchema(), var2);
      return new WsConnectorSchemas(var2.getStatus(), var3, var2.getRequestRootElementExpandedName(), var4, var2.getResponseRootElementExpandedName());
   }

   public WsConnectorSchemaFiles getConnectorSchemaFiles(boolean var1, WsConnectorInput var2) {
      WsConnectorSchemaFiles var3 = this.wrapper.getConnectorSchemaFiles(var1, var2);
      WsStatus var4 = var3.getStatus();
      if (var4.getStatusCode() == 0) {
         String var5 = var3.getRequestRootElementExpandedName();
         if (Strings.isNullOrEmpty(var5)) {
            var3.setRequestRootElementExpandedName("");
            var3.setRequestMainSchema("");
         }

         this.deflateAndBase64EncodeString(var4, var3.getSchemaFiles());
      }

      return var3;
   }

   private void deflateAndBase64EncodeString(WsStatus var1, WsConnectorSchemaFile[] var2) {
      for (WsConnectorSchemaFile var6 : var2) {
         var6.setSchemaContent(this.deflateAndBase64EncodeString(var1, var6.getSchemaContent()));
      }
   }

   private String deflateAndBase64EncodeString(WsStatus var1, String var2) {
      try {
         return ZipUtils.deflateAndBase64EncodeString(var2);
      } catch (IOException var4) {
         var4.printStackTrace();
         var1.setStatusCode(-99);
         var1.setMessage("fail to zip the schema content: " + var4.getMessage());
         return "";
      }
   }

   public WsConnectorExecutionResponse executeConnector(WsConnectorInput var1, String var2) {
      return this.wrapper.executeConnector(var1, var2);
   }

   public WsStatus executeConnectorFile(WsConnectorInput var1, String var2, String var3) {
      return this.wrapper.executeConnectorFileUtf8(var1, var2, var3);
   }

   public WsConnectorAutoDeskItemInfo getAutoDeskItemVersionId(WsEndpointParameters var1, String var2, String var3) {
      return this.wrapper.getAutoDeskItemVersionId(var1, var2, var3);
   }

   public WsConnectorSessionResult login(WsEndpointParameters var1) throws RemoteException {
      return this.wrapper.login(var1);
   }

   public WsStatus logout(WsEndpointParameters var1) throws RemoteException {
      return this.wrapper.logout(var1);
   }
}
