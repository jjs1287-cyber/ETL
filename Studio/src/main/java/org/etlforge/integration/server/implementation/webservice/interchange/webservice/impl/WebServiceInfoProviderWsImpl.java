package org.etlforge.integration.server.implementation.webservice.interchange.webservice.impl;

import com.google.common.collect.Lists;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.xml.rpc.ServiceException;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.client.mapping.serverapi.webservice.GenerateSoapSchemasCallback;
import org.etlforge.integration.client.mapping.serverapi.webservice.GetWebServiceJtrCallback;
import org.etlforge.integration.client.mapping.serverapi.webservice.SoapSchemaProvider;
import org.etlforge.integration.client.mapping.serverapi.webservice.WebServiceInfoProvider;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.client.wsdl.serverapi.DeleteWsdlCallback;
import org.etlforge.integration.client.wsdl.serverapi.GetWsdlFilesCallback;
import org.etlforge.integration.client.wsdl.serverapi.WsdlProvider;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.client.ServerFileUploadResult;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;
import org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.CWsdlFile;
import org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.CWsdlFileUploadResult;
import org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.CWsdlFiles;
import org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.SoapSchemaGenerationResult;
import org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.WebServiceInfoProviderServiceLocator;
import org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.WebServiceJtr;
import org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.WebServiceOperationData;
import org.etlforge.util.crypto.DigestUtils;
import org.etlforge.util.zip.ZipUtils;

public final class WebServiceInfoProviderWsImpl implements WebServiceInfoProvider, SoapSchemaProvider, WsdlProvider {
   private final CppWebServiceConfiguration config;

   public WebServiceInfoProviderWsImpl(CppWebServiceConfiguration var1) {
      this.config = var1;
   }

   @Override
   public void getWsdlFiles(GetWsdlFilesCallback var1) {
      WebServiceInfoProviderWsImpl.Impl var2 = new WebServiceInfoProviderWsImpl.Impl(this.config);
      var2.getWsdlFiles(var1);
   }

   @Override
   public void uploadFile(File var1, ServerFileFolder var2, String var3, UploadServerFileCallback var4) {
      WebServiceInfoProviderWsImpl.Impl var5 = new WebServiceInfoProviderWsImpl.Impl(this.config);
      var5.uploadLocalFile(var1.getAbsolutePath(), var3, var4);
   }

   @Override
   public void uploadRemoteWsdl(String var1, UploadServerFileCallback var2) {
      WebServiceInfoProviderWsImpl.Impl var3 = new WebServiceInfoProviderWsImpl.Impl(this.config);
      var3.uploadRemoteWsdl(var1, var2);
   }

   @Override
   public void deleteWsdl(String var1, boolean var2, DeleteWsdlCallback var3) {
      WebServiceInfoProviderWsImpl.Impl var4 = new WebServiceInfoProviderWsImpl.Impl(this.config);
      var4.deleteWsdl(var1, var2, var3);
   }

   @Override
   public void generateXsds(WebServiceOperationInfo var1, String var2, String var3, boolean var4, GenerateSoapSchemasCallback var5) {
      WebServiceInfoProviderWsImpl.Impl var6 = new WebServiceInfoProviderWsImpl.Impl(this.config);
      var6.generateXsds(var1, var2, var3, var4, var5);
   }

   @Override
   public void getInputAndOutputJTR(WebServiceOperationInfo var1, GetWebServiceJtrCallback var2) {
      WebServiceInfoProviderWsImpl.Impl var3 = new WebServiceInfoProviderWsImpl.Impl(this.config);
      var3.getInputAndOutputJTR(var1, var2);
   }

   private static class Impl extends CppWebServiceServerCall {
      private static final String SERVICE_NAME = "konga_webservice_infoprovider";
      private String user;
      private String password;
      private String serverName;
      private ServerGuid serverId;

      public Impl(CppWebServiceConfiguration var1) {
         super(var1);
      }

      private org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.WebServiceInfoProvider getProvider(ServerCallback var1) throws IntegrationServerException {
         WebServiceInfoProviderServiceLocator var2 = new WebServiceInfoProviderServiceLocator();

         try {
            StringBuilder var3 = new StringBuilder();
            if (!this.getConfiguration().configureWebservice(var2, "konga_webservice_infoprovider", var3, var1)) {
               return null;
            }

            this.user = this.getConfiguration().getUserName();
            this.password = this.getConfiguration().getPasswordHash();
            this.serverName = this.getConfiguration().getServerName();
            this.serverId = this.getConfiguration().getServerId();
            var2.setkonga_webservice_infoproviderEndpointAddress(var3.toString());
            return var2.getkonga_webservice_infoprovider();
         } catch (ServiceException var4) {
            throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
         }
      }

      public void uploadLocalFile(String var1, String var2, UploadServerFileCallback var3) {
         File var4 = new File(var1);
         if (!var4.canRead()) {
            var3.caught(new WsdlInfoProviderImplException("The local file '" + var4.getAbsolutePath() + "' can not be read."));
         }

         try {
            String var5 = ZipUtils.deflateAndBase64EncodeFileContents(var4);
            String var6 = DigestUtils.getMd5FileDigestString(var4);
            this.callUploadWs(var4.getName(), var5, var6, var2, var3);
         } catch (Exception var7) {
            var3.caught(var7);
         }
      }

      public void uploadRemoteWsdl(String var1, UploadServerFileCallback var2) {
         if (var1.indexOf("://") == -1) {
            var2.caught(new WsdlInfoProviderImplException("'" + var1 + "' is not a valid URL."));
         }

         this.callUploadWs(var1, null, null, null, var2);
      }

      private CWsdlFile createCWsdlFile(String var1, String var2, String var3) {
         CWsdlFile var4 = new CWsdlFile();
         var4.setMD5(var2);
         var4.setWsdlLocator(var1);
         var4.setZippedBase64Contents(var3);
         return var4;
      }

      private void callUploadWs(String var1, String var2, String var3, String var4, UploadServerFileCallback var5) {
         try {
            org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.WebServiceInfoProvider var6 = this.getProvider(var5);
            if (var6 == null) {
               return;
            }

            CWsdlFile var7 = this.createCWsdlFile(var1, var3, var2);
            CWsdlFile var8 = null;
            if (var4 != null && var4.length() > 0) {
               var8 = this.createCWsdlFile(var4, null, null);
            }

            CWsdlFileUploadResult var9 = var6.uploadWsdl(this.user, this.password, var7, var8);
            CWsdlFile[] var10 = var9.getExistingDependencies();
            String[] var11 = var9.getMissingWsdlFiles();
            ArrayList var12 = Lists.newArrayList();
            if (var10 != null && var10.length > 0) {
               for (CWsdlFile var16 : var10) {
                  ServerFile var17 = ServerFile.builder(var16.getWsdlLocator()).withHash(var16.getMD5()).build();
                  var12.add(var17);
               }
            }

            ServerInfo var20 = new ServerInfo(this.serverId, this.serverName);
            ServerFile var21 = ServerFile.builder(var1).withCompressedContents(var2).withHash(var3).build();
            ServerFileUploadResult var22 = new ServerFileUploadResult(var20, var21, var11, var12);
            var5.fileWasUploaded(var22);
         } catch (RemoteException var18) {
            var5.caught(this.convert(var18));
         } catch (IntegrationServerException var19) {
            var5.caught(var19);
         }
      }

      public void deleteWsdl(String var1, boolean var2, DeleteWsdlCallback var3) {
         try {
            org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.WebServiceInfoProvider var4 = this.getProvider(var3);
            if (var4 == null) {
               return;
            }

            CWsdlFile var5 = new CWsdlFile();
            var5.setWsdlLocator(var1);
            String[] var6 = var4.deleteWsdl(this.user, this.password, var5, var2);
            if (Thread.interrupted()) {
               var3.cancelled();
               return;
            }

            ArrayList var7 = Lists.newArrayList();
            if (var6 != null && var6.length > 0) {
               for (String var11 : var6) {
                  var7.add(var11);
               }
            }

            var3.deleteComplete(var7);
         } catch (RemoteException var12) {
            var3.caught(this.convert(var12));
         } catch (IntegrationServerException var13) {
            var3.caught(var13);
         }
      }

      public void getWsdlFiles(GetWsdlFilesCallback var1) {
         try {
            org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.WebServiceInfoProvider var2 = this.getProvider(var1);
            if (var2 == null) {
               return;
            }

            ServerInfo var3 = this.getConfiguration().getServerInfo();
            CWsdlFiles var4 = var2.getWsdlFiles(this.user, this.password);
            if (Thread.interrupted()) {
               var1.cancelled();
               return;
            }

            CWsdlFile[] var5 = var4.getWsdlFiles();
            ArrayList var6 = Lists.newArrayList();

            for (CWsdlFile var10 : var5) {
               StringKeyValuePairs var11 = new StringKeyValuePairs();
               var11.putString("wsdl_id", var10.getWsdlLocator());
               var11.putLong("status", var10.getStatus());
               var11.putString("applicationmessage", var10.getApplicationMessage());
               var11.putString("usercomment", var10.getUserComment());
               var6.add(new WsdlFile(var11));
            }

            var1.gotWsdlFiles(var3, var6);
         } catch (RemoteException var12) {
            var1.caught(this.convert(var12));
         } catch (IntegrationServerException var13) {
            var1.caught(var13);
         }
      }

      public void getInputAndOutputJTR(WebServiceOperationInfo var1, GetWebServiceJtrCallback var2) {
         try {
            org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.WebServiceInfoProvider var3 = this.getProvider(var2);
            if (var3 == null) {
               return;
            }

            WebServiceOperationData var4 = this.createOperationData(var1);
            WebServiceJtr var5 = var3.getInputAndOutputJTR(this.user, this.password, var4);
            if (Thread.interrupted()) {
               var2.cancelled();
               return;
            }

            var2.jtrGenerated(new WebServiceCallStructuresImpl(var5));
         } catch (RemoteException var6) {
            var2.caught(this.convert(var6));
         } catch (IntegrationServerException var7) {
            var2.caught(new IntegrationServerException("Failed to initialize webservice: " + var7.getMessage(), var7));
         }
      }

      private WebServiceOperationData createOperationData(WebServiceOperationInfo var1) {
         WebServiceOperationData var2 = new WebServiceOperationData();
         var2.setBindingName(var1.getProperty("Binding"));
         String var3 = var1.getProperty("SoapBodyUse");
         var2.setBodyUse(var3 != null ? var3 : "");
         var2.setWsdlLocator(var1.getProperty("Identifier"));
         var2.setOperationName(var1.getProperty("Operation"));
         var3 = var1.getProperty("SoapStyle");
         var2.setSoapStyle(var3 != null ? var3 : "");
         var3 = var1.getProperty("InputName");
         var2.setOperationInputName(var3 != null ? var3 : "");
         var3 = var1.getProperty("OutputName");
         var2.setOperationOutputName(var3 != null ? var3 : "");
         return var2;
      }

      public void generateXsds(WebServiceOperationInfo var1, String var2, String var3, boolean var4, GenerateSoapSchemasCallback var5) {
         try {
            org.etlforge.integration.server.implementation.webservice.interchange.webservice.client.WebServiceInfoProvider var6 = this.getProvider(var5);
            if (var6 == null) {
               return;
            }

            WebServiceOperationData var7 = this.createOperationData(var1);
            SoapSchemaGenerationResult var8 = var6.generateSoapSchema(this.user, this.password, var7, var2, var3, var4, true);
            if (var8.getResult() == 0) {
               var5.filesWereGenerated();
            } else {
               var5.fileConflictsDetected(var8.getFileNames());
            }
         } catch (RemoteException var9) {
            var5.caught(this.convert(var9));
         } catch (IntegrationServerException var10) {
            var5.caught(new IntegrationServerException("Failed to initialize webservice: " + var10.getMessage(), var10));
         }
      }
   }
}
