package org.etlforge.integration.ldap.structure;

import com.google.common.base.Preconditions;

public final class NumericOid {
   public static final NumericOid UNKNOWN = new NumericOid("-1");
   private final String value;

   public NumericOid(String var1) {
      String var2 = var1.trim();
      Preconditions.checkArgument(!var2.isEmpty(), "value cannot be empty");
      this.value = var2;
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof NumericOid && ((NumericOid)var1).value.equals(this.value);
   }

   @Override
   public int hashCode() {
      return this.value.hashCode();
   }

   @Override
   public String toString() {
      return this.value;
   }
}
