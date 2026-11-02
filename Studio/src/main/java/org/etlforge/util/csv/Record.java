package org.etlforge.util.csv;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;

public final class Record {
   private final ImmutableList<String> values;

   public static Record of(String... var0) {
      return new Record(Arrays.asList(var0));
   }

   public static Record of(Iterable<String> var0) {
      return new Record(ImmutableList.copyOf(var0));
   }

   private Record(ImmutableList<String> var1) {
      this.values = var1;
   }

   public Record(Iterable<String> var1) {
      this.values = ImmutableList.copyOf(var1);
   }

   public ImmutableList<String> getValues() {
      return this.values;
   }

   public String getValueAt(int var1) {
      return this.values.get(var1);
   }

   public String[] toArray() {
      return this.values.toArray(new String[this.values.size()]);
   }

   public Record append(String... var1) {
      if (var1.length == 0) {
         return this;
      }

      ArrayList var2 = Lists.newArrayList(this.values);
      var2.addAll(Arrays.asList(var1));
      return new Record(var2);
   }

   public boolean isEmpty() {
      return this.values.isEmpty();
   }

   public int size() {
      return this.values.size();
   }
}
