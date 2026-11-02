package com.etlforge.integration.server.engine.bulkload;

import com.etlforge.integration.server.engine.bulkload.batch.BatchProcessConfig;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.ServerDbException;
import org.etlforge.integration.server.engine.EngineSessionException;
import org.etlforge.integration.server.engine.SessionExecutionWrapper;

public final class BulkLoadExecutor extends SessionExecutionWrapper<BulkLoadSession, BulkLoadResult> {
   private final BulkLoadParams params;
   private final BatchProcessConfig batchConfig;

   public BulkLoadExecutor(BulkLoadSession var1, BulkLoadParams var2, BatchProcessConfig var3) {
      super(var1);
      this.params = var2;
      this.batchConfig = var3;
   }

   protected BulkLoadResult doWork(BulkLoadSession var1) throws EngineSessionException, ServerAccessException, ServerDbException {
      try {
         return var1.runBulkLoad(this.params, this.batchConfig);
      } catch (BulkLoadException var3) {
         throw new EngineSessionException(var3);
      } catch (InterruptedException var4) {
         Thread.currentThread().interrupt();
         throw new EngineSessionException("The server thread was interrupted during the bulk load process.");
      }
   }
}
