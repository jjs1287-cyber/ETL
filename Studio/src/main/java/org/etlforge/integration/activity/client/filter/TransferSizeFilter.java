package org.etlforge.integration.activity.client.filter;

import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.util.arithmetic.ComparisonOperator;

public final class TransferSizeFilter extends ActivityEntryFilter {
   private final ComparisonOperator operator;
   private final long valueInBytes;

   public static TransferSizeFilter transparent() {
      return new TransferSizeFilter(null, 0L);
   }

   public TransferSizeFilter(ComparisonOperator var1, long var2) {
      this.operator = var1;
      this.valueInBytes = var2;
   }

   public ComparisonOperator getOperator() {
      return this.operator;
   }

   public long getValueInBytes() {
      return this.valueInBytes;
   }

   @Override
   public boolean isValid(OperationLogEntry var1) {
      return this.check(var1.getTotalTransferSizeInBytes());
   }

   @Override
   public boolean isValid(SourceHistoryEntry var1) {
      return this.check(var1.getFileSizeInBytes());
   }

   private boolean check(long var1) {
      boolean var3 = true;
      ComparisonOperator var4 = this.getOperator();
      if (var4 != null) {
         long var5 = this.getValueInBytes();
         if (var1 >= 0L) {
            var3 = var4.isTrue(var1, var5);
         }
      }

      return var3;
   }
}
