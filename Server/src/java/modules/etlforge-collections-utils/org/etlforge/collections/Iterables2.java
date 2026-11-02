package org.etlforge.collections;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class Iterables2 {
   public static <T> FluentIterable<T> empty() {
      return FluentIterable.from(Collections.EMPTY_LIST);
   }

   public static <T> List<T> sortedCopy(Iterable<T> var0, Comparator<? super T> var1) {
      return sortedCopy(var0, Ordering.from(var1));
   }

   public static <T> List<T> sortedCopy(Iterable<T> var0, Ordering<? super T> var1) {
      return var1.sortedCopy(var0);
   }

   public static <T> boolean removeIfNot(Iterable<T> var0, Predicate<? super T> var1) {
      return Iterables.removeIf(var0, Predicates.not(var1));
   }

   private Iterables2() {
   }
}
