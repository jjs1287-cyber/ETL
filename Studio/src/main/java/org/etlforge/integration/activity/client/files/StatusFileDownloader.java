package org.etlforge.integration.activity.client.files;

import com.google.common.base.Optional;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import org.etlforge.integration.activity.serverapi.files.DownloadFileCallback;
import org.etlforge.integration.activity.serverapi.files.DownloadFileRequest;
import org.etlforge.integration.common.server.file.OperationFile;

public final class StatusFileDownloader implements Callable<File> {
   private final OperationFile opFile;
   private final RemoteFileSupport remoteFileSupport;
   private volatile Optional<RemoteFileCache> cache = Optional.absent();

   public StatusFileDownloader(OperationFile var1, RemoteFileSupport var2) {
      this.opFile = var1;
      this.remoteFileSupport = var2;
   }

   public void setCache(RemoteFileCache var1) {
      this.cache = Optional.of(var1);
   }

   public File call() throws Exception {
      Callable var1 = new Callable<File>() {
         public File call() throws Exception {
            File var1 = StatusFileDownloader.this.remoteFileSupport.getDestination(StatusFileDownloader.this.opFile);
            return var1 == null ? null : StatusFileDownloader.this.downloadFile(var1);
         }
      };
      return this.cache.isPresent() ? this.cache.get().wrapSupplier(this.opFile, var1) : (File)var1.call();
   }

   private File downloadFile(File var1) throws Exception {
      DownloadFileRequest var2 = new DownloadFileRequest(this.opFile, var1);
      StatusFileDownloader.FileReceiver var3 = new StatusFileDownloader.FileReceiver();
      this.remoteFileSupport.downloadRemoteFile(var2, var3);

      try {
         return var3.waitForResult();
      } catch (InterruptedException var5) {
         Thread.currentThread().interrupt();
         return null;
      }
   }

   private class FileReceiver implements DownloadFileCallback {
      private final CountDownLatch latch = new CountDownLatch(1);
      private File localFile;
      private Exception error;

      private FileReceiver() {
      }

      public File waitForResult() throws InterruptedException, Exception {
         this.latch.await();
         if (this.error != null) {
            throw this.error;
         } else {
            return this.localFile;
         }
      }

      @Override
      public void gotFile(File var1) {
         this.localFile = var1;
         this.latch.countDown();
      }

      @Override
      public void caught(Throwable var1) {
         this.error = var1 instanceof Exception ? (Exception)var1 : new Exception(var1);
         this.latch.countDown();
      }

      @Override
      public void cancelled() {
         this.latch.countDown();
      }
   }
}
