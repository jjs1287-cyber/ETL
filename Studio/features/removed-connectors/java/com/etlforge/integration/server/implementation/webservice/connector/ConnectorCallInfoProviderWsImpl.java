package com.etlforge.integration.server.implementation.webservice.connector;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import java.io.File;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.GetSchemasCallback;
import org.etlforge.integration.client.connector.serverapi.ListObjectsCallback;
import org.etlforge.integration.client.connector.serverapi.MultipleSchemas;
import org.etlforge.integration.client.connector.serverapi.SingleSchemas;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.webservice.JavaWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.JavaWebServiceServerCall;
import org.etlforge.integration.client.server.webservice.WebServiceConfiguration;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;
import org.etlforge.integration.etlforgeconnector.AutoDeskConnectorObject;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.etlforgeconnector.SchemaData;
import org.etlforge.integration.etlforgeconnector.SimpleConnectorObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.server.backend.webservice.connector.client.ETLForgeConnectorEngine;
import org.etlforge.integration.server.backend.webservice.connector.client.WsConnectorInput;
import org.etlforge.integration.server.backend.webservice.connector.client.WsConnectorObject;
import org.etlforge.integration.server.backend.webservice.connector.client.WsConnectorObjectList;
import org.etlforge.integration.server.backend.webservice.connector.client.WsConnectorSchemaFile;
import org.etlforge.integration.server.backend.webservice.connector.client.WsConnectorSchemaFiles;
import org.etlforge.integration.server.backend.webservice.connector.client.WsConnectorSchemas;
import org.etlforge.integration.server.backend.webservice.connector.client.WsStatus;
import org.etlforge.util.zip.CompressedAndBase64EncodedString;

public final class ConnectorCallInfoProviderWsImpl extends JavaWebServiceServerCall implements ConnectorCallInfoProvider {
   private final FileUploadApi fileUploadApi;

   public ConnectorCallInfoProviderWsImpl(JavaWebServiceConfiguration var1, FileUploadApi var2) {
      super(var1);
      this.fileUploadApi = var2;
   }

   @Override
   public void uploadFile(File var1, ServerFileFolder var2, String var3, UploadServerFileCallback var4) {
      this.fileUploadApi.uploadFile(var1, var2, var3, var4);
   }

   @Override
   public void getObjectList(ConnectorEndpoint var1, List<ConnectorObject> var2, ListObjectsCallback var3) {
      ConnectorCallInfoProviderWsImpl.Worker var4 = new ConnectorCallInfoProviderWsImpl.Worker(this.getConfiguration());
      var4.getObjectList(var1, var2, var3);
   }

   @Override
   public void getSingleSchemas(ConnectorEndpoint var1, List<ConnectorObject> var2, GetSchemasCallback var3) {
      ConnectorCallInfoProviderWsImpl.Worker var4 = new ConnectorCallInfoProviderWsImpl.Worker(this.getConfiguration());
      var4.getSingleSchemas(var1, var2, var3);
   }

   @Override
   public void getMultipleSchemas(ConnectorEndpoint var1, List<ConnectorObject> var2, boolean var3, GetSchemasCallback var4) {
      ConnectorCallInfoProviderWsImpl.Worker var5 = new ConnectorCallInfoProviderWsImpl.Worker(this.getConfiguration());
      var5.getMultipleSchemas(var1, var2, var3, var4);
   }

   private static class ConnectorObjectTransform implements Function<WsConnectorObject, ConnectorObject> {
      private final EntityType endpointEntity;

      public ConnectorObjectTransform(EntityType var1) {
         this.endpointEntity = var1;
      }

      public ConnectorObject apply(WsConnectorObject var1) {
         switch (this.endpointEntity) {
            case NetSuiteEndpoint:
               return new NetSuiteConnectorObject(var1.getObjectName(), var1.getObjectDesciption());
            case AutoDeskEndpoint:
               return new AutoDeskConnectorObject(var1.getObjectName(), var1.getObjectDesciption());
            default:
               return new SimpleConnectorObject(var1.getObjectName(), var1.getObjectDesciption());
         }
      }
   }

   private static class Worker extends ConnectorWsBase {
      public Worker(WebServiceConfiguration var1) {
         super(var1);
      }

      public void getObjectList(ConnectorEndpoint var1, List<ConnectorObject> var2, ListObjectsCallback var3) {
         try {
            ETLForgeConnectorEngine var4 = this.getServerProxy(var3);
            if (var4 == null) {
               return;
            }

            WsConnectorInput var5 = this.createWsConnectorInput(var1, var2);
            WsConnectorObjectList var6 = var4.getConnectorObjectList(var5, "");
            WsStatus var7 = var6.getStatus();
            if (var7.getStatusCode() < 0) {
               var3.caught(new Exception(var7.getMessage()));
            } else {
               var3.handleObjectList(this.transformObjectList(var1.getEntityType(), var6));
            }
         } catch (RemoteException var8) {
            var3.caught(this.convert(var8));
         } catch (ServiceException var9) {
            var3.caught(new IntegrationServerException("Failed to call webservice: " + var9.getMessage(), var9));
         }
      }

      private Iterable<ConnectorObject> transformObjectList(EntityType var1, WsConnectorObjectList var2) {
         return FluentIterable.from(Arrays.asList(var2.getObjectList()))
            .transform(new ConnectorCallInfoProviderWsImpl.ConnectorObjectTransform(var1))
            .toImmutableList();
      }

      public void getSingleSchemas(ConnectorEndpoint var1, List<ConnectorObject> var2, GetSchemasCallback var3) {
         try {
            ETLForgeConnectorEngine var4 = this.getServerProxy(var3);
            if (var4 == null) {
               return;
            }

            WsConnectorInput var5 = this.createWsConnectorInput(var1, var2);
            WsConnectorSchemas var6 = var4.getConnectorSchemas(var5);
            WsStatus var7 = var6.getStatus();
            if (var7.getStatusCode() < 0) {
               var3.caught(new Exception(var7.getMessage()));
            } else {
               SingleSchemas var8 = this.convertReturnedSchemas(var6);
               var3.handleSchemas(var8);
            }
         } catch (RemoteException var9) {
            var3.caught(this.convert(var9));
         } catch (ServiceException var10) {
            var3.caught(new IntegrationServerException("Failed to call webservice: " + var10.getMessage(), var10));
         }
      }

      private SingleSchemas convertReturnedSchemas(WsConnectorSchemas var1) {
         SchemaData var2 = SchemaData.fromCompressedContents(var1.getRequestRootElementExpandedName(), var1.getRequestXsdSchema());
         SchemaData var3 = SchemaData.fromCompressedContents(var1.getResponseRootElementExpandedName(), var1.getResponseXsdSchema());
         return new SingleSchemas(var2, var3);
      }

      public void getMultipleSchemas(ConnectorEndpoint var1, List<ConnectorObject> var2, boolean var3, GetSchemasCallback var4) {
         try {
            ETLForgeConnectorEngine var5 = this.getServerProxy(var4);
            if (var5 == null) {
               return;
            }

            WsConnectorInput var6 = this.createWsConnectorInput(var1, var2);
            boolean var7 = var3;
            WsConnectorSchemaFiles var8 = var5.getConnectorSchemaFiles(var7, var6);
            WsStatus var9 = var8.getStatus();
            if (var9.getStatusCode() < 0) {
               var4.caught(new Exception(var9.getMessage()));
            } else {
               var4.handleSchemas(convertSchemaFiles(var8));
            }
         } catch (RemoteException var10) {
            var4.caught(this.convert(var10));
         } catch (ServiceException var11) {
            var4.caught(new IntegrationServerException("Failed to call webservice: " + var11.getMessage(), var11));
         }
      }

      private static MultipleSchemas convertSchemaFiles(WsConnectorSchemaFiles var0) {
         HashMap var1 = Maps.newHashMap();

         for (WsConnectorSchemaFile var5 : var0.getSchemaFiles()) {
            var1.put(var5.getSchemaFileName(), new CompressedAndBase64EncodedString(var5.getSchemaContent()));
         }

         return new MultipleSchemas(
            var0.getRequestMainSchema(),
            var0.getRequestRootElementExpandedName(),
            var0.getResponseMainSchema(),
            var0.getResponseRootElementExpandedName(),
            var1
         );
      }
   }
}
