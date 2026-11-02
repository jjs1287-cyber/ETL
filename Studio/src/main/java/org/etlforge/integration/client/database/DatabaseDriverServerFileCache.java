package org.etlforge.integration.client.database;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerDirectory;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.cache.DefaultServerCache;
import org.etlforge.integration.client.server.cache.ServerCacheListener;
import org.etlforge.integration.common.server.ServerGuid;

public final class DatabaseDriverServerFileCache implements DatabaseDriverServerCache {
   private final ServerDirectory cacheRoot;
   private final DefaultServerCache<DatabaseDriverCache> cacheImpl = new DefaultServerCache<>(
      "org.etlforge.integration.data.location.driver.DriverDescriptorServerCache"
   );
   private final Object lock = new Object();

   public DatabaseDriverServerFileCache(ServerDirectory var1) {
      this.cacheRoot = var1;
      this.init();
   }

   private void init() {
      for (KnownServer var3 : this.cacheRoot.getInformationStore().getRegisteredServers()) {
         this.loadServer(var3);
      }
   }

   @Override
   public DatabaseDriverCache createCache(ServerInfo var1) {
      ServerGuid var2 = var1.getGuid();
      File var3 = this.cacheRoot.getLocation(var2);
      DatabaseDriverCache var4 = new DatabaseDriverCache(var2, var3);
      this.addCache(var2, var4);
      return var4;
   }

   private void addCache(ServerGuid var1, DatabaseDriverCache var2) {
      synchronized (this.lock) {
         this.cacheImpl.addCache(var1, var2);
      }
   }

   @Override
   public ImmutableSet<DatabaseDriverCache> getAll() {
      synchronized (this.lock) {
         return this.cacheImpl.getAll();
      }
   }

   public DatabaseDriverCache getCache(ServerGuid var1) {
      synchronized (this.lock) {
         return this.cacheImpl.getCache(var1);
      }
   }

   @Override
   public String getID() {
      return this.cacheImpl.getID();
   }

   @Override
   public boolean isEmpty() {
      synchronized (this.lock) {
         return this.cacheImpl.isEmpty();
      }
   }

   @Override
   public void addCacheListener(ServerCacheListener<DatabaseDriverCache> var1) {
      this.cacheImpl.addCacheListener(var1);
   }

   @Override
   public void removeCacheListener(ServerCacheListener<DatabaseDriverCache> var1) {
      this.cacheImpl.removeCacheListener(var1);
   }

   private void loadServer(KnownServer var1) {
      File var2 = this.cacheRoot.getLocation(var1.getServerInfo().getGuid());
      String var3 = var2.getName();

      try {
         ServerGuid var4 = new ServerGuid(var3);
         DatabaseDriverCache var5 = new DatabaseDriverCache(var4, var2);
         this.loadCacheForServer(var5, var4, var2);
      } catch (IllegalArgumentException var6) {
      }
   }

   private void loadCacheForServer(DatabaseDriverCache var1, ServerGuid var2, File var3) {
      try {
         var1.load();
         this.addCache(var2, var1);
      } catch (DataCacheException var6) {
         String var5 = "Failed to load the database driver cache from the server data at " + var3.getAbsolutePath() + ". Reported error: " + var6.getMessage();
         ErrorLog.log(DatabaseDriverServerFileCache.class, var5, var6);
      }
   }
}
