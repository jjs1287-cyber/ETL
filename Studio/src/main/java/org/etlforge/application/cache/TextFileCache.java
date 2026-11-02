package org.etlforge.application.cache;

import java.nio.charset.Charset;

public interface TextFileCache extends DataCache {
   void store(Object var1, String var2, Charset var3) throws DataCacheException;

   boolean contains(Object var1);

   String retrieve(Object var1, Charset var2) throws DataCacheException;

   boolean delete(Object var1);

   void deleteAll();

   Object[] getCachedObjectIds();

   void store(Object var1, byte[] var2) throws DataCacheException;

   byte[] retrieveAsBytes(Object var1) throws DataCacheException;
}
