package org.etlforge.integration.activity.client.files;

import org.etlforge.integration.activity.serverapi.files.OperationFileProvider;
import org.etlforge.integration.activity.serverapi.log.OperationLogProvider;

public final class StatusHistoryApi {
   private final OperationLogProvider logApi;
   private final OperationFileProvider fileApi;

   public StatusHistoryApi(OperationLogProvider var1, OperationFileProvider var2) {
      this.logApi = var1;
      this.fileApi = var2;
   }

   public OperationLogProvider getLogApi() {
      return this.logApi;
   }

   public OperationFileProvider getFileApi() {
      return this.fileApi;
   }
}
