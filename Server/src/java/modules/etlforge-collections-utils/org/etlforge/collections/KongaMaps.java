package org.etlforge.collections;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class KongaMaps {
   public static <K, V> HashMap<K, V> toHashMap(Collection<? extends V> var0, Function<? super V, K> var1) {
      HashMap var2 = new HashMap(capacity(var0.size()));
      populateMap(var2, var0, var1);
      return var2;
   }

   public static <K, V> LinkedHashMap<K, V> toLinkedHashMap(Collection<? extends V> var0, Function<? super V, K> var1) {
      LinkedHashMap var2 = new LinkedHashMap(capacity(var0.size()));
      populateMap(var2, var0, var1);
      return var2;
   }

   public static <K, V> void populateMap(Map<K, V> var0, Collection<? extends V> var1, Function<? super V, K> var2) {
      for (V var4 : var1) {
         var0.put(var2.apply(var4), var4);
      }
   }

   private static int capacity(int var0) {
      return Math.max(var0 * 2, 16);
   }

   public static <K, V> Optional<V> findValue(Map<K, V> var0, Predicate<? super K> var1) {
      for (Entry<K,V> var3 : var0.entrySet()) {
         if (var1.apply(var3.getKey())) {
            return Optional.of((V)var3.getValue());
         }
      }

      return Optional.absent();
   }

   public static <K, V> Optional<K> findKey(Map<K, V> var0, Predicate<? super V> var1) {
      for (Entry<K,V> var3 : var0.entrySet()) {
         if (var1.apply(var3.getValue())) {
            return Optional.of((K)var3.getKey());
         }
      }

      return Optional.absent();
   }

   private KongaMaps() {
   }
}
