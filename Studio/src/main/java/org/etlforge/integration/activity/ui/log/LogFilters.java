package org.etlforge.integration.activity.ui.log;

import javax.swing.event.ChangeListener;
import org.etlforge.integration.activity.client.filter.TimeWindowFilter;
import org.etlforge.integration.activity.client.log.LogStatusFilter;
import org.etlforge.integration.activity.client.log.MessageFilter;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.MaxLogEntriesToFetchPreference;
import org.etlforge.integration.activity.ui.component.filter.ActivityEntryFilterUi;
import org.etlforge.integration.activity.ui.component.filter.MaxEntriesCompositeFilter;
import org.etlforge.integration.activity.ui.component.filter.TimeWindowFilterUi;

final class LogFilters extends MaxEntriesCompositeFilter<OperationLogEntry> {
   private final LogStatusFilterUi statusFilter = new LogStatusFilterUi();
   private final TimeWindowFilterUi<OperationLogEntry> timeWindowFilter;
   private final MessageFilterUi messageFilter;

   public LogFilters() {
      super(PackageResources.FILTER_TITLE, new MaxLogEntriesToFetchPreference().get());
      this.addFilter(this.statusFilter);
      this.timeWindowFilter = new TimeWindowFilterUi<>("Time Filter");
      this.timeWindowFilter.setFieldLabels("Status changed after", "Status changed before");
      this.addFilter(this.timeWindowFilter);
      this.messageFilter = new MessageFilterUi();
      this.addFilter(this.messageFilter);
   }

   public LogStatusFilter getStatusFilter() {
      return this.statusFilter.getFilter();
   }

   public TimeWindowFilter getTimeFilter() {
      return this.timeWindowFilter.getFilter();
   }

   public void addTimeWindowFilterListener(ChangeListener var1) {
      this.timeWindowFilter.addChangeListener(var1);
   }

   public MessageFilter getMessageFilter() {
      return this.messageFilter.getFilter();
   }

   @Override
   protected boolean useScrollPane() {
      return true;
   }

   public ActivityEntryFilterUi<OperationLogEntry> getFilterForColumn(int var1) {
      return null;
   }
}
