package org.etlforge.integration.activity.client.history;

import java.util.Date;
import org.etlforge.integration.activity.client.AbstractActivityEntry;
import org.etlforge.integration.activity.client.TimeWindow;
import org.etlforge.integration.activity.client.filter.ActivityEntryFilter;
import org.etlforge.integration.data.entity.id.OperationId;

public final class SourceHistoryEntry extends AbstractActivityEntry {
   private final String sourceFiles;
   private final int fileSizeInBytes;
   private final Date fileTimestamp;

   public SourceHistoryEntry(long var1, OperationId var3, String var4, String var5, int var6, Date var7) {
      super(var1, var3, var4);
      this.sourceFiles = var5;
      this.fileSizeInBytes = var6;
      this.fileTimestamp = var7;
   }

   public String getSourceFiles() {
      return this.sourceFiles;
   }

   public int getFileSizeInBytes() {
      return this.fileSizeInBytes;
   }

   public Date getFileTimestamp() {
      return this.fileTimestamp;
   }

   @Override
   public boolean occurredWithinTimeWindow(Date var1, Date var2) {
      return new TimeWindow(var1, var2).isInside(this.fileTimestamp);
   }

   @Override
   public boolean passes(ActivityEntryFilter var1) {
      return var1.isValid(this);
   }
}
