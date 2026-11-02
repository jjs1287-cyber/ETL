package org.etlforge.integration.client.wsdl.cache;

import java.io.File;
import org.apache.commons.codec.binary.Hex;
import org.etlforge.application.cache.AbstractTextFileCache;
import org.etlforge.application.cache.DataCache;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.cache.TextFileCache;

public class WsdlFileCache implements DataCache {
   public static final String CACHE_ID = "WsdlFileCache";
   private final TextFileCache cacheImpl;

   public WsdlFileCache(File var1) {
      this.cacheImpl = new AbstractTextFileCache("WsdlFileCache", var1, true) {
         @Override
         protected String getFileName(Object var1) {
            return var1 + ".wsdl";
         }
      };
   }

   @Override
   public String getID() {
      return this.cacheImpl.getID();
   }

   public synchronized void store(String var1, byte[] var2) throws DataCacheException {
      this.cacheImpl.store(translateLocator(var1), var2);
   }

   public synchronized boolean contains(String var1) {
      return this.cacheImpl.contains(translateLocator(var1));
   }

   public synchronized byte[] retrieve(String var1) throws DataCacheException {
      return this.cacheImpl.retrieveAsBytes(translateLocator(var1));
   }

   private static String translateLocator(String var0) {
      return new String(Hex.encodeHex(var0.getBytes()));
   }
}
