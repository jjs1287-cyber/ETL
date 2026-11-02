package org.etlforge.integration.salesforce.bulkload.backend;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;

public final class SectionOfFile {
   public final long startPos;
   public final long endPos;

   public SectionOfFile(long var1, long var3) {
      this.startPos = ArgCheck.checkNotNegative(var1);
      this.endPos = ArgCheck.checkNotNegative(var3);
      Preconditions.checkArgument(var3 >= var1, "endPos must be >= startPos (endPos = " + var3 + ", startPos = " + var1 + ")");
   }

   public long sizeInBytes() {
      return this.endPos - this.startPos + 1L;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.startPos, this.endPos);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof SectionOfFile)) {
         return false;
      }

      SectionOfFile var2 = (SectionOfFile)var1;
      return this.startPos == var2.startPos && this.endPos == var2.endPos;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("startPos", this.startPos).add("endPos", this.endPos).toString();
   }
}
