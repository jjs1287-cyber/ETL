package org.etlforge.integration.client.entity.valuecache;

public interface CacheStorage {
   void write(Iterable<String> var1);

   Iterable<String> read();
}
