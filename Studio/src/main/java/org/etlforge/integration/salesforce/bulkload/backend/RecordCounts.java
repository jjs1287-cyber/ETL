package org.etlforge.integration.salesforce.bulkload.backend;

import com.google.common.base.Objects;
import org.etlforge.system.ArgCheck;

public final class RecordCounts {
   public static final RecordCounts ZERO = new RecordCounts(0, 0, 0, 0);
   public static final RecordCounts DONEDUMMY = new RecordCounts(1, 1, 1, 0);
   public final int sent;
   public final int processed;
   public final int success;
   public final int failure;

   public static RecordCounts notProcessed(int var0) {
      return new RecordCounts(var0, 0, 0, 0);
   }

   public RecordCounts(int var1, int var2, int var3, int var4) {
      this.sent = ArgCheck.checkNotNegative(var1);
      this.processed = ArgCheck.checkNotNegative(var2);
      this.success = ArgCheck.checkNotNegative(var3);
      this.failure = ArgCheck.checkNotNegative(var4);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.sent, this.processed, this.success, this.failure);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof RecordCounts)) {
         return false;
      }

      RecordCounts var2 = (RecordCounts)var1;
      return this.sent == var2.sent && this.processed == var2.processed && this.success == var2.success && this.failure == var2.failure;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("total", this.sent)
         .add("processed", this.processed)
         .add("success", this.success)
         .add("failure", this.failure)
         .toString();
   }

   public RecordCounts add(RecordCounts var1) {
      return new RecordCounts(this.sent + var1.sent, this.processed + var1.processed, this.success + var1.success, this.failure + var1.failure);
   }
}
