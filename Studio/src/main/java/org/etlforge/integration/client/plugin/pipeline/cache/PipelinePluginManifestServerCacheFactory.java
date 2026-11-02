package org.etlforge.integration.client.plugin.pipeline.cache;

import com.google.common.collect.ImmutableSet;
import java.util.List;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.cache.DataCacheFactory;
import org.etlforge.integration.client.plugin.serverapi.PipelinePluginInfoProvider;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerDirectory;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.cache.DefaultServerCache;
import org.etlforge.integration.client.server.cache.ServerCacheListener;
import org.etlforge.integration.client.server.login.LoginService;
import org.etlforge.integration.client.server.login.event.LoginEvent;
import org.etlforge.integration.client.server.login.event.LoginListener;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.io.Folder;

public final class PipelinePluginManifestServerCacheFactory implements DataCacheFactory<PipelinePluginManifestServerCache> {
   private final PipelinePluginManifestServerCache cache;
   private final PipelinePluginInfoProvider serverApi;

   public PipelinePluginManifestServerCacheFactory(ServerDirectory var1, LoginService var2, PipelinePluginInfoProvider var3) {
      this.cache = new PipelinePluginManifestServerCacheFactory.CacheImpl(var1);
      this.serverApi = var3;
      this.registerPostLoginJob(var2);
   }

   private void registerPostLoginJob(LoginService var1) {
      var1.addLoginListener(
         new LoginListener.Adapter() {
            @Override
            public void postLogin(LoginEvent var1) {
               PipelinePluginCacheUpdateCall var2 = new PipelinePluginCacheUpdateCall(
                  PipelinePluginManifestServerCacheFactory.this.cache, PipelinePluginManifestServerCacheFactory.this.serverApi
               );
               var2.call();
            }
         }
      );
   }

   @Override
   public Class<PipelinePluginManifestServerCache> getCacheType() {
      return PipelinePluginManifestServerCache.class;
   }

   public PipelinePluginManifestServerCache get() {
      return this.cache;
   }

   private static class CacheImpl implements PipelinePluginManifestServerCache {
      private final ServerDirectory serverDirectory;
      private final DefaultServerCache<PipelinePluginManifestCache> delegate;

      public CacheImpl(ServerDirectory var1) {
         this.serverDirectory = var1;
         this.delegate = new DefaultServerCache<>("PipelinePluginManifestCache");
         this.init();
      }

      private void init() {
         for (KnownServer var3 : this.serverDirectory.getInformationStore().getRegisteredServers()) {
            this.initializeCacheForServer(var3);
         }
      }

      private void initializeCacheForServer(KnownServer var1) {
         try {
            ServerGuid var2 = var1.getServerInfo().getGuid();
            PipelinePluginManifestCache var3 = this.createCache(var2);
            this.addCache(var2, var3);
         } catch (IllegalArgumentException var4) {
         }
      }

      private synchronized void addCache(ServerGuid var1, PipelinePluginManifestCache var2) {
         this.delegate.addCache(var1, var2);
      }

      @Override
      public synchronized ImmutableSet<PipelinePluginManifestCache> getAll() {
         return this.delegate.getAll();
      }

      @Override
      public synchronized PipelinePluginManifestCache getCache(ServerGuid var1) {
         PipelinePluginManifestCache var2 = this.delegate.getCache(var1);
         if (var2 == null) {
            var2 = this.createCache(var1);
            this.addCache(var1, var2);
         }

         return var2;
      }

      private PipelinePluginManifestCache createCache(ServerGuid var1) {
         Folder var2 = this.getServerDirectory(var1);
         Folder var3 = var2.subFolder(this.getID());
         var3.createOnDisk();
         PipelinePluginManifestCacheFactory var4 = PipelinePluginManifestCacheFactory.newFileBasedCacheFactory(var3);
         return var4.createCache();
      }

      private Folder getServerDirectory(ServerGuid var1) {
         Folder var2 = new Folder(this.serverDirectory.getLocation(var1));
         var2.createOnDisk();
         return var2;
      }

      @Override
      public synchronized String getID() {
         return "PipelinePluginManifestCache";
      }

      @Override
      public synchronized boolean isEmpty() {
         return this.delegate.isEmpty();
      }

      @Override
      public synchronized void addManifests(ServerInfo var1, List<PipelinePluginManifest> var2) throws DataCacheException {
         ServerGuid var3 = var1.getGuid();
         PipelinePluginManifestCache var4 = this.getCache(var3);
         var4.add(var2);
      }

      @Override
      public void addCacheListener(ServerCacheListener<PipelinePluginManifestCache> var1) {
         this.delegate.addCacheListener(var1);
      }

      @Override
      public void removeCacheListener(ServerCacheListener<PipelinePluginManifestCache> var1) {
         this.delegate.removeCacheListener(var1);
      }
   }
}
