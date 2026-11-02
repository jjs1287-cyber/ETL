package org.etlforge.application.cache;

public interface DataCacheFactory<T extends DataCache> {
   Class<T> getCacheType();

   T get();
}
