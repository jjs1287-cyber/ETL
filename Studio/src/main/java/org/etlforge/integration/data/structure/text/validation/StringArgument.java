package org.etlforge.integration.data.structure.text.validation;

import com.google.common.base.Preconditions;

public final class StringArgument implements OperatorArgument {
   private final String value;

   public static StringArgument of(String var0) {
      return new StringArgument(var0);
   }

   public StringArgument(String var1) {
      this.value = Preconditions.checkNotNull(var1);
   }

   public boolean isBlank() {
      return this.value.trim().isEmpty();
   }

   public String getValue() {
      return this.value;
   }

   @Override
   public String toStorageForm() {
      return this.value;
   }

   public static StringArgument fromStorageForm(String var0) {
      return of(var0);
   }

   @Override
   public int hashCode() {
      return this.value.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof StringArgument && this.value.equals(((StringArgument)var1).value);
   }

   @Override
   public String toString() {
      return this.value;
   }
}
