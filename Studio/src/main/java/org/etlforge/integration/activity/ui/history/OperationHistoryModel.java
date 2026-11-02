package org.etlforge.integration.activity.ui.history;

import com.google.common.base.Predicate;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.filter.CompositeActivityEntryFilter;
import org.etlforge.integration.activity.client.filter.TransferSizeFilter;
import org.etlforge.integration.activity.serverapi.history.HistoryRequest;
import org.etlforge.integration.activity.ui.AbstractOperationLogModel;

public final class OperationHistoryModel extends AbstractOperationLogModel {
   private TransferSizeFilter sizeFilter;

   public OperationHistoryModel(ActivitySubject var1) {
      super(var1);
   }

   @Override
   protected Predicate<ActivityEntry> createCompositeFilter() {
      CompositeActivityEntryFilter var1 = new CompositeActivityEntryFilter(this.getNameFilter(), this.getTimeFilter());
      if (this.sizeFilter != null) {
         var1.add(this.sizeFilter);
      }

      return var1;
   }

   public TransferSizeFilter getSizeFilter() {
      return this.sizeFilter;
   }

   public void setSizeFilter(TransferSizeFilter var1) {
      this.sizeFilter = var1;
   }

   public HistoryRequest createRequest() {
      return RequestBuilder.buildRequest(this, this.sizeFilter);
   }
}
