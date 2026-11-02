package com.etlforge.integration.server.implementation.webservice.admin.server.impl;

import com.etlforge.integration.client.admin.server.DownloadServerFileCallback;
import com.etlforge.integration.client.admin.server.GetServerFileListCallback;
import com.etlforge.integration.client.admin.server.ServerFileLocator;
import com.etlforge.integration.client.admin.server.UploadServerConfigurationFileCallback;
import com.etlforge.integration.client.admin.server.ViewServerConfigurationFileCallback;
import com.etlforge.integration.server.implementation.webservice.serveradmin.client.ServerAdministration;
import com.etlforge.integration.server.implementation.webservice.serveradmin.client.ServerAdministrationServiceLocator;
import com.etlforge.integration.server.implementation.webservice.serveradmin.client.WsServerFileLocators;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.UUID;
import java.util.zip.DataFormatException;
import javax.xml.rpc.ServiceException;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.webservice.WebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.WebServiceServerCall;
import org.etlforge.integration.common.server.ServerConversionUtils;
import org.etlforge.lang.Arrays2;
import org.etlforge.util.zip.ZipUtils;

final class ServerFilesWsImpl extends WebServiceServerCall {
   private static final String SERVICE_NAME = "konga_serveradmin";
   private String user;
   private String password;

   public ServerFilesWsImpl(WebServiceConfiguration var1) {
      super(var1);
   }

   public void viewServerConfigurationFile(ViewServerConfigurationFileCallback var1) {
      try {
         ServerAdministration var2 = this.getProvider(var1);
         if (var2 == null) {
            return;
         }

         ServerInfo var3 = this.getConfiguration().getServerInfo();
         String var4 = var2.downloadServerConfigurationFile(this.user, this.password);
         var1.configurationFileReceived(new ServerConfigurationFileImpl(var3, var4));
      } catch (RemoteException var5) {
         var1.caught(this.convert(var5));
      } catch (DataFormatException var6) {
         var1.caught(var6);
      } catch (IntegrationServerException var7) {
         var1.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
      }
   }

   public void uploadServerConfigurationFile(File var1, UploadServerConfigurationFileCallback var2) {
      try {
         ServerAdministration var3 = this.getProvider(var2);
         if (var3 == null) {
            return;
         }

         String var4 = ZipUtils.deflateAndBase64EncodeFileContents(var1);
         var3.uploadServerConfigurationFile(this.user, this.password, var4);
         var2.fileWasUploaded();
      } catch (RemoteException var5) {
         var2.caught(this.convert(var5));
      } catch (IOException var6) {
         var2.caught(var6);
      } catch (IntegrationServerException var7) {
         var2.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
      }
   }

   public void listPluginLogFiles(GetServerFileListCallback var1) {
      try {
         ServerAdministration var2 = this.getProvider(var1);
         if (var2 == null) {
            return;
         }

         WsServerFileLocators var3 = var2.listPluginLogFiles(this.user, this.password);
         var1.fileListRetrieved(Arrays2.transform(var3.getLocators(), ServerFileLocator.class, ServerFileLocatorImpl.FROM_SERVER));
      } catch (RemoteException var4) {
         var1.caught(this.convert(var4));
      } catch (IntegrationServerException var5) {
         var1.caught(new IntegrationServerException("Server exception: " + var5.getMessage(), var5));
      }
   }

   public void listServerLogFiles(GetServerFileListCallback var1) {
      try {
         ServerAdministration var2 = this.getProvider(var1);
         if (var2 == null) {
            return;
         }

         WsServerFileLocators var3 = var2.listServerLogFiles(this.user, this.password);
         var1.fileListRetrieved(Arrays2.transform(var3.getLocators(), ServerFileLocator.class, ServerFileLocatorImpl.FROM_SERVER));
      } catch (RemoteException var4) {
         var1.caught(this.convert(var4));
      } catch (IntegrationServerException var5) {
         var1.caught(new IntegrationServerException("Server exception: " + var5.getMessage(), var5));
      }
   }

   public void listOperationLogFiles(Date var1, Date var2, GetServerFileListCallback var3) {
      try {
         ServerAdministration var4 = this.getProvider(var3);
         if (var4 == null) {
            return;
         }

         WsServerFileLocators var5 = var4.listOperationLogFiles(
            this.user, this.password, ServerConversionUtils.toServerDateFormat(var1), ServerConversionUtils.toServerDateFormat(var2)
         );
         var3.fileListRetrieved(Arrays2.transform(var5.getLocators(), ServerFileLocator.class, ServerFileLocatorImpl.FROM_SERVER));
      } catch (RemoteException var6) {
         var3.caught(this.convert(var6));
      } catch (IntegrationServerException var7) {
         var3.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
      }
   }

   public void listDebugLogFiles(Date var1, Date var2, GetServerFileListCallback var3) {
      try {
         ServerAdministration var4 = this.getProvider(var3);
         if (var4 == null) {
            return;
         }

         WsServerFileLocators var5 = var4.listDebugLogFiles(
            this.user, this.password, ServerConversionUtils.toServerDateFormat(var1), ServerConversionUtils.toServerDateFormat(var2)
         );
         var3.fileListRetrieved(Arrays2.transform(var5.getLocators(), ServerFileLocator.class, ServerFileLocatorImpl.FROM_SERVER));
      } catch (RemoteException var6) {
         var3.caught(this.convert(var6));
      } catch (IntegrationServerException var7) {
         var3.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
      }
   }

   public void downloadServerFile(String var1, DownloadServerFileCallback var2) {
      try {
         ServerAdministration var3 = this.getProvider(var2);
         if (var3 == null) {
            return;
         }

         String var4 = var3.downloadFile(this.user, this.password, var1);
         File var5 = writeDownloadedDataToTempFile(var4);
         var2.fileWasDownloaded(var5);
      } catch (RemoteException var6) {
         var2.caught(this.convert(var6));
      } catch (IOException var7) {
         var2.caught(var7);
      } catch (IntegrationServerException var8) {
         var2.caught(new IntegrationServerException("Server exception: " + var8.getMessage(), var8));
      }
   }

   private ServerAdministration getProvider(ServerCallback var1) throws IntegrationServerException {
      if (Thread.currentThread().isInterrupted()) {
         var1.cancelled();
         return null;
      }

      try {
         ServerAdministrationServiceLocator var2 = new ServerAdministrationServiceLocator();
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(var2, "konga_serveradmin", var3, var1)) {
            return null;
         }

         this.user = this.getConfiguration().getUserName();
         this.password = this.getConfiguration().getPasswordHash();
         var2.setkonga_serveradminEndpointAddress(var3.toString());
         return var2.getkonga_serveradmin();
      } catch (ServiceException var4) {
         throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
      }
   }

   private static File writeDownloadedDataToTempFile(String var0) throws IOException {
      File var1 = ApplicationTempFolder.newFile(UUID.randomUUID().toString());
      var1.deleteOnExit();
      ZipUtils.base64DecodeAndInflateToFile(var0, var1);
      return var1;
   }
}
