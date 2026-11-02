package com.etlforge.integration.salesforce.ui;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;

public final class NameDisplayModes implements Serializable {
   public final NameDisplayMode object;
   public final NameDisplayMode field;

   public static NameDisplayModes api() {
      return new NameDisplayModes(NameDisplayMode.API_NAME, NameDisplayMode.API_NAME);
   }

   public static NameDisplayModes of(NameDisplayMode var0, NameDisplayMode var1) {
      return new NameDisplayModes(var0, var1);
   }

   public NameDisplayModes(NameDisplayMode var1, NameDisplayMode var2) {
      this.object = Preconditions.checkNotNull(var1);
      this.field = Preconditions.checkNotNull(var2);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.object, this.field);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof NameDisplayModes)) {
         return false;
      }

      NameDisplayModes var2 = (NameDisplayModes)var1;
      return this.object == var2.object && this.field == var2.field;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("objects", this.object).add("fields", this.field).toString();
   }
}
