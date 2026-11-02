package org.etlforge.integration.server.implementation.webservice.activity.impl;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.activity.serverapi.files.DownloadFileCallback;
import org.etlforge.integration.activity.serverapi.files.DownloadFileRequest;
import org.etlforge.integration.activity.serverapi.files.ListAvailableFilesCallback;
import org.etlforge.integration.activity.serverapi.files.OperationFileProvider;
import org.etlforge.integration.activity.serverapi.files.OperationFileRequest;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.download.Credentials;
import org.etlforge.integration.client.server.download.Request;
import org.etlforge.integration.client.server.download.ServerFileDownloadException;
import org.etlforge.integration.client.server.download.ServerFileDownloader;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.integration.server.implementation.webservice.activity.client.ActivityInfoProvider;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationFileStatus;
import org.etlforge.integration.server.implementation.webservice.activity.client.WsOperationInstanceFilter;

public final class OperationFileProviderWsImpl implements OperationFileProvider {
   private final CppWebServiceConfiguration config;

   public OperationFileProviderWsImpl(CppWebServiceConfiguration var1) {
      this.config = var1;
   }

   @Override
   public void listAvailableFiles(OperationFileRequest var1, ListAvailableFilesCallback var2) {
      OperationFileProviderWsImpl.Worker var3 = new OperationFileProviderWsImpl.Worker(this.config);
      var3.listAvailableFiles(var1, var2);
   }

   @Override
   public void downloadFile(DownloadFileRequest var1, DownloadFileCallback var2) {
      OperationFileProviderWsImpl.Worker var3 = new OperationFileProviderWsImpl.Worker(this.config);
      var3.downloadFile(var1, var2);
   }

   private static class Worker extends AbstractActivityInfoProviderWsImpl {
      public Worker(CppWebServiceConfiguration var1) {
         super(var1);
      }

      public void listAvailableFiles(OperationFileRequest var1, ListAvailableFilesCallback var2) {
         try {
            ActivityInfoProvider var3 = this.getServerProxy(var2);
            if (var3 == null) {
               return;
            }

            ServerInfo var4 = this.getConfiguration().getServerInfo();
            WsOperationInstanceFilter var5 = toWsFilter(var1);
            if (Thread.currentThread().isInterrupted()) {
               var2.cancelled();
               return;
            }

            WsOperationFileStatus[] var6 = var3.getOperationFiles(this.userName, this.password, var5);
            if (Thread.currentThread().isInterrupted()) {
               var2.cancelled();
               return;
            }

            List var7 = this.createClientSideResult(var6);
            var2.gotFileList(var4, var7);
         } catch (RemoteException var8) {
            var2.caught(this.convert(var8));
         }
      }

      protected List<OperationFile> createClientSideResult(WsOperationFileStatus[] var1) {
         return var1 != null && var1.length != 0 ? KongaListUtils.transform(var1, new Functions.FromWsOperationFile()) : Collections.emptyList();
      }

      private static WsOperationInstanceFilter toWsFilter(OperationFileRequest var0) {
         WsOperationInstanceFilter var1 = new WsOperationInstanceFilter();
         if (var0.getMaxEntriesToGet() > 0) {
            var1.setMaxRecords(var0.getMaxEntriesToGet());
         }

         var1.setGuids(new String[]{getIdString(var0.getSubjectId())});
         long[] var2 = var0.getOpInstanceIds();
         if (var2.length == 0) {
            var1.setOperationInstances(new long[]{-1L});
         } else {
            var1.setOperationInstances(var2);
         }

         return var1;
      }

      public void downloadFile(DownloadFileRequest var1, DownloadFileCallback var2) {
         try {
            ActivityInfoProvider var3 = this.getServerProxy(var2);
            if (var3 == null) {
               return;
            }

            ServerFileDownloader var4 = this.createDownloader();
            Request var5 = new Request(
               "resultfile",
               var1.serverFile.getGuid().toString(),
               new Credentials(this.getConfiguration().getUserName(), this.getConfiguration().getPassword()),
               var1.serverFile.getEncoding()
            );
            var4.downloadToLocalFile(var5, var1.localFile);
            var2.gotFile(var1.localFile);
         } catch (ServerFileDownloadException var6) {
            var2.caught(var6);
         } catch (Exception var7) {
            var2.caught(var7);
         }
      }

      private ServerFileDownloader createDownloader() {
         String var1 = this.getConfiguration().getServerName();
         return new ServerFileDownloader(var1).withHttps(this.getConfiguration().isUsingHttps());
      }
   }
}
