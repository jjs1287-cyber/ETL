package org.etlforge.collections;

import com.google.common.collect.Sets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public final class KongaSetUtils {
   public static <T> Set<T> asSet(Enumeration<T> var0) {
      return Sets.newHashSet(new IterableEnumeration<>(var0));
   }

   public static <T> Set<T> intersection(T[] var0, T[] var1) {
      return Sets.intersection(Sets.newHashSet((T[])var0), Sets.newHashSet(var1));
   }

   public static <T> Set<T> intersection(Iterable<T> var0, T[] var1) {
      return Sets.intersection(Sets.newHashSet(var0), Sets.newHashSet(var1));
   }

   public static <T> Set<? extends T> createAssymetricSetDifference(Set<? extends T> var0, Set<? extends T> var1) {
      HashSet var2 = Sets.newHashSet(var0);
      var2.removeAll(var1);
      return var2;
   }

   private KongaSetUtils() {
   }
}
