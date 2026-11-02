package org.etlforge.integration.activity.ui.files;

import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.activity.client.files.StatusHistoryApi;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.serverapi.files.ListAvailableFilesCallback;
import org.etlforge.integration.activity.serverapi.files.OperationFileProvider;
import org.etlforge.integration.activity.serverapi.files.OperationFileRequest;
import org.etlforge.integration.activity.serverapi.log.GetOperationLogCallback;
import org.etlforge.integration.activity.serverapi.log.OperationLogProvider;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;

final class DownloadStatusHistoryWorkflow {
   private final StatusHistoryModel model;
   private final StatusHistoryApi api;
   private Receiver<String> messageReceiver = NoReceiver.create();
   private final DownloadStatusHistoryWorkflow.LogCallback logCallback;

   public DownloadStatusHistoryWorkflow(StatusHistoryModel var1, StatusHistoryApi var2) {
      this.model = var1;
      this.api = var2;
      this.logCallback = new DownloadStatusHistoryWorkflow.LogCallback(var1, var2.getFileApi(), this.messageReceiver);
   }

   public void setMessageReceiver(Receiver<String> var1) {
      this.messageReceiver = var1;
      this.logCallback.setMessageReceiver(var1);
   }

   public ExceptionHandler<Throwable> getUncaughtExceptionHandler() {
      return this.logCallback;
   }

   public void run() {
      this.messageReceiver.handle("Retrieving operation log");
      OperationLogProvider var1 = this.api.getLogApi();
      var1.getOperationLog(this.model.getLogRequest(), this.logCallback);
   }

   private abstract static class AbstractCallback implements ServerCallback {
      protected final StatusHistoryModel model;
      protected volatile Receiver<String> messageReceiver;
      protected volatile boolean canceled;

      protected AbstractCallback(StatusHistoryModel var1, Receiver<String> var2) {
         this.model = var1;
         this.messageReceiver = var2;
      }

      public final void setMessageReceiver(Receiver<String> var1) {
         this.messageReceiver = var1;
      }

      public void sendMessage(String var1) {
         this.messageReceiver.handle(var1);
      }

      @Override
      public final void caught(Throwable var1) {
         if (!this.canceled) {
            this.model.setBusy(false);
            ErrorLog.attention(StatusHistoryController.class, null, var1);
         }
      }

      @Override
      public final void cancelled() {
         if (!this.canceled) {
            this.model.setBusy(false);
            this.canceled = true;
         }
      }
   }

   private static class FileListCallback extends DownloadStatusHistoryWorkflow.AbstractCallback implements ListAvailableFilesCallback {
      private final List<OperationLogEntry> logs;
      private volatile boolean canceled;

      public FileListCallback(StatusHistoryModel var1, Receiver<String> var2, List<OperationLogEntry> var3) {
         super(var1, var2);
         this.logs = var3;
      }

      @Override
      public void gotFileList(ServerInfo var1, List<OperationFile> var2) {
         if (!this.canceled) {
            this.sendMessage("Updating the UI");
            ResultListBuilder var3 = new ResultListBuilder(this.model, this.logs, var2);
            var3.build();
         }
      }
   }

   private static class LogCallback extends DownloadStatusHistoryWorkflow.AbstractCallback implements GetOperationLogCallback {
      private final OperationFileProvider fileApi;

      public LogCallback(StatusHistoryModel var1, OperationFileProvider var2, Receiver<String> var3) {
         super(var1, var3);
         this.fileApi = var2;
      }

      @Override
      public void logWasRetrieved(List<OperationLogEntry> var1) {
         if (!this.canceled) {
            if (var1.isEmpty()) {
               this.model.setResultList(Collections.emptyList());
               this.model.setBusy(false);
            } else {
               this.downloadFileList(var1);
            }
         }
      }

      private void downloadFileList(List<OperationLogEntry> var1) {
         OperationFileRequest var2 = this.createFileRequest(var1);
         DownloadStatusHistoryWorkflow.FileListCallback var3 = new DownloadStatusHistoryWorkflow.FileListCallback(this.model, this.messageReceiver, var1);
         this.sendMessage("Asking for result files");
         this.fileApi.listAvailableFiles(var2, var3);
      }

      private OperationFileRequest createFileRequest(List<OperationLogEntry> var1) {
         ArrayList var2 = Lists.newArrayList();

         for (OperationLogEntry var4 : var1) {
            var2.add(var4.getOpInstanceId());
         }

         return OperationFileRequest.forOpInstances(Longs.toArray(var2));
      }
   }
}
