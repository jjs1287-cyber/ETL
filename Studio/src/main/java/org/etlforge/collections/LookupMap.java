package org.etlforge.collections;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.Map;

public class LookupMap<K, V> {
   private final Map<K, V> map = this.createMap();
   private final Function<K, V> valueProvider;

   public static <K, V> LookupMap<K, V> withSupplier(Function<K, V> var0) {
      return new LookupMap<>(var0);
   }

   protected Map<K, V> createMap() {
      return new HashMap<>();
   }

   public LookupMap(Function<K, V> var1) {
      Preconditions.checkNotNull(this.map, "createEmptyMap may not return null");
      this.valueProvider = Preconditions.checkNotNull(var1, "valueProvider");
   }

   public final V getValue(K var1) {
      Preconditions.checkNotNull(var1, "key");
      Object var2;
      if (this.map.containsKey(var1)) {
         var2 = this.map.get(var1);
         Preconditions.checkNotNull(var2, "value");
      } else {
         var2 = this.valueProvider.apply((K)var1);
         Preconditions.checkNotNull(var2, "value");
         this.map.put((K)var1, (V)var2);
      }

      return (V)var2;
   }

   public final ImmutableCollection<V> values() {
      return ImmutableList.copyOf(this.map.values());
   }
}
