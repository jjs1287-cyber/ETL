package org.etlforge.integration.data.structure.text.validation;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.etlforge.lang.Objects;
import org.etlforge.util.csv.CsvUtils;

public final class Range implements OperatorArgument {
   private final String lower;
   private final Range.BoundType lowerType;
   private final String upper;
   private final Range.BoundType upperType;

   public Range(String var1, Range.BoundType var2, String var3, Range.BoundType var4) {
      this.lower = Preconditions.checkNotNull(var1);
      this.lowerType = Preconditions.checkNotNull(var2);
      this.upper = Preconditions.checkNotNull(var3);
      this.upperType = Preconditions.checkNotNull(var4);
   }

   @Override
   public String toStorageForm() {
      return CsvUtils.toRecord(
         ImmutableList.of(this.lowerType == Range.BoundType.CLOSED ? "[" : "]", this.lower, this.upper, this.upperType == Range.BoundType.CLOSED ? "]" : "[")
      );
   }

   public static Range fromStorageForm(String var0) {
      ImmutableList var1 = CsvUtils.toValues(var0);
      return new Range(
         (String)var1.get(1),
         ((String)var1.get(0)).equals("[") ? Range.BoundType.CLOSED : Range.BoundType.OPEN,
         (String)var1.get(2),
         ((String)var1.get(3)).equals("]") ? Range.BoundType.CLOSED : Range.BoundType.OPEN
      );
   }

   public String lowerEndpoint() {
      return this.lower;
   }

   public String upperEndpoint() {
      return this.upper;
   }

   public boolean isIncomplete() {
      return this.lower.trim().isEmpty() || this.upper.trim().isEmpty();
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.lower, this.lowerType, this.upper, this.upperType);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof Range)) {
         return false;
      }

      Range var2 = (Range)var1;
      return this.lowerType == var2.lowerType && this.upperType == var2.upperType && this.lower.equals(var2.lower) && this.upper.equals(var2.upper);
   }

   @Override
   public String toString() {
      return String.format(
         "%s%s - %s%s", this.lowerType == Range.BoundType.CLOSED ? "[" : "]", this.lower, this.upper, this.upperType == Range.BoundType.CLOSED ? "]" : "["
      );
   }

   public enum BoundType {
      CLOSED,
      OPEN;
   }
}
