package org.etlforge.integration.server.accesscontrol;

import com.google.common.collect.Maps;
import java.util.Map;

abstract class AbstractCachedLookup<K, V> {
   private final Object lock;
   private final Map<K, V> cache = Maps.newHashMap();

   protected AbstractCachedLookup() {
      this.lock = new Object();
   }

   protected final V lookup(K var1) {
      synchronized (this.lock) {
         Object var3 = this.cache.get(var1);
         if (var3 == null) {
            var3 = this.produceNewValue((K)var1);
            this.cache.put((K)var1, (V)var3);
         }

         return (V)var3;
      }
   }

   public void reset() {
      synchronized (this.lock) {
         this.cache.clear();
      }
   }

   protected abstract V produceNewValue(K var1);
}
