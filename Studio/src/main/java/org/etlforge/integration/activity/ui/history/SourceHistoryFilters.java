package org.etlforge.integration.activity.ui.history;

import org.etlforge.integration.activity.client.filter.TimeWindowFilter;
import org.etlforge.integration.activity.client.filter.TransferSizeFilter;
import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.activity.ui.MaxHistoryEntriesToFetchPreference;
import org.etlforge.integration.activity.ui.component.filter.ActivityEntryFilterUi;
import org.etlforge.integration.activity.ui.component.filter.MaxEntriesCompositeFilter;
import org.etlforge.integration.activity.ui.component.filter.TimeWindowFilterUi;
import org.etlforge.integration.activity.ui.component.filter.TransferSizeFilterUi;
import org.etlforge.ui.widget.table.TableColumnMetaData;

final class SourceHistoryFilters extends MaxEntriesCompositeFilter<SourceHistoryEntry> {
   private final TimeWindowFilterUi<SourceHistoryEntry> timeWindowFilter = new TimeWindowFilterUi<>();
   private final TransferSizeFilterUi<SourceHistoryEntry> fileSizeFilter = new TransferSizeFilterUi<>(PackageResources.SourceFilter.FILE_SIZE);

   public SourceHistoryFilters() {
      super(PackageResources.SourceFilter.TITLE, new MaxHistoryEntriesToFetchPreference().get());
      this.addFilter(this.timeWindowFilter);
      this.addFilter(this.fileSizeFilter);
   }

   public TimeWindowFilter getTimeFilter() {
      return this.timeWindowFilter.getFilter();
   }

   public TransferSizeFilter getFileSizeFilter() {
      return this.fileSizeFilter.getFilter();
   }

   public ActivityEntryFilterUi<SourceHistoryEntry> getFilterForColumn(TableColumnMetaData var1) {
      if (var1 == SourceHistoryTableModel.OPERATION) {
         return this.getNameFilterUi();
      } else if (var1 == SourceHistoryTableModel.FILE_SIZE) {
         return this.fileSizeFilter;
      } else {
         return var1 == SourceHistoryTableModel.TIMESTAMP ? this.timeWindowFilter : null;
      }
   }
}
