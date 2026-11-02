package org.etlforge.integration.client.ui.connector;

import java.util.concurrent.Callable;
import javax.swing.SwingWorker;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.client.connector.schema.DownloadedSchemasProcessor;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.ui.interchange.entity.MappingStructureRefresher;
import org.etlforge.integration.data.connector.AbstractConnectorCall;
import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;
import org.etlforge.system.thread.Runnables;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public abstract class RefreshFunctionJobFactory {
   private final FileStore fileStore;
   private final FileUploadApi fileUploadApi;
   private final MappingStructureRefresher txRefresher;
   private WaitService waitService = WaitService.NONE;
   private Runnable pageRefresher = Runnables.NO_OP;

   public RefreshFunctionJobFactory(FileStore var1, FileUploadApi var2, MappingStructureRefresher var3) {
      this.fileStore = var1;
      this.fileUploadApi = var2;
      this.txRefresher = var3;
   }

   public final void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public final void setPageRefresher(Runnable var1) {
      this.pageRefresher = var1;
   }

   public final Runnable createRefreshJob(ConnectorCall var1) {
      Callable var2 = this.backgroundJob(var1);
      final WaitLock var3 = this.waitService.startWait();
      final SwingWorker var4 = Computation.create(var2)
         .swingWorkerBuilder()
         .withErrorLog(ErrorLevel.ATTENTION, this.getClass())
         .runWhenFinished(new Runnable() {
            @Override
            public void run() {
               var3.release();
            }
         })
         .build();
      return new Runnable() {
         @Override
         public void run() {
            var4.execute();
         }
      };
   }

   private Callable<Void> backgroundJob(ConnectorCall var1) {
      return this.getRefreshFunctionJob(var1);
   }

   protected RefreshFunctionJob getRefreshFunctionJob(ConnectorCall var1) {
      Callable var2 = this.getSchemaGenerator(var1);
      return new RefreshFunctionJob(
         var1, var2, new DownloadedSchemasProcessor((AbstractConnectorCall)var1, this.fileStore, this.fileUploadApi), this.pageRefresher, this.txRefresher
      );
   }

   public abstract Callable<Schemas> getSchemaGenerator(ConnectorCall var1);
}
