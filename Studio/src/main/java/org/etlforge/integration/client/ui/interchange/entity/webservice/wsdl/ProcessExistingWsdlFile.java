package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import org.etlforge.application.Application;
import org.etlforge.application.DebugMode;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.wsdl.WsdlParser;
import org.etlforge.integration.client.wsdl.WsdlParserException;
import org.etlforge.integration.client.wsdl.WsdlParserFactory;
import org.etlforge.integration.client.wsdl.cache.WsdlFileCache;
import org.etlforge.integration.client.wsdl.cache.WsdlFileServerCache;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.download.DownloadedFiles;
import org.etlforge.integration.filetransfer.client.download.FileDownloadClient;
import org.etlforge.integration.filetransfer.client.download.FileDownloadManager;
import org.etlforge.lang.exception.ExceptionUtils;
import org.etlforge.system.SystemUtils;

final class ProcessExistingWsdlFile extends AbstractWsdlProcessingJob {
   private final FileStore fileStore;
   private final ServerInfo serverInfo;

   public ProcessExistingWsdlFile(FileStore var1, ServerInfo var2, WsdlFile var3) {
      this.fileStore = var1;
      this.serverInfo = var2;
      this.setWsdlFile(var3);
   }

   @Override
   public void run() {
      byte[] var1 = this.getCachedFileContents();
      if (var1 == null) {
         this.download();
      } else {
         this.parse(var1);
      }
   }

   private byte[] getCachedFileContents() {
      WsdlFileCache var1 = this.getFileCache();
      if (var1 == null) {
         return null;
      }

      try {
         return var1.retrieve(this.getWsdlFile().getLocator());
      } catch (DataCacheException var4) {
         String var3 = "Failed to retrieved a cached WSDL file (\"" + this.getWsdlFile().getLocator() + "\"). Reason: ";
         ErrorLog.log(ProcessExistingWsdlFile.class, var3, var4);
         var3 = var3 + SystemUtils.LINE_SEPARATOR;
         var3 = var3 + ExceptionUtils.getStackTrace(var4);
         ApplicationLog.getMainLogger().warning(var3);
         return null;
      }
   }

   private WsdlFileCache getFileCache() {
      DataCacheService var1 = Application.getCacheService();
      WsdlFileServerCache var2 = var1.getCache(WsdlFileServerCache.class);
      return var2 != null ? var2.getCache(this.serverInfo.getGuid()) : null;
   }

   private void download() {
      ProcessExistingWsdlFile.WsdlDownloader var1 = new ProcessExistingWsdlFile.WsdlDownloader();
      String var2 = this.getWsdlFile().getLocator();
      var1.download(var2);
   }

   private void parse(byte[] var1) {
      WsdlParser var2 = WsdlParserFactory.newParser(this.getWsdlFile(), this.fileStore);

      try {
         this.setNewMessage(PackageResources.ProcessExistingFileJob.STATUS_PARSING);
         WebServiceInfo var3 = var2.parse(var1);
         this.setParsedInfo(var3);
         this.signalJobSucceeded();
         var2.dispose();
      } catch (WsdlParserException var7) {
         this.signalJobFailed(var7);
      } finally {
         if (!DebugMode.IS_ENABLED) {
            var2.dispose();
         }
      }
   }

   private final class WsdlDownloader implements FileDownloadClient {
      private WsdlDownloader() {
      }

      public void download(String var1) {
         ServerFile var2 = ServerFile.fromPath(ProcessExistingWsdlFile.this.getWsdlFile().getLocator());
         FileDownloadManager var3 = new FileDownloadManager(this);
         var3.download(var2);
      }

      @Override
      public void downloadCancelled() {
         ProcessExistingWsdlFile.this.signalJobCancelled();
      }

      @Override
      public void downloadComplete(DownloadedFiles var1) {
         ServerFile[] var2 = var1.getFiles();
         if (var2.length == 0) {
            ProcessExistingWsdlFile.this.signalJobFailed(new Exception("No files were downloaded from the server"));
         } else {
            this.cacheDownloadedWsdlFiles(var1);
            this.parseWsdlFile(var2[0]);
         }
      }

      @Override
      public void downloadStarted() {
      }

      @Override
      public FileStore getFileStore() {
         return ProcessExistingWsdlFile.this.fileStore;
      }

      @Override
      public void handleError(String var1, Throwable var2) {
         ProcessExistingWsdlFile.this.signalJobFailed(var2);
      }

      private void cacheDownloadedWsdlFiles(DownloadedFiles var1) {
         DataCacheService var2 = Application.getCacheService();
         WsdlFileServerCache var3 = var2.getCache(WsdlFileServerCache.class);

         for (ServerFile var7 : var1.getFiles()) {
            try {
               this.cacheWsdlFile(var1.getServer(), var7, var3);
            } catch (DataCacheException var10) {
               String var9 = "Failed to cache a downloaded WSDL file (\"" + var7.getFilePath() + "\"). Reason: ";
               ErrorLog.log(ProcessExistingWsdlFile.class, var9, var10);
               var9 = var9 + ExceptionUtils.getStackTrace(var10);
               var9 = var9 + SystemUtils.LINE_SEPARATOR;
               ApplicationLog.getMainLogger().warning(var9);
            }
         }
      }

      private void cacheWsdlFile(ServerInfo var1, ServerFile var2, WsdlFileServerCache var3) throws DataCacheException {
         String var4 = var2.getFilePath();
         byte[] var5 = var2.getContentsAsBytes();
         var3.cacheWsdlFile(var1, var4, var5);
      }

      private void parseWsdlFile(ServerFile var1) {
         byte[] var2 = var1.getContentsAsBytes();
         ProcessExistingWsdlFile.this.parse(var2);
      }
   }
}
