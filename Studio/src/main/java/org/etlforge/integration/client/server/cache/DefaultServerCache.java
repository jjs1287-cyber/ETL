package org.etlforge.integration.client.server.cache;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.etlforge.application.cache.DataCache;
import org.etlforge.integration.common.server.ServerGuid;

public final class DefaultServerCache<T extends DataCache> implements ServerCache<T> {
   private final String cacheId;
   private final Map<ServerGuid, T> caches = Maps.newHashMap();
   private final List<ServerCacheListener<T>> listeners = Lists.newCopyOnWriteArrayList();

   public DefaultServerCache(String var1) {
      this.cacheId = Preconditions.checkNotNull(var1, "cacheId");
   }

   @Override
   public String getID() {
      return this.cacheId;
   }

   @Override
   public T getCache(ServerGuid var1) {
      return this.caches.get(var1);
   }

   public T addCache(ServerGuid var1, T var2) {
      Preconditions.checkNotNull(var2, "cache");
      Preconditions.checkArgument(
         this.cacheId.equals(var2.getID()),
         "This collection only accepts server caches with the ID " + this.cacheId + ". The submitted cache has the ID " + var2.getID()
      );
      DataCache var3 = this.caches.put(var1, (T)var2);
      if (var2 != var3) {
         this.fireCacheAdded(var1, (T)var2);
      }

      return (T)var3;
   }

   private void fireCacheAdded(ServerGuid var1, T var2) {
      for (ServerCacheListener var4 : this.listeners) {
         var4.cacheAdded(var1, var2);
      }
   }

   @Override
   public ImmutableSet<T> getAll() {
      return ImmutableSet.copyOf(this.caches.values());
   }

   @Override
   public boolean isEmpty() {
      return this.caches.isEmpty();
   }

   @Override
   public void addCacheListener(ServerCacheListener<T> var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeCacheListener(ServerCacheListener<T> var1) {
      this.listeners.remove(var1);
   }
}
