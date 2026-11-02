package org.etlforge.collections;

import com.google.common.base.Objects;
import java.util.Comparator;
import java.util.Map.Entry;

public final class Pair<K, V> {
   public final K first;
   public final V second;

   public static <E, F> Pair<E, F> of(E var0, F var1) {
      return (Pair<E, F>)(new Pair<>(var0, var1));
   }

   public static <E, F> Pair<E, F> of(Entry<E, F> var0) {
      return (Pair<E, F>)(new Pair<>(var0.getKey(), var0.getValue()));
   }

   public Pair(K var1, V var2) {
      this.first = (K)var1;
      this.second = (V)var2;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof Pair)) {
         return false;
      }

      Pair var2 = (Pair)var1;
      return Objects.equal(this.first, var2.first) && Objects.equal(this.second, var2.second);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + (this.first == null ? 0 : this.first.hashCode());
      return 31 * var1 + (this.second == null ? 0 : this.second.hashCode());
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("[first: ");
      var1.append(this.first);
      var1.append("] [second: ");
      var1.append(this.second);
      var1.append("]");
      return var1.toString();
   }

   public static <T extends Comparable<T>, V> Comparator<Pair<T, V>> compareFirst() {
      return new Comparator<Pair<T, V>>() {
         public int compare(Pair<T, V> var1, Pair<T, V> var2) {
            return ((Comparable)var1.first).compareTo(var2.first);
         }
      };
   }

   public static <T, V extends Comparable<V>> Comparator<Pair<T, V>> compareSecond() {
      return new Comparator<Pair<T, V>>() {
         public int compare(Pair<T, V> var1, Pair<T, V> var2) {
            return var1.second.compareTo(var2.second);
         }
      };
   }
}
