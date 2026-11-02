package org.etlforge.util.name;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public final class Nameables {
   public static <T extends Nameable> Function<T, String> toNameFunction() {
      return new Nameables.ToNameFunction<>();
   }

   public static <T extends Nameable> Iterable<String> toNames(Iterable<T> var0) {
      return Iterables.transform(var0, toNameFunction());
   }

   public static <T extends Nameable> HashMap<String, T> toHashMap(T... var0) {
      return toHashMap(Arrays.asList((T[])var0));
   }

   public static <T extends Nameable> HashMap<String, T> toHashMap(Iterable<? extends T> var0) {
      HashMap var1 = Maps.newHashMap();
      addToMap(var1, var0);
      return var1;
   }

   public static <T extends Nameable> ImmutableMap<String, T> toImmutableMap(Iterable<? extends T> var0) {
      return ImmutableMap.copyOf(toHashMap(var0));
   }

   public static <T extends Nameable> ImmutableMap<String, T> toImmutableSortedMap(Iterable<? extends T> var0) {
      return ImmutableMap.copyOf(toSortedMap(var0));
   }

   public static <T extends Nameable> void addToMap(Map<String, T> var0, Iterable<? extends T> var1) {
      for (T var3 : var1) {
         var0.put(var3.getName(), var3);
      }
   }

   public static <T extends Nameable> SortedMap<String, T> toSortedMap(Iterable<? extends T> var0) {
      TreeMap var1 = Maps.newTreeMap();
      addToMap(var1, var0);
      return var1;
   }

   public static <T extends Nameable> LinkedHashMap<String, T> toLinkedHashMap(T... var0) {
      return toLinkedHashMap(Arrays.asList((T[])var0));
   }

   public static <T extends Nameable> LinkedHashMap<String, T> toLinkedHashMap(Iterable<? extends T> var0) {
      LinkedHashMap var1 = Maps.newLinkedHashMap();
      addToMap(var1, var0);
      return var1;
   }

   public static List<String> getNames(Iterable<? extends Nameable> var0) {
      ArrayList var1 = Lists.newArrayList();
      addNames(var1, var0);
      return var1;
   }

   public static void addNames(Collection<String> var0, Iterable<? extends Nameable> var1) {
      for (Nameable var3 : var1) {
         var0.add(var3.getName());
      }
   }

   public static <T extends Nameable> void addNames(Collection<String> var0, T[] var1) {
      for (Nameable var5 : var1) {
         var0.add(var5.getName());
      }
   }

   public static Set<String> getUniqueNames(Iterable<? extends Nameable> var0) {
      HashSet var1 = Sets.newHashSet();
      addNames(var1, var0);
      return var1;
   }

   public static <T extends Nameable> Set<String> getUniqueNames(T[] var0) {
      return getUniqueNames(Arrays.asList(var0));
   }

   private Nameables() {
   }

   private static class ToNameFunction<T extends Nameable> implements Function<T, String>, Serializable {
      private ToNameFunction() {
      }

      public String apply(T var1) {
         return var1.getName();
      }
   }
}

