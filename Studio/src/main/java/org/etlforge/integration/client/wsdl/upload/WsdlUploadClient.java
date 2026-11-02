package org.etlforge.integration.client.wsdl.upload;

import com.google.common.base.Function;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.wsdl.cache.WsdlFileServerCache;
import org.etlforge.integration.client.wsdl.cache.WsdlListCache;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileUploadResult;
import org.etlforge.integration.filetransfer.client.upload.FileUploadClient;
import org.etlforge.lang.Arrays2;
import org.etlforge.lang.exception.ExceptionUtils;
import org.etlforge.system.callback.CallbackResult;

public final class WsdlUploadClient implements FileUploadClient {
   private final FileStore fileStore;
   private final CallbackResult callback;

   public WsdlUploadClient(FileStore var1, CallbackResult var2) {
      this.fileStore = var1;
      this.callback = var2;
   }

   @Override
   public void fileWasUploaded(String var1, ServerFile var2, ServerFileUploadResult var3) {
      this.storeFileInFileStore(var1, var2);
      this.registerExistingDependencies(var3);
      this.storeFileInCache(var1, var3);
   }

   private void storeFileInFileStore(String var1, ServerFile var2) {
      try {
         File var3 = new File(var1);
         this.getFileStore().addFile(var3, var3.getName(), FileCollisionResolver.REPLACE_EXISTING);
         if (var2 != null) {
            this.getFileStore().addDependency(var2.getFilePath(), var3.getName());
         }
      } catch (FileStoreException var4) {
         var4.printStackTrace();
      }
   }

   private void registerExistingDependencies(ServerFileUploadResult var1) {
      String var2 = var1.getServerFile().getName();

      for (ServerFile var4 : var1.getExistingDependencies()) {
         String var5 = var4.getName();
         this.getFileStore().addDependency(var2, var5);
      }
   }

   private void storeFileInCache(String var1, ServerFileUploadResult var2) {
      try {
         DataCacheService var3 = Application.getCacheService();
         WsdlFileServerCache var4 = var3.getCache(WsdlFileServerCache.class);
         File var5 = new File(var1);
         byte[] var6 = Files.toByteArray(var5);
         var4.cacheWsdlFile(var2.getServer(), var5.getName(), var6);
      } catch (IOException var7) {
         this.handleNonFatalError(var7);
      } catch (DataCacheException var8) {
         this.handleNonFatalError(var8);
      }
   }

   private void handleNonFatalError(Throwable var1) {
      String var2 = "Failed to cache uploaded WSDL file: " + ExceptionUtils.getStackTrace(var1);
      ErrorLog.log(this.getClass(), var2, var1);
   }

   @Override
   public FileStore getFileStore() {
      return this.fileStore;
   }

   @Override
   public void handleError(Throwable var1) {
      this.callback.failed(var1);
   }

   @Override
   public boolean isAborted() {
      return false;
   }

   @Override
   public boolean isFileClearedForUpload(File var1) {
      return true;
   }

   @Override
   public void uploadCancelled() {
      this.callback.cancelled(null);
   }

   @Override
   public void uploadComplete(ServerInfo var1, ServerFile var2, ServerFile[] var3) {
      WsdlFile[] var4 = Arrays2.transform(var3, WsdlFile.class, new Function<ServerFile, WsdlFile>() {
         public WsdlFile apply(ServerFile var1) {
            return new WsdlFile(var1.getFilePath());
         }
      });
      WsdlListCache.update(var1.getGuid(), var4);
      this.callback.succeeded(var2);
   }
}
