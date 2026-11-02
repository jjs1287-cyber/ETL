package org.etlforge.integration.data.entity.operation.options;

import com.google.common.primitives.Booleans;

abstract class BooleanOperationOption implements OperationOption {
   private final boolean value;

   public BooleanOperationOption(boolean var1) {
      this.value = var1;
   }

   protected final boolean getValue() {
      return this.value;
   }

   @Override
   public final int hashCode() {
      return Booleans.hashCode(this.value);
   }

   @Override
   public final boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else {
         return this.getClass() == var1.getClass() ? this.value == ((BooleanOperationOption)var1).value : false;
      }
   }
}
