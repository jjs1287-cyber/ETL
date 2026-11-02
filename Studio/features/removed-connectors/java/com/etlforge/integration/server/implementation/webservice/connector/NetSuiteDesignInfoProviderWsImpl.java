package com.etlforge.integration.server.implementation.webservice.connector;

import java.io.File;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.connector.netsuite.serverapi.GetNetSuiteCustomFieldsCallBack;
import org.etlforge.integration.client.connector.netsuite.serverapi.GetNetSuiteDefinitionContentsCallback;
import org.etlforge.integration.client.connector.netsuite.serverapi.GetNetSuiteSearchCustomFieldMetaDataCallBack;
import org.etlforge.integration.client.connector.netsuite.serverapi.GetNetSuiteSearchMetaDataCallBack;
import org.etlforge.integration.client.connector.netsuite.serverapi.GetNetSuiteSearchObjectSavedSearchCallBack;
import org.etlforge.integration.client.connector.netsuite.serverapi.NetSuiteDesignInfoProvider;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.GetSchemasCallback;
import org.etlforge.integration.client.connector.serverapi.ListObjectsCallback;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.JavaWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.JavaWebServiceServerCall;
import org.etlforge.integration.client.server.webservice.WebServiceConfiguration;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.server.backend.webservice.netsuite.client.NetSuiteDesignEngine;
import org.etlforge.integration.server.backend.webservice.netsuite.client.NetSuiteDesignEngineServiceLocator;
import org.etlforge.integration.server.backend.webservice.netsuite.client.WsEndpointParameters;
import org.etlforge.integration.server.backend.webservice.netsuite.client.WsNetSuiteDefinitionContents;
import org.etlforge.integration.server.backend.webservice.netsuite.client.WsNetSuiteObjectCustomFieldsNew;
import org.etlforge.integration.server.backend.webservice.netsuite.client.WsNetSuiteSavedSearch;
import org.etlforge.integration.server.backend.webservice.netsuite.client.WsStatus;

public final class NetSuiteDesignInfoProviderWsImpl extends JavaWebServiceServerCall implements NetSuiteDesignInfoProvider {
   private static final String WS_NAME = "konga_netsuitedesign_engine";
   private final ConnectorCallInfoProvider delegate;

   public NetSuiteDesignInfoProviderWsImpl(JavaWebServiceConfiguration var1, ConnectorCallInfoProvider var2) {
      super(var1);
      this.delegate = var2;
   }

   @Override
   public void getSearchMetaData(GetNetSuiteSearchMetaDataCallBack var1) {
   }

   @Override
   public void getNetSuiteSearchCustomFieldMetaData(NetSuiteEndpoint var1, String var2, GetNetSuiteSearchCustomFieldMetaDataCallBack var3) {
   }

   @Override
   public void getNetSuiteDefinitionContents(NetSuiteEndpoint var1, GetNetSuiteDefinitionContentsCallback var2) {
      NetSuiteDesignInfoProviderWsImpl.DesignMetaDataWorker var3 = new NetSuiteDesignInfoProviderWsImpl.DesignMetaDataWorker(this.getConfiguration());
      var3.getNetSuiteDefinitionContents(var1, var2);
   }

   @Override
   public void getNetSuiteSearchObjectSavedSearch(NetSuiteEndpoint var1, String var2, GetNetSuiteSearchObjectSavedSearchCallBack var3) {
      NetSuiteDesignInfoProviderWsImpl.DesignMetaDataWorker var4 = new NetSuiteDesignInfoProviderWsImpl.DesignMetaDataWorker(this.getConfiguration());
      var4.getNetSuiteSearchObjectSavedSearch(var1, var2, var3);
   }

   @Override
   public void getNetSuiteCustomFieldsNew(NetSuiteEndpoint var1, boolean var2, String var3, GetNetSuiteCustomFieldsCallBack var4) {
      NetSuiteDesignInfoProviderWsImpl.DesignMetaDataWorker var5 = new NetSuiteDesignInfoProviderWsImpl.DesignMetaDataWorker(this.getConfiguration());
      var5.getNetSuiteCustomFieldsNew(var1, var2, var3, var4);
   }

   @Override
   public void getObjectList(ConnectorEndpoint var1, List<ConnectorObject> var2, ListObjectsCallback var3) {
      this.delegate.getObjectList(var1, var2, var3);
   }

   @Override
   public void getSingleSchemas(ConnectorEndpoint var1, List<ConnectorObject> var2, GetSchemasCallback var3) {
      this.delegate.getSingleSchemas(var1, var2, var3);
   }

   @Override
   public void getMultipleSchemas(ConnectorEndpoint var1, List<ConnectorObject> var2, boolean var3, GetSchemasCallback var4) {
      this.delegate.getMultipleSchemas(var1, var2, var3, var4);
   }

   @Override
   public void uploadFile(File var1, ServerFileFolder var2, String var3, UploadServerFileCallback var4) {
      this.delegate.uploadFile(var1, var2, var3, var4);
   }

   private static class DesignMetaDataWorker {
      private final WebServiceConfiguration config;

      public DesignMetaDataWorker(WebServiceConfiguration var1) {
         this.config = var1;
      }

      public void getNetSuiteSearchObjectSavedSearch(NetSuiteEndpoint var1, String var2, GetNetSuiteSearchObjectSavedSearchCallBack var3) {
         try {
            NetSuiteDesignEngine var4 = this.getServerProxy(var3);
            if (var4 == null) {
               return;
            }

            WsEndpointParameters var5 = this.createWsEndpointParams(var1);
            WsNetSuiteSavedSearch var6 = var4.getSavedSearch(var5, var2);
            WsStatus var7 = var6.getStatus();
            if (var7.getStatusCode() < 0) {
               var3.caught(new Exception(var7.getMessage()));
            } else {
               Map var8 = NetSuiteDesignInfoProviderHelper.convertToSavedSearch(var6);
               var3.gotNetSuiteSearchObjectSavedSearch(var8);
            }
         } catch (RemoteException var9) {
            var3.caught(this.convert(var9));
         } catch (ServiceException var10) {
            var3.caught(new IntegrationServerException("Failed to call webservice: " + var10.getMessage(), var10));
         }
      }

      public void getNetSuiteDefinitionContents(NetSuiteEndpoint var1, GetNetSuiteDefinitionContentsCallback var2) {
         try {
            NetSuiteDesignEngine var3 = this.getServerProxy(var2);
            if (var3 == null) {
               return;
            }

            WsEndpointParameters var4 = this.createWsEndpointParams(var1);
            WsNetSuiteDefinitionContents var5 = var3.getNetSuiteDefinitionContents(var4);
            WsStatus var6 = var5.getStatus();
            if (var6.getStatusCode() < 0) {
               var2.caught(new Exception(var6.getMessage()));
            } else {
               var2.gotNetSuiteDefinitionContents(NetSuiteDesignInfoProviderHelper.convert(var5));
            }
         } catch (RemoteException var7) {
            var2.caught(this.convert(var7));
         } catch (ServiceException var8) {
            var2.caught(new IntegrationServerException("Failed to call webservice: " + var8.getMessage(), var8));
         }
      }

      public void getNetSuiteCustomFieldsNew(NetSuiteEndpoint var1, boolean var2, String var3, GetNetSuiteCustomFieldsCallBack var4) {
         try {
            NetSuiteDesignEngine var5 = this.getServerProxy(var4);
            if (var5 == null) {
               return;
            }

            WsEndpointParameters var6 = this.createWsEndpointParams(var1);
            WsNetSuiteObjectCustomFieldsNew var7 = var5.getNetSuiteCustomFieldsNew(var6, var2, var3);
            WsStatus var8 = var7.getStatus();
            if (var8.getStatusCode() < 0) {
               var4.caught(new Exception(var8.getMessage()));
            } else {
               var4.gotNetSuiteCustomFields(NetSuiteDesignInfoProviderHelper.convert(var7));
            }
         } catch (RemoteException var9) {
            var4.caught(this.convert(var9));
         } catch (ServiceException var10) {
            var4.caught(new IntegrationServerException("Failed to call webservice: " + var10.getMessage(), var10));
         }
      }

      private NetSuiteDesignEngine getServerProxy(ServerCallback var1) throws ServiceException {
         NetSuiteDesignEngineServiceLocator var2 = new NetSuiteDesignEngineServiceLocator();
         StringBuilder var3 = new StringBuilder();
         if (this.config.configureWebservice(var2, "konga_netsuitedesign_engine", var3, var1)) {
            var2.setkonga_netsuitedesign_engineEndpointAddress(var3.toString());
            return var2.getkonga_netsuitedesign_engine();
         } else {
            return null;
         }
      }

      private IntegrationServerException convert(RemoteException var1) {
         return this.config.convertToIntegrationServerException(var1);
      }

      private final WsEndpointParameters createWsEndpointParams(ConnectorEndpoint var1) {
         String var2 = this.config.getUserName();
         String var3 = this.config.getPasswordHash();
         return NetSuiteDesignInfoProviderHelper.createWsEndpointParams(var2, var3, var1);
      }
   }
}
