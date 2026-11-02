package com.etlforge.integration.server.engine.bulkload;

import com.google.common.base.Preconditions;
import com.etlforge.integration.server.engine.bulkload.batch.BatchProcessConfig;
import com.etlforge.integration.server.engine.bulkload.rest.HttpClientCreator;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.apache.http.client.HttpClient;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.engine.EngineSession;

public final class BulkLoadSession extends EngineSession<BulkLoadSession> {
   private final Object lock = new Object();
   private BulkLoadStaging bulkLoad;
   private final Logger logger;
   private volatile boolean bypassDeployedData;

   public BulkLoadSession(ExternalApiSupport var1, Logger var2) {
      super(new BulkLoadSessionId(), var1);
      this.logger = var2;
   }

   public BulkLoadSessionId getId() {
      return (BulkLoadSessionId)super.getId();
   }

   @Override
   protected void preCloseConnectionCleanup() {
      BulkLoadStaging var1;
      synchronized (this.lock) {
         var1 = this.bulkLoad;
      }

      if (var1 != null) {
         try {
            boolean var6 = var1.cancel(5L, TimeUnit.MINUTES);
            if (!var6) {
               this.logger
                  .severe(
                     "A request to cancel an on-going bulk load process timed out. This may result in a mismatch between the recorded data and what was actually sent to salesforce."
                  );
            }
         } catch (InterruptedException var4) {
            Thread.currentThread().interrupt();
         }
      }
   }

   public BulkLoadResult runBulkLoad(BulkLoadParams var1, BatchProcessConfig var2) throws BulkLoadException, ServerAccessException, InterruptedException {
      this.logger
         .info(
            String.format(
               "Starting new bulk load. Bulk load ID: %d. Operation instance ID: %d. Source file: %s. Target file: %s",
               var1.getBulkloadId(),
               var1.getOperationInstance(),
               var1.getSourceFile() != null ? var1.getSourceFile().getAbsolutePath() : "",
               var1.getTargetFile() != null ? var1.getTargetFile().getAbsolutePath() : ""
            )
         );
      HttpClient var3 = null;
      synchronized (this.lock) {
         Preconditions.checkState(this.bulkLoad == null, "A bulk load process is already running.");
         Connection var5 = this.getBackendConnection();
         this.bulkLoad = new BulkLoadStaging(var5, var1, var2);
         var3 = HttpClientCreator.newHttpClientDB(var5);
         this.bulkLoad.setBypassDeployedData(this.bypassDeployedData);
      }

      try {
         this.bulkLoad.setLogger(this.logger);
         return this.bulkLoad.runBulkLoad(var3);
      } finally {
         this.cleanupAfterBulkLoad(this.bulkLoad);
         this.logger.info(String.format("Bulk load done. Bulk load ID: %d. Operation instance ID: %d.", var1.getBulkloadId(), var1.getOperationInstance()));
      }
   }

   private void cleanupAfterBulkLoad(BulkLoadStaging var1) {
      try {
         var1.dispose();
      } finally {
         synchronized (this.lock) {
            this.bulkLoad = null;
         }
      }
   }

   public void setBypassDeployedData(boolean var1) {
      this.bypassDeployedData = var1;
   }
}
