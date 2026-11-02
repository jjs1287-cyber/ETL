package org.etlforge.integration.activity.ui.history;

import org.etlforge.integration.activity.serverapi.history.HistoryRequest;
import org.etlforge.integration.activity.serverapi.history.OperationHistoryProvider;

public final class OperationHistoryController {
   private final OperationHistoryModel model;
   private final OperationHistoryProvider historyApi;

   public OperationHistoryController(OperationHistoryModel var1, OperationHistoryProvider var2) {
      this.model = var1;
      this.historyApi = var2;
   }

   public void downloadHistory() {
      HistoryRequest var1 = this.model.createRequest();
      GetOperationHistoryJob var2 = new GetOperationHistoryJob(this.model, this.historyApi, var1);
      this.model.setTalkingToServer(true);
      var2.submit();
   }
}
