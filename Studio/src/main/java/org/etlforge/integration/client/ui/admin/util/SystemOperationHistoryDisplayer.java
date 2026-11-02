package org.etlforge.integration.client.ui.admin.util;

import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.serverapi.history.OperationHistoryProvider;
import org.etlforge.integration.activity.serverapi.history.SourceHistoryProvider;
import org.etlforge.integration.activity.ui.component.page.ActivityPage;
import org.etlforge.integration.activity.ui.history.OperationHistoryPage;

public final class SystemOperationHistoryDisplayer extends SystemActivityDisplayer {
   private final OperationHistoryProvider opHistoryApi;
   private final SourceHistoryProvider sourceHistoryApi;
   private boolean deleteSourceHistorySupported = true;

   public SystemOperationHistoryDisplayer(OperationHistoryProvider var1, SourceHistoryProvider var2) {
      this.opHistoryApi = var1;
      this.sourceHistoryApi = var2;
   }

   public void setDeleteSourceHistorySupported(boolean var1) {
      this.deleteSourceHistorySupported = var1;
   }

   @Override
   protected ActivityPage createPage(ActivitySubject var1) {
      return new OperationHistoryPage(var1, this.opHistoryApi, this.sourceHistoryApi, this.deleteSourceHistorySupported);
   }
}
