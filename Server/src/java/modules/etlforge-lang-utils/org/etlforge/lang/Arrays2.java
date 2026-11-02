package org.etlforge.lang;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public final class Arrays2 {
   public static <F> Object[] transform(F[] var0, Function<? super F, ?> var1) {
      int var2 = var0.length;
      Object[] var3 = new Object[var2];

      for (int var4 = 0; var4 < var2; var4++) {
         var3[var4] = var1.apply(var0[var4]);
      }

      return var3;
   }

   public static <F, T> T[] transform(F[] var0, Class<T> var1, Function<? super F, ? extends T> var2) {
      int var3 = var0.length;
      Object[] var4 = (Object[])Array.newInstance(var1, var3);

      for (int var5 = 0; var5 < var3; var5++) {
         var4[var5] = var2.apply(var0[var5]);
      }

      return (T[])var4;
   }

   public static <F> String[] stringify(F[] var0) {
      return transform(var0, String.class, Functions.toStringFunction());
   }

   public static <T> String join(T[] var0, Function<? super T, String> var1, String var2) {
      String[] var3 = transform(var0, String.class, var1);
      return StringUtils.join(var3, var2);
   }

   public static <T> T[] append(T[] var0, T[] var1) {
      Object[] var2 = Arrays.copyOf(var0, var0.length + var1.length);
      System.arraycopy(var1, 0, var2, var0.length, var1.length);
      return (T[])var2;
   }

   public static <T> T[] reverse(T[] var0) {
      int var1 = 0;

      for (int var2 = var0.length - 1; var1 < var2; var2--) {
         T var3 = var0[var1];
         var0[var1] = var0[var2];
         var0[var2] = var3;
         var1++;
      }

      return (T[])var0;
   }

   public static int sum(int... var0) {
      int var1 = 0;

      for (int var2 = 0; var2 < var0.length; var2++) {
         var1 += var0[var2];
      }

      return var1;
   }

   public static <T> T[] toArray(Iterable<? extends T> var0, Class<T> var1) {
      ArrayList var2 = Lists.newArrayList(var0);
      return (T[])var2.toArray((T[])((Object[])Array.newInstance(var1, var2.size())));
   }

   public static <T> T first(T[] var0, Predicate<? super T> var1) {
      return first((T[])var0, 0, var1);
   }

   public static <T> T first(T[] var0, int var1, Predicate<? super T> var2) {
      for (int var3 = var1; var3 < var0.length; var3++) {
         if (var2.apply(var0[var3])) {
            return (T)var0[var3];
         }
      }

      return null;
   }

   public static <T> T last(T[] var0, Predicate<? super T> var1) {
      return last((T[])var0, var0.length - 1, var1);
   }

   public static <T> T last(T[] var0, int var1, Predicate<? super T> var2) {
      for (int var3 = var1; var3 >= 0; var3--) {
         if (var2.apply(var0[var3])) {
            return (T)var0[var3];
         }
      }

      return null;
   }

   public static <F, T> List<T> toList(F[] var0, Class<T> var1, Function<? super F, ? extends T> var2) {
      return Arrays.asList((T[])transform(var0, var1, var2));
   }

   public static <T> T[] of(T... var0) {
      return (T[])var0;
   }

   private Arrays2() {
   }
}
