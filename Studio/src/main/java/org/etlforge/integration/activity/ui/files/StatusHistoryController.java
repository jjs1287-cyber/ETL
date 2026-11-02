package org.etlforge.integration.activity.ui.files;

import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.activity.client.files.ResultFileSummary;
import org.etlforge.integration.activity.client.files.StatusFileDownloaderFactory;
import org.etlforge.integration.activity.client.files.StatusHistoryApi;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.system.callback.Receiver;

public final class StatusHistoryController implements OperationFileController {
   private final StatusHistoryModel model;
   private final DefaultOperationFileController fileController;
   private final StatusHistoryApi api;

   public StatusHistoryController(StatusHistoryModel var1, StatusHistoryApi var2) {
      this.model = var1;
      this.api = var2;
      this.fileController = this.createFileController();
   }

   private DefaultOperationFileController createFileController() {
      DefaultOperationFileController var1 = new DefaultOperationFileController(this.model.getCsvParams());
      var1.setWaitService(this.model.asWaitService());
      return var1;
   }

   public void enableRemoteFileSupport(StatusFileDownloaderFactory var1) {
      this.fileController.enableDownloadOfRemoteFiles(var1);
   }

   public void downloadHistory() {
      this.model.setBusy(true);
      DownloadStatusHistoryWorkflow var1 = new DownloadStatusHistoryWorkflow(this.model, this.api);
      StatusHistoryController.HistoryWorker var2 = new StatusHistoryController.HistoryWorker(var1);
      var2.submit();
   }

   @Override
   public void openFile(OperationFile var1, Receiver<ResultFileSummary> var2) {
      this.fileController.openFile(var1, var2);
   }

   private static class HistoryWorker extends UiJob {
      private final DownloadStatusHistoryWorkflow workflow;

      public HistoryWorker(DownloadStatusHistoryWorkflow var1) {
         super("Fetching result history");
         this.workflow = var1;
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(var1.getUncaughtExceptionHandler());
         var1.setMessageReceiver(new Receiver<String>() {
            public void handle(String var1) {
               HistoryWorker.this.setMessage(var1);
            }
         });
      }

      @Override
      protected void runImpl() throws InterruptedException {
         this.workflow.run();
      }
   }
}
