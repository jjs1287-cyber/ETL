package org.etlforge.collections;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class KongaListUtils {
   public static <F, T> List<T> transform(Collection<F> var0, Function<? super F, ? extends T> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (F var4 : var0) {
         Object var5 = var1.apply(var4);
         var2.add(var5);
      }

      return var2;
   }

   public static <F, T> List<T> transform(F[] var0, Function<? super F, ? extends T> var1) {
      return transform(asList(var0), var1);
   }

   public static <F, T> List<T> transform(Collection<F> var0, Function<? super F, ? extends T> var1, Predicate<? super T> var2) {
      ArrayList var3 = Lists.newArrayList();

      for (F var5 : var0) {
         T var6 = var1.apply(var5);
         if (var2.apply(var6)) {
            var3.add(var6);
         }
      }

      return var3;
   }

   public static <T> List<T> add(List<T> var0, T... var1) {
      for (T var5 : var1) {
         var0.add(var5);
      }

      return var0;
   }

   public static <S, T extends S> boolean addUniqueElement(T var0, List<S> var1) {
      if (var0 != null && var1 != null && !var1.contains(var0)) {
         var1.add(var0);
         return true;
      } else {
         return false;
      }
   }

   public static <T> List<T> asList(T... var0) {
      return new ArrayList<>(Arrays.asList((T[])var0));
   }

   public static <T> List<T> asList(Iterator<T> var0) {
      ArrayList var1 = new ArrayList();

      while (var0.hasNext()) {
         var1.add(var0.next());
      }

      return var1;
   }

   public static <T> List<T> asList(Iterable<T> var0) {
      ArrayList var1 = new ArrayList();

      for (Object var3 : var0) {
         var1.add(var3);
      }

      return var1;
   }

   public static <T> List<T> filter(Iterable<T> var0, Predicate<? super T> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (T var4 : var0) {
         if (var1.apply(var4)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public static <T> List<T> copyAndAdd(List<T> var0, T... var1) {
      ArrayList var2 = Lists.newArrayList(var0);
      Collections.addAll(var2, var1);
      return var2;
   }

   public static <T> ImmutableList<T> concatenateToImmutableList(Iterable<T> var0, Iterable<T> var1) {
      return concatenateToImmutableList(var0, var1, Collections.emptyList());
   }

   public static <T> ImmutableList<T> concatenateToImmutableList(Iterable<T> var0, Iterable<T> var1, Iterable<T>... var2) {
      Builder var3 = ImmutableList.builder();
      var3.addAll(var0);
      var3.addAll(var1);

      for (Iterable var7 : var2) {
         var3.addAll(var7);
      }

      return var3.build();
   }

   private KongaListUtils() {
   }
}

