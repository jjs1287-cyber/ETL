package org.etlforge.integration.client.server.cache;

import com.google.common.collect.ImmutableSet;
import org.etlforge.application.cache.DataCache;
import org.etlforge.integration.common.server.ServerGuid;

public interface ServerCache<T extends DataCache> extends DataCache {
   @Override
   String getID();

   T getCache(ServerGuid var1);

   ImmutableSet<T> getAll();

   boolean isEmpty();

   void addCacheListener(ServerCacheListener<T> var1);

   void removeCacheListener(ServerCacheListener<T> var1);
}
