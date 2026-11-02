package org.etlforge.integration.activity.ui.log;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import org.etlforge.integration.activity.client.log.OperationLogEntry;

public final class LogColumnComparator implements Comparator<OperationLogEntry> {
   private final OperationLogTableColumn column;
   private final boolean natural;

   public static LogColumnComparator ascending(OperationLogTableColumn var0) {
      return new LogColumnComparator(var0, true);
   }

   public static LogColumnComparator descending(OperationLogTableColumn var0) {
      return new LogColumnComparator(var0, false);
   }

   private LogColumnComparator(OperationLogTableColumn var1, boolean var2) {
      Preconditions.checkArgument(var1.isNaturallyComparable(), "column's values must be Comparable (was " + var1.getDataClass().getName() + ")");
      this.column = var1;
      this.natural = var2;
   }

   public int compare(OperationLogEntry var1, OperationLogEntry var2) {
      Object var3 = this.column.createColumnValue(var1);
      Object var4 = this.column.createColumnValue(var2);
      if (var3 == null) {
         return var4 == null ? 0 : 1;
      }

      if (var4 == null) {
         return -1;
      }

      int var5 = ((Comparable)var3).compareTo(var4);
      return this.natural ? var5 : -var5;
   }
}
