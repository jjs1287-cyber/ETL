package org.etlforge.integration.activity.ui.history;

import org.etlforge.integration.activity.client.filter.TimeWindowFilter;
import org.etlforge.integration.activity.client.filter.TransferSizeFilter;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.MaxHistoryEntriesToFetchPreference;
import org.etlforge.integration.activity.ui.component.filter.ActivityEntryFilterUi;
import org.etlforge.integration.activity.ui.component.filter.MaxEntriesCompositeFilter;
import org.etlforge.integration.activity.ui.component.filter.TimeWindowFilterUi;
import org.etlforge.integration.activity.ui.component.filter.TransferSizeFilterUi;
import org.etlforge.integration.activity.ui.log.OperationLogTableColumn;
import org.etlforge.ui.widget.table.TableColumnMetaData;

final class OperationHistoryFilters extends MaxEntriesCompositeFilter<OperationLogEntry> {
   private final TransferSizeFilterUi<OperationLogEntry> transferSizeFilter = new TransferSizeFilterUi<>(PackageResources.OperationFilter.TRANSFER_SIZE);
   private final TimeWindowFilterUi<OperationLogEntry> timeWindowFilter = new TimeWindowFilterUi<>();

   public OperationHistoryFilters() {
      super(PackageResources.OperationFilter.TITLE, new MaxHistoryEntriesToFetchPreference().get());
      this.addFilter(this.transferSizeFilter);
      this.addFilter(this.timeWindowFilter);
   }

   public TimeWindowFilter getTimeFilter() {
      return this.timeWindowFilter.getFilter();
   }

   public TransferSizeFilter getTransferSizeFilter() {
      return this.transferSizeFilter.getFilter();
   }

   public ActivityEntryFilterUi<OperationLogEntry> getFilterForColumn(TableColumnMetaData var1) {
      if (var1 == OperationLogTableColumn.OPERATION) {
         return this.getNameFilterUi();
      } else if (var1 == OperationLogTableColumn.TRANSFER_SIZE) {
         return this.transferSizeFilter;
      } else {
         return var1 != OperationLogTableColumn.TIME_STARTED && var1 != OperationLogTableColumn.TIME_DONE ? null : this.timeWindowFilter;
      }
   }
}
