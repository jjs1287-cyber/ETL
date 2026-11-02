package org.etlforge.integration.data.structure.text.validation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.etlforge.util.csv.CsvUtils;

public final class StringList implements OperatorArgument {
   private final ImmutableSet<String> values;

   public static StringList empty() {
      return new StringList();
   }

   private StringList() {
      this.values = ImmutableSet.of();
   }

   public StringList(Iterable<String> var1) {
      this.values = ImmutableSet.copyOf(var1);
   }

   public boolean isEmpty() {
      return this.values.isEmpty();
   }

   public ImmutableSet<String> getValues() {
      return this.values;
   }

   @Override
   public String toStorageForm() {
      return this.values.isEmpty() ? "" : CsvUtils.toRecord(this.values);
   }

   public static StringList fromStorageForm(String var0) {
      if (var0.isEmpty()) {
         return empty();
      }

      ImmutableList var1 = CsvUtils.toValues(var0);
      return new StringList(var1);
   }

   @Override
   public int hashCode() {
      return this.values.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof StringList && this.values.equals(((StringList)var1).values);
   }

   @Override
   public String toString() {
      return this.values.toString();
   }
}
