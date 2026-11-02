package org.etlforge.integration.salesforce.wsdl;

import java.util.Arrays;

abstract class AbstractBooleanAttributes {
   protected final boolean[] values;

   protected AbstractBooleanAttributes(boolean[] var1) {
      this.values = var1;
   }

   public final int serializeToInt() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.values.length; var2++) {
         if (this.values[var2]) {
            var1 |= 1 << var2;
         }
      }

      return var1;
   }

   protected static final boolean[] deserializeArray(int var0, int var1) {
      boolean[] var2 = new boolean[var1];

      for (int var3 = 0; var3 < var1; var3++) {
         var2[var3] = (var0 & 1 << var3) != 0;
      }

      return var2;
   }

   @Override
   public final int hashCode() {
      return Arrays.hashCode(this.values);
   }

   @Override
   public abstract boolean equals(Object var1);
}
