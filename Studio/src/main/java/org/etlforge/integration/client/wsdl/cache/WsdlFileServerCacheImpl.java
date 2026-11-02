package org.etlforge.integration.client.wsdl.cache;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.client.server.ServerDirectory;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.cache.DefaultServerCache;
import org.etlforge.integration.client.server.cache.ServerCacheListener;
import org.etlforge.integration.common.server.ServerGuid;

final class WsdlFileServerCacheImpl implements WsdlFileServerCache {
   private final ServerDirectory cacheRoot;
   private final DefaultServerCache<WsdlFileCache> fileCache = new DefaultServerCache<>("WsdlFileCache");

   public WsdlFileServerCacheImpl(ServerDirectory var1) {
      this.cacheRoot = var1;
   }

   @Override
   public String getID() {
      return "WsdlFileCache";
   }

   public synchronized WsdlFileCache getCache(ServerGuid var1) {
      return this.fileCache.getCache(var1);
   }

   @Override
   public synchronized ImmutableSet<WsdlFileCache> getAll() {
      return this.fileCache.getAll();
   }

   @Override
   public synchronized boolean isEmpty() {
      return this.fileCache.isEmpty();
   }

   @Override
   public void addCacheListener(ServerCacheListener<WsdlFileCache> var1) {
      this.fileCache.addCacheListener(var1);
   }

   @Override
   public void removeCacheListener(ServerCacheListener<WsdlFileCache> var1) {
      this.fileCache.removeCacheListener(var1);
   }

   @Override
   public synchronized void cacheWsdlFile(ServerInfo var1, String var2, byte[] var3) throws DataCacheException {
      ServerGuid var4 = var1.getGuid();
      WsdlFileCache var5 = this.getCache(var4);
      if (var5 == null) {
         var5 = this.createCache(var4);
         this.fileCache.addCache(var4, var5);
      }

      var5.store(var2, var3);
   }

   private WsdlFileCache createCache(ServerGuid var1) {
      File var2 = new File(this.getServerDirectory(var1), this.getID());
      if (!var2.exists()) {
         var2.mkdirs();
      }

      return new WsdlFileCache(var2);
   }

   private File getServerDirectory(ServerGuid var1) {
      File var2 = this.cacheRoot.getLocation(var1);
      if (!var2.exists()) {
         var2.mkdirs();
      }

      return var2;
   }
}
