package org.etlforge.integration.salesforce;

import com.google.common.base.Objects;
import com.google.common.primitives.Longs;

public final class StartAtRowProperty {
   private static final boolean IS_ONE_BASED = true;
   public static final StartAtRowProperty DEFAULT = new StartAtRowProperty(firstRow());
   private final long value;

   public static final long firstRow() {
      return 1L;
   }

   public StartAtRowProperty(long var1) {
      this.value = Math.max(firstRow(), var1);
   }

   public long getRow() {
      return this.value;
   }

   @Override
   public int hashCode() {
      return Longs.hashCode(this.value);
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof StartAtRowProperty && this.value == ((StartAtRowProperty)var1).value;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).addValue(this.value).toString();
   }
}
