package com.etlforge.integration.server.implementation.webservice.connector;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.zip.DataFormatException;
import javax.xml.rpc.ServiceException;
import org.apache.commons.lang.SerializationUtils;
import org.etlforge.integration.client.connector.mscrm.serverapi.GetDetailedMetaDataCallback;
import org.etlforge.integration.client.connector.mscrm.serverapi.GetObjectDescriptionsCallback;
import org.etlforge.integration.client.connector.mscrm.serverapi.GetResponseSchemaCallback;
import org.etlforge.integration.client.connector.mscrm.serverapi.GetSavedQueriesCallback;
import org.etlforge.integration.client.connector.mscrm.serverapi.MSCrmDesignInfoProvider;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.JavaWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.JavaWebServiceServerCall;
import org.etlforge.integration.client.server.webservice.WebServiceConfiguration;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.integration.etlforgeconnector.SchemaData;
import org.etlforge.integration.etlforgeconnector.UserCredentials;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.query.FetchXml;
import org.etlforge.integration.server.backend.webservice.mscrm.client.MSCrmDesignEngine;
import org.etlforge.integration.server.backend.webservice.mscrm.client.MSCrmDesignEngineServiceLocator;
import org.etlforge.integration.server.backend.webservice.mscrm.client.WsEndpointParameters;
import org.etlforge.integration.server.backend.webservice.mscrm.client.WsFetchXml;
import org.etlforge.integration.server.backend.webservice.mscrm.client.WsMetaDataResponse;
import org.etlforge.integration.server.backend.webservice.mscrm.client.WsNameValuePair;
import org.etlforge.integration.server.backend.webservice.mscrm.client.WsQueryResponseSchema;
import org.etlforge.integration.server.backend.webservice.mscrm.client.WsSavedQueries;
import org.etlforge.integration.server.backend.webservice.mscrm.client.WsSchema;
import org.etlforge.integration.server.backend.webservice.mscrm.client.WsStatus;
import org.etlforge.util.zip.CompressedAndBase64EncodedString;
import org.etlforge.util.zip.ZipUtils;

public final class MSCrmDesignInfoProviderWsImpl extends JavaWebServiceServerCall implements MSCrmDesignInfoProvider {
   private static final String WS_NAME = "konga_mscrmdesign_engine";
   private final FileUploadApi fileUploadApi;

   public MSCrmDesignInfoProviderWsImpl(JavaWebServiceConfiguration var1, FileUploadApi var2) {
      super(var1);
      this.fileUploadApi = var2;
   }

   @Override
   public void uploadFile(File var1, ServerFileFolder var2, String var3, UploadServerFileCallback var4) {
      this.fileUploadApi.uploadFile(var1, var2, var3, var4);
   }

   @Override
   public void getObjectDescriptions(MSCrmEndpoint var1, GetObjectDescriptionsCallback var2) {
      MSCrmDesignInfoProviderWsImpl.Worker var3 = new MSCrmDesignInfoProviderWsImpl.Worker(this.getConfiguration());
      var3.getObjectDescriptions(var1, var2);
   }

   @Override
   public void getDetailedMetaData(MSCrmEndpoint var1, String var2, GetDetailedMetaDataCallback var3) {
      MSCrmDesignInfoProviderWsImpl.Worker var4 = new MSCrmDesignInfoProviderWsImpl.Worker(this.getConfiguration());
      var4.getDetailedMetaData(var1, var2, var3);
   }

   @Override
   public void getSavedQueries(MSCrmEndpoint var1, String var2, GetSavedQueriesCallback var3) {
      MSCrmDesignInfoProviderWsImpl.Worker var4 = new MSCrmDesignInfoProviderWsImpl.Worker(this.getConfiguration());
      var4.getSavedQueries(var1, var2, var3);
   }

   @Override
   public void getQueryResponseSchema(MSCrmEndpoint var1, String var2, GetResponseSchemaCallback var3) {
      MSCrmDesignInfoProviderWsImpl.Worker var4 = new MSCrmDesignInfoProviderWsImpl.Worker(this.getConfiguration());
      var4.getQueryResponseSchema(var1, var2, var3);
   }

   private static class FetchXmlDeserializer implements Function<WsFetchXml, FetchXml> {
      private FetchXmlDeserializer() {
      }

      public FetchXml apply(WsFetchXml var1) {
         return FetchXml.compressed(var1.getName(), new CompressedAndBase64EncodedString(var1.getCompressedContents()));
      }
   }

   private static class Worker {
      private final WebServiceConfiguration config;

      public Worker(WebServiceConfiguration var1) {
         this.config = var1;
      }

      public void getObjectDescriptions(MSCrmEndpoint var1, GetObjectDescriptionsCallback var2) {
         try {
            MSCrmDesignEngine var3 = this.getServerProxy(var2);
            if (var3 == null) {
               return;
            }

            WsEndpointParameters var4 = this.createWsEndpointParams(var1);
            WsMetaDataResponse var5 = var3.getObjectDescriptions(this.config.getUserName(), this.config.getPasswordHash(), var4);
            List var6 = deserializeMetaData(var5.getSerializedData());
            var2.gotDescriptions(ImmutableList.copyOf(var6));
         } catch (RemoteException var7) {
            var2.caught(this.convert(var7));
         } catch (DataFormatException var8) {
            var2.caught(new Exception("The ETLForge server sent data in an unexpected format.", var8));
         } catch (ServiceException var9) {
            var2.caught(new IntegrationServerException("Failed to call webservice: " + var9.getMessage(), var9));
         }
      }

      public void getDetailedMetaData(MSCrmEndpoint var1, String var2, GetDetailedMetaDataCallback var3) {
         try {
            MSCrmDesignEngine var4 = this.getServerProxy(var3);
            if (var4 == null) {
               return;
            }

            WsEndpointParameters var5 = this.createWsEndpointParams(var1);
            WsMetaDataResponse var6 = var4.getDetailedMetaData(this.config.getUserName(), this.config.getPasswordHash(), var5, var2);
            List var7 = deserializeMetaData(var6.getSerializedData());
            var3.gotMetaData((MSCrmObject)var7.get(0));
         } catch (RemoteException var8) {
            var3.caught(this.convert(var8));
         } catch (DataFormatException var9) {
            var3.caught(new Exception("The ETLForge server sent data in an unexpected format.", var9));
         } catch (ServiceException var10) {
            var3.caught(new IntegrationServerException("Failed to call webservice: " + var10.getMessage(), var10));
         }
      }

      private static <T> List<T> deserializeMetaData(String var0) throws DataFormatException {
         byte[] var1 = ZipUtils.base64DecodeAndInflateStringAsBytes(var0);
         ArrayList var2 = (ArrayList)SerializationUtils.deserialize(var1);
         return ImmutableList.copyOf(var2);
      }

      public void getSavedQueries(MSCrmEndpoint var1, String var2, GetSavedQueriesCallback var3) {
         try {
            MSCrmDesignEngine var4 = this.getServerProxy(var3);
            if (var4 == null) {
               return;
            }

            WsEndpointParameters var5 = this.createWsEndpointParams(var1);
            WsSavedQueries var6 = var4.getSavedQueries(this.config.getUserName(), this.config.getPasswordHash(), var5, var2);
            WsStatus var7 = var6.getStatus();
            if (var7.getStatusCode() < 0) {
               var3.caught(new Exception(var7.getMessage()));
            } else {
               WsFetchXml[] var8 = var6.getQueries();
               ImmutableList var9 = FluentIterable.from(Arrays.asList(var8))
                  .transform(new MSCrmDesignInfoProviderWsImpl.FetchXmlDeserializer())
                  .toImmutableList();
               var3.gotQueries(var9);
            }
         } catch (RemoteException var10) {
            var3.caught(this.convert(var10));
         } catch (ServiceException var11) {
            var3.caught(new IntegrationServerException("Failed to call webservice: " + var11.getMessage(), var11));
         }
      }

      public void getQueryResponseSchema(MSCrmEndpoint var1, String var2, GetResponseSchemaCallback var3) {
         try {
            MSCrmDesignEngine var4 = this.getServerProxy(var3);
            if (var4 == null) {
               return;
            }

            WsEndpointParameters var5 = this.createWsEndpointParams(var1);
            WsQueryResponseSchema var6 = var4.getQueryResponseSchema(this.config.getUserName(), this.config.getPasswordHash(), var5, var2);
            WsStatus var7 = var6.getStatus();
            if (var7.getStatusCode() < 0) {
               var3.caught(new Exception(var7.getMessage()));
            } else {
               WsSchema var8 = var6.getSchema();
               SchemaData var9 = SchemaData.fromCompressedContents(var8.getRootName(), var8.getCompressedContents());
               var3.gotResponseSchema(var9);
            }
         } catch (RemoteException var10) {
            var3.caught(this.convert(var10));
         } catch (ServiceException var11) {
            var3.caught(new IntegrationServerException("Failed to call webservice: " + var11.getMessage(), var11));
         }
      }

      private MSCrmDesignEngine getServerProxy(ServerCallback var1) throws ServiceException {
         MSCrmDesignEngineServiceLocator var2 = new MSCrmDesignEngineServiceLocator();
         StringBuilder var3 = new StringBuilder();
         if (this.config.configureWebservice(var2, "konga_mscrmdesign_engine", var3, var1)) {
            var2.setkonga_mscrmdesign_engineEndpointAddress(var3.toString());
            return var2.getkonga_mscrmdesign_engine();
         } else {
            return null;
         }
      }

      private WsEndpointParameters createWsEndpointParams(ConnectorEndpoint var1) {
         WsEndpointParameters var2 = new WsEndpointParameters();
         String var3 = var1.getConnectorTypeId();
         var2.setEndpointTypeName(var3);
         var2.setEndpointGUID(var1.getID().toString());
         EndpointParameters var4 = var1.getConnectionParameters();
         var2.setHost(var4.getServer().getAddress());
         UserCredentials var5 = var4.getUserCredentials();
         var2.setUser(var5.getUsername());
         Optional var6 = var5.getPassword();
         if (var6.isPresent()) {
            var2.setPassword(((Password)var6.get()).clearText());
         }

         var2.setIsPasswordEncrypted(false);
         ArrayList var7 = Lists.newArrayList();
         Properties var8 = var4.getAdditionalParams();

         for (String var10 : var8.stringPropertyNames()) {
            String var11 = var8.getProperty(var10);
            var7.add(new WsNameValuePair(var10, var11));
         }

         var2.setAdditionalParameters(Iterables.toArray(var7, WsNameValuePair.class));
         return var2;
      }

      private IntegrationServerException convert(RemoteException var1) {
         return this.config.convertToIntegrationServerException(var1);
      }
   }
}
