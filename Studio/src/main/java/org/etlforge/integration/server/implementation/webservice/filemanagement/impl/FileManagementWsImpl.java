package org.etlforge.integration.server.implementation.webservice.filemanagement.impl;

import com.google.common.collect.Lists;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.filetransfer.serverapi.FileDownloadCallback;
import org.etlforge.integration.filetransfer.serverapi.FileDownloader;
import org.etlforge.integration.server.implementation.webservice.filemanagement.client.CDownloadedFile;
import org.etlforge.integration.server.implementation.webservice.filemanagement.client.CFileDownloadResult;
import org.etlforge.integration.server.implementation.webservice.filemanagement.client.FileManager;
import org.etlforge.integration.server.implementation.webservice.filemanagement.client.FileManagerServiceLocator;

public final class FileManagementWsImpl extends CppWebServiceServerCall implements FileDownloader {
   private static final String SERVICE_NAME = "konga_file_management";

   public FileManagementWsImpl(CppWebServiceConfiguration var1) {
      super(var1);
   }

   private FileManager getFileManager(ServerCallback var1) throws IntegrationServerException {
      FileManagerServiceLocator var2 = new FileManagerServiceLocator();

      try {
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(var2, "konga_file_management", var3, var1)) {
            return null;
         }

         var2.setkonga_file_managementEndpointAddress(var3.toString());
         return var2.getkonga_file_management();
      } catch (ServiceException var4) {
         throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
      }
   }

   @Override
   public void downloadFile(String var1, FileDownloadCallback var2) {
      try {
         FileManager var3 = this.getFileManager(var2);
         if (var3 == null) {
            return;
         }

         ServerInfo var4 = this.getConfiguration().getServerInfo();
         CDownloadedFile var5 = new CDownloadedFile();
         var5.setPath(var1);
         CFileDownloadResult var6 = var3.downloadFile(this.getConfiguration().getUserName(), this.getConfiguration().getPasswordHash(), var5);
         CDownloadedFile var7 = var6.getDownloadedFile();
         CDownloadedFile[] var8 = var6.getDependencies();
         ArrayList var9 = Lists.newArrayList();
         if (var8 != null) {
            for (CDownloadedFile var13 : var8) {
               var9.add(new FileDownloadDependencyImpl(var13.getPath(), var13.getMD5()));
            }
         }

         var2.fileWasDownloaded(var4, var7.getPath(), var7.getZippedBase64EncodedContents(), var9);
      } catch (RemoteException var14) {
         var2.caught(this.convert(var14));
      } catch (IntegrationServerException var15) {
         var2.caught(new IntegrationServerException("Server exception: " + var15.getMessage(), var15));
      }
   }
}
