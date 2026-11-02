package org.etlforge.integration.activity.client.log;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.integration.activity.client.AbstractActivityEntry;
import org.etlforge.integration.activity.client.TimeWindow;
import org.etlforge.integration.activity.client.filter.ActivityEntryFilter;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.time.Duration;

public final class OperationLogEntry extends AbstractActivityEntry {
   private final long opInstanceId;
   private final LogStatus status;
   private volatile Date timeReceived;
   private final Date startTime;
   private final Date statusTime;
   private final String shortMessage;
   private final ImmutableList<OperationLogMessage> details;
   private volatile Long parentOpInstanceId;
   private volatile Long messageId;
   private volatile String from;
   private volatile OperationStats stats = OperationStats.NONE;

   public OperationLogEntry(OperationId var1, String var2, long var3, LogStatus var5, Date var6, Date var7, String var8, List<OperationLogMessage> var9) {
      super(var3, var1, var2);
      this.opInstanceId = ArgCheck.checkPositive(var3);
      this.status = var5;
      this.startTime = copy(var6);
      this.statusTime = copy(var7);
      this.shortMessage = Strings.nullToEmpty(var8);
      this.details = ImmutableList.copyOf(var9);
   }

   private static Date copy(Date var0) {
      return var0 == null ? null : new Date(var0.getTime());
   }

   public long getOpInstanceId() {
      return this.opInstanceId;
   }

   public LogStatus getLogStatus() {
      return this.status;
   }

   public Date getStartTime() {
      return copy(this.startTime);
   }

   public Date getTimeCompleted() {
      return this.status.isDone() ? copy(this.statusTime) : null;
   }

   public Duration getDuration() {
      return this.startTime != null && this.status.isDone() ? new Duration(this.startTime, this.statusTime) : null;
   }

   public Date getStatusTime() {
      return copy(this.statusTime);
   }

   public String getShortMessage() {
      return this.shortMessage;
   }

   public boolean hasDetails() {
      return !this.details.isEmpty();
   }

   public Iterable<OperationLogMessage> getDetails() {
      return this.details;
   }

   @Override
   public boolean occurredWithinTimeWindow(Date var1, Date var2) {
      return new TimeWindow(var1, var2).isInside(this.statusTime);
   }

   @Override
   public boolean passes(ActivityEntryFilter var1) {
      return var1.isValid(this);
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("Status: ");
      if (this.status == null) {
         var1.append("Undefined");
      } else {
         var1.append(this.status.toString());
      }

      var1.append("  Start time: ");
      var1.append(this.startTime);
      var1.append("  Status time: ");
      var1.append(this.statusTime);
      var1.append("  Short message: ");
      var1.append(this.shortMessage);

      for (OperationLogMessage var3 : this.details) {
         var1.append(SystemUtils.LINE_SEPARATOR);
         var1.append(var3.toString());
      }

      return var1.toString();
   }

   public Long getParentOpInstanceId() {
      return this.parentOpInstanceId;
   }

   public void setParentOpInstanceId(Long var1) {
      this.parentOpInstanceId = var1;
   }

   public OperationStats getStats() {
      return this.stats;
   }

   public void setStats(OperationStats var1) {
      this.stats = var1;
   }

   public String getFrom() {
      return this.from;
   }

   public void setFrom(String var1) {
      this.from = var1;
   }

   public Date getTimeReceived() {
      return this.timeReceived;
   }

   public void setTimeReceived(Date var1) {
      this.timeReceived = var1;
   }

   public Long getMessageId() {
      return this.messageId;
   }

   public void setMessageId(Long var1) {
      this.messageId = var1;
   }

   public long getTotalTransferSizeInBytes() {
      return this.stats.sourceTransferBytes + this.stats.targetTransferBytes;
   }
}
