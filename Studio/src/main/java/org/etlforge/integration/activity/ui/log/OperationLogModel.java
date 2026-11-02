package org.etlforge.integration.activity.ui.log;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.filter.CompositeActivityEntryFilter;
import org.etlforge.integration.activity.client.log.LogEntryComparator;
import org.etlforge.integration.activity.client.log.LogStatusFilter;
import org.etlforge.integration.activity.client.log.MessageFilter;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.AbstractOperationLogModel;

public final class OperationLogModel extends AbstractOperationLogModel {
   private LogStatusFilter statusFilter;
   private MessageFilter messageFilter;
   private long timeOfLastEntry = -1L;

   public OperationLogModel(ActivitySubject var1) {
      super(var1);
      this.statusFilter = new LogStatusFilter(null);
      this.messageFilter = new MessageFilter(null);
   }

   @Override
   protected void newEntriesReceived(List<OperationLogEntry> var1) {
      this.updateTimeOfLatestEntry(var1);
   }

   private void updateTimeOfLatestEntry(List<OperationLogEntry> var1) {
      if (var1 != null && !var1.isEmpty()) {
         OperationLogEntry var2 = Collections.min(var1, LogEntryComparator.LATEST_CHANGE_FIRST);
         Calendar var3 = Calendar.getInstance();
         var3.setTime(var2.getStatusTime());
         var3.add(13, 1);
         this.timeOfLastEntry = var3.getTimeInMillis();
      }
   }

   @Override
   protected Predicate<ActivityEntry> createCompositeFilter() {
      return new CompositeActivityEntryFilter(this.statusFilter, this.getNameFilter(), this.getTimeFilter(), this.messageFilter);
   }

   public Date getTimeOfLastEntry() {
      return this.timeOfLastEntry >= 0L ? new Date(this.timeOfLastEntry) : null;
   }

   public LogStatusFilter getStatusFilter() {
      return this.statusFilter;
   }

   public void setStatusFilter(LogStatusFilter var1) {
      this.statusFilter = Preconditions.checkNotNull(var1);
   }

   public MessageFilter getMessageFilter() {
      return this.messageFilter;
   }

   public void setMessageFilter(MessageFilter var1) {
      this.messageFilter = Preconditions.checkNotNull(var1);
   }
}
