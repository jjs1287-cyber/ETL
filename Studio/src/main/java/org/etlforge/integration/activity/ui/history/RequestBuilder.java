package org.etlforge.integration.activity.ui.history;

import org.etlforge.integration.activity.client.filter.TransferSizeFilter;
import org.etlforge.integration.activity.serverapi.history.HistoryRequest;
import org.etlforge.integration.activity.ui.AbstractActivityDisplayModel;
import org.etlforge.util.SortOrder;

final class RequestBuilder {
   public static HistoryRequest buildRequest(AbstractActivityDisplayModel<?> var0, TransferSizeFilter var1) {
      HistoryRequest var2 = new HistoryRequest();
      var2.subjectId = var0.getSubjectId();
      var2.nameFilter = var0.getNameFilter();
      var2.maxNumberOfEntriesFilter = var0.getMaxEntriesFilter();
      var2.timeFilter = var0.getTimeFilter();
      var2.transferSizeFilter = var1;
      var2.sortOrder = SortOrder.DESCENDING;
      return var2;
   }

   private RequestBuilder() {
   }
}
