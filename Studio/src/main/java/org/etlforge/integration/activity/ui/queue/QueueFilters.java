package org.etlforge.integration.activity.ui.queue;

import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.client.queue.QueueStatusFilter;
import org.etlforge.integration.activity.ui.MaxQueueEntriesToFetchPreference;
import org.etlforge.integration.activity.ui.component.filter.ActivityEntryFilterUi;
import org.etlforge.integration.activity.ui.component.filter.MaxEntriesCompositeFilter;
import org.etlforge.integration.activity.ui.log.OperationLogTableColumn;
import org.etlforge.ui.widget.table.TableColumnMetaData;

final class QueueFilters extends MaxEntriesCompositeFilter<OperationLogEntry> {
   private final QueueStatusFilterUi statusFilter = new QueueStatusFilterUi();

   public QueueFilters() {
      super(PackageResources.FILTER_TITLE, new MaxQueueEntriesToFetchPreference().get());
      this.addFilter(this.statusFilter);
   }

   public QueueStatusFilter getStatusFilter() {
      return this.statusFilter.getFilter();
   }

   public ActivityEntryFilterUi<OperationLogEntry> getFilterForColumn(TableColumnMetaData var1) {
      if (var1 == OperationLogTableColumn.OPERATION) {
         return this.getNameFilterUi();
      } else {
         return var1 == OperationLogTableColumn.STATUS ? this.statusFilter : null;
      }
   }
}
