package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.ResultProducingUiJob;
import org.etlforge.integration.debug.client.file.DataFile;
import org.etlforge.integration.debug.client.file.DataFileIdentifier;
import org.etlforge.integration.debug.client.file.DataFilesInfo;
import org.etlforge.integration.debug.serverapi.DataFileProvider;
import org.etlforge.integration.debug.serverapi.DownloadDataFileCallback;
import org.etlforge.system.callback.Receiver;
import org.etlforge.util.id.KongaGuid;

final class DataFileRetriever {
   private final TransformationFileStore localFiles;
   private final DataFileProvider downloader;

   public DataFileRetriever(TransformationFileStore var1, DataFileProvider var2) {
      this.localFiles = var1;
      this.downloader = var2;
   }

   public void retrieveDataFile(DataFileIdentifier var1, Receiver<DataFileRetriever.Result> var2) {
      KongaGuid var3 = null;
      TransformationDataFile var4 = TransformationDataFile.NULL;
      synchronized (this.localFiles) {
         DataFilesInfo var6 = this.localFiles.getCurrentFilesInfo();
         if (var6 == null || !var6.contains(var1)) {
            boolean var7 = true;
            var2.handle(DataFileRetriever.Result.error(var7));
            return;
         }

         var3 = var6.getGuid();
         var4 = this.localFiles.getFile(var1);
      }

      if (var4 == TransformationDataFile.NULL) {
         this.downloadFile(var3, var1, var2);
      } else {
         var2.handle(DataFileRetriever.Result.success(var4));
      }
   }

   private void downloadFile(KongaGuid var1, DataFileIdentifier var2, Receiver<DataFileRetriever.Result> var3) {
      DataFileRetriever.Downloader var4 = new DataFileRetriever.Downloader(var1, var2);
      var4.setResultReceiver(var3);
      var4.submit();
   }

   private class Downloader extends ResultProducingUiJob<DataFileRetriever.Result> implements DownloadDataFileCallback {
      private final KongaGuid guid;
      private final DataFileIdentifier fileId;

      public Downloader(KongaGuid var2, DataFileIdentifier var3) {
         super("Downloading a requested data file from the server.");
         this.guid = var2;
         this.fileId = var3;
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(this);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         DataFileRetriever.this.downloader.downloadDataFile(this.guid, this.fileId, this);
      }

      @Override
      protected void onJobCancelled() {
         this.cancelled();
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.attention(DataFileRetriever.class, null, var1);
         boolean var2 = false;
         this.resultWasComputed(DataFileRetriever.Result.error(var2));
      }

      @Override
      public void cancelled() {
         this.resultWasComputed(DataFileRetriever.Result.canceled());
      }

      @Override
      public void fileWasDownloaded(DataFileIdentifier var1, DataFile var2) {
         this.setCanBeCancelled(false);
         this.setMayBeInterrupted(false);
         TransformationDataFile var3 = TransformationDataFile.NULL;
         synchronized (DataFileRetriever.this.localFiles) {
            DataFilesInfo var5 = DataFileRetriever.this.localFiles.getCurrentFilesInfo();
            if (var5 != null) {
               var3 = DataFileRetriever.this.localFiles.addDataFile(this.guid, var1, var2);
            }
         }

         if (var3 == TransformationDataFile.NULL) {
            this.cancelled();
         } else {
            this.resultWasComputed(DataFileRetriever.Result.success(var3));
         }
      }
   }

   public static final class Result {
      public final TransformationDataFile dataFile;
      public final boolean canceled;
      public final boolean unknownFile;
      public final boolean failed;

      private static DataFileRetriever.Result canceled() {
         return new DataFileRetriever.Result(TransformationDataFile.NULL, true, false, false);
      }

      private static DataFileRetriever.Result error(boolean var0) {
         return new DataFileRetriever.Result(TransformationDataFile.NULL, false, true, var0);
      }

      private static DataFileRetriever.Result success(TransformationDataFile var0) {
         return new DataFileRetriever.Result(var0, false, false, false);
      }

      private Result(TransformationDataFile var1, boolean var2, boolean var3, boolean var4) {
         this.dataFile = var1;
         this.canceled = var2;
         this.failed = var3;
         this.unknownFile = var4;
      }
   }
}
