package org.etlforge.integration.salesforce.bulkload.backend;

import com.google.common.base.Objects;
import org.etlforge.system.ArgCheck;

public final class SuccessAndFailureCounts {
   public static final SuccessAndFailureCounts ZERO = new SuccessAndFailureCounts(0L, 0L);
   public final long successCount;
   public final long failureCount;

   public SuccessAndFailureCounts(long var1, long var3) {
      this.successCount = ArgCheck.checkNotNegative(var1);
      this.failureCount = ArgCheck.checkNotNegative(var3);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("success", this.successCount).add("failure", this.failureCount).toString();
   }
}
