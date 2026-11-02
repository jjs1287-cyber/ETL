package org.etlforge.application.cache;

public interface DataCacheService {
   <T extends DataCache> void registerFactory(DataCacheFactory<T> var1);

   <T extends DataCache> T getCache(Class<T> var1);
}
