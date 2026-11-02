package org.etlforge.integration.activity.ui.history;

import org.etlforge.integration.activity.serverapi.history.HistoryRequest;
import org.etlforge.integration.activity.serverapi.history.SourceHistoryProvider;

public final class SourceHistoryController {
   private final SourceHistoryModel model;
   private final SourceHistoryProvider historyApi;

   public SourceHistoryController(SourceHistoryModel var1, SourceHistoryProvider var2) {
      this.model = var1;
      this.historyApi = var2;
   }

   public void downloadHistory() {
      HistoryRequest var1 = this.model.createRequest();
      GetSourceHistoryJob var2 = new GetSourceHistoryJob(this.model, this.historyApi, var1);
      this.model.setTalkingToServer(true);
      var2.submit();
   }

   public void deleteSelectedFiles() {
      this.model.setTalkingToServer(true);
      DeleteHistoryJob var1 = new DeleteHistoryJob(this.historyApi, this.model, this);
      var1.submit();
   }
}
