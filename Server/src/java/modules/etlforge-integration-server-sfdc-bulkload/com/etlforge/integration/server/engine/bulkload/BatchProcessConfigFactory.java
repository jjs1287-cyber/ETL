package com.etlforge.integration.server.engine.bulkload;

import com.etlforge.integration.server.engine.bulkload.batch.BatchProcessConfig;
import java.sql.Connection;
import org.etlforge.integration.server.db.ServerDbException;

public final class BatchProcessConfigFactory {
   private BatchProcessConfig template;

   public BatchProcessConfigFactory(Connection var1) {
   }

   public void setTemplate(BatchProcessConfig var1) {
      this.template = var1;
   }

   public BatchProcessConfig getConfig(long var1) throws ServerDbException {
      return this.template == null ? BatchProcessConfig.DEFAULT : this.template;
   }
}
