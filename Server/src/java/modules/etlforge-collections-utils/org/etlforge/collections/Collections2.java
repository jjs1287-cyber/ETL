package org.etlforge.collections;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class Collections2 {
   public static <T> void removeFrom(Collection<T> var0, Predicate<? super T> var1) {
      Iterables.removeIf(var0, var1);
   }

   public static <T> List<T> except(Iterable<T> var0, Predicate<? super T> var1) {
      return keep(var0, Predicates.not(var1));
   }

   public static <T> List<T> keep(Iterable<T> var0, Predicate<? super T> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (T var4 : var0) {
         if (var1.apply(var4)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public static <T> void removeAll(Collection<T> var0, T... var1) {
      for (Object var5 : var1) {
         var0.remove(var5);
      }
   }

   public static <T> Iterator<T> emptyIterator() {
      return Collections.<T>emptyList().iterator();
   }

   public static <T> Iterator<T> readOnlyIterator(Collection<T> var0) {
      return Collections.<T>unmodifiableCollection(var0).iterator();
   }

   public static <T> Iterable<T> sort(Iterable<T> var0, Comparator<T> var1) {
      List var2 = KongaListUtils.asList(var0);
      Collections.sort(var2, var1);
      return var2;
   }

   public static <T> boolean equal(Set<T> var0, Set<T> var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   public static <T> Collection<T> sort(Collection<T> var0, Comparator<T> var1) {
      ArrayList var2 = new ArrayList(var0);
      Collections.sort(var2, var1);
      return var2;
   }

   public static <T, V> String join(Collection<T> var0, String var1) {
      return join(var0, Functions.toStringFunction(), var1);
   }

   public static <T, V> String join(Collection<T> var0, Function<? super T, V> var1, String var2) {
      return Joiner.on(var2).join(Iterables.transform(var0, var1));
   }

   public static <T, V> Iterable<Pair<T, V>> zip(Iterable<T> var0, Iterable<V> var1) {
      final Iterator var2 = var0.iterator();
      final Iterator var3 = var1.iterator();
      return new Iterable<Pair<T, V>>() {
         @Override
         public Iterator<Pair<T, V>> iterator() {
            return new Iterator<Pair<T, V>>() {
               @Override
               public boolean hasNext() {
                  return var2.hasNext() && var3.hasNext();
               }

               public Pair<T, V> next() {
                  return Pair.of((T)var2.next(), (V)var3.next());
               }

               @Override
               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }
      };
   }

   public static <E extends Enum<E>> EnumSet<E> enumSetOf(E[] var0, Class<E> var1) {
      EnumSet var2 = EnumSet.noneOf(var1);

      for (Enum var6 : var0) {
         var2.add((E)var6);
      }

      return var2;
   }

   private Collections2() {
   }
}
