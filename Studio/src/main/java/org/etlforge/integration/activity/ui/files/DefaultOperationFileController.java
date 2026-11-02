package org.etlforge.integration.activity.ui.files;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.io.File;
import java.util.concurrent.Callable;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.activity.client.files.ResultFileParser;
import org.etlforge.integration.activity.client.files.ResultFileSummary;
import org.etlforge.integration.activity.client.files.StatusFileDownloaderFactory;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.csv.CSVParams;

public final class DefaultOperationFileController implements OperationFileController {
   private final CSVParams csvParams;
   private WaitService waitService = WaitService.NONE;
   private Optional<StatusFileDownloaderFactory> remoteFileDownloaderFactory = Optional.absent();

   public DefaultOperationFileController(CSVParams var1) {
      this.csvParams = Preconditions.checkNotNull(var1);
   }

   public void enableDownloadOfRemoteFiles(StatusFileDownloaderFactory var1) {
      this.remoteFileDownloaderFactory = Optional.of(var1);
   }

   public void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1);
   }

   @Override
   public void openFile(OperationFile var1, Receiver<ResultFileSummary> var2) {
      Callable var3 = this.createLocalFileProducer(var1);
      if (var3 != null) {
         DefaultOperationFileController.FileParser var4 = new DefaultOperationFileController.FileParser(var1, var3);
         ViewResultFileWorker var5 = new ViewResultFileWorker(var4, var2);
         var5.executeWithWaitService(this.waitService);
      }
   }

   private Callable<File> createLocalFileProducer(final OperationFile var1) {
      if (var1.existsLocally()) {
         return new Callable<File>() {
            public File call() throws Exception {
               return var1.asLocalFile();
            }
         };
      }

      if (this.remoteFileDownloaderFactory.isPresent()) {
         return this.remoteFileDownloaderFactory.get().newDownloader(var1);
      }

      NonBlockingAlert.error(
         "The file " + var1.asLocalFile().getAbsolutePath() + " does not exist on the local client. This version does not support accessing remote files.",
         "Not Supported"
      );
      return null;
   }

   private class FileParser implements Callable<ResultFileSummary> {
      private final OperationFile opFile;
      private final Callable<File> localFileProducer;

      public FileParser(OperationFile var2, Callable<File> var3) {
         this.opFile = var2;
         this.localFileProducer = var3;
      }

      public ResultFileSummary call() throws Exception {
         File var1 = this.localFileProducer.call();
         if (var1 == null) {
            return null;
         }

         ResultFileParser var2 = new ResultFileParser(var1, this.opFile.getEncoding(), DefaultOperationFileController.this.csvParams);
         return var2.call();
      }
   }
}
