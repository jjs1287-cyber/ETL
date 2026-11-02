package org.etlforge.integration.client.ui.interchange.entity.script;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginCacheUpdateCall;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCacheListener;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestServerCache;
import org.etlforge.integration.client.plugin.serverapi.PipelinePluginInfoProvider;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.ui.script.builder.plugin.ScriptPluginProvider;
import org.etlforge.integration.client.ui.script.builder.plugin.ScriptPluginProviderListener;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.plugin.pipeline.ManifestComparators;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.integration.plugin.pipeline.RelativePosition;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.system.Disposable;
import org.etlforge.ui.action.ActionUtils;

public final class ScriptPluginProviderImpl implements ScriptPluginProvider, Disposable {
   private final PipelinePluginInfoProvider pluginInfoProvider;
   private final ServerConnectionService connectionService;
   private final ScriptPluginProviderImpl.ServerListener serverListener = new ScriptPluginProviderImpl.ServerListener();
   private ServerInfo currentServer;
   private PipelinePluginManifestCache currentCache;
   private ImmutableList<PipelinePluginManifest> knownManifests;
   private final PipelinePluginManifestCacheListener cacheListener = new ScriptPluginProviderImpl.CacheListener();
   private final List<ScriptPluginProviderListener> listeners = Lists.newCopyOnWriteArrayList();
   private final Action loginAction;

   public ScriptPluginProviderImpl(PipelinePluginInfoProvider var1, ServerConnectionService var2, Action var3) {
      this.pluginInfoProvider = var1;
      this.connectionService = var2;
      this.loginAction = var3;
      var2.addServerConnectionListener(this.serverListener);
      this.setCurrentServer(var2.getCurrentServerInfo());
   }

   private void setCurrentServer(ServerInfo var1) {
      synchronized (this) {
         this.currentServer = var1;
         if (var1 == null) {
            this.setCurrentCache(null);
         } else {
            PipelinePluginManifestServerCache var3 = this.getServerCache();
            this.setCurrentCache(var3.getCache(this.currentServer.getGuid()));
         }
      }

      this.fireServerChanged(var1);
   }

   private PipelinePluginManifestServerCache getServerCache() {
      DataCacheService var1 = Application.getCacheService();
      return var1.getCache(PipelinePluginManifestServerCache.class);
   }

   private void setCurrentCache(PipelinePluginManifestCache var1) {
      this.knownManifests = null;
      this.removeCacheListener();
      this.currentCache = var1;
      if (var1 != null) {
         var1.addCacheListener(this.cacheListener);
      }
   }

   private void removeCacheListener() {
      if (this.currentCache != null) {
         this.currentCache.removeCacheListener(this.cacheListener);
      }
   }

   private void fireServerChanged(ServerInfo var1) {
      for (ScriptPluginProviderListener var3 : this.listeners) {
         var3.serverConnectionChanged(var1);
      }
   }

   @Override
   public synchronized ImmutableList<PipelinePluginManifest> getAvailablePlugins() {
      if (this.knownManifests != null) {
         return this.knownManifests;
      }

      if (this.currentCache != null) {
         try {
            this.knownManifests = this.askCacheForItsPlugins();
            return this.knownManifests;
         } catch (DataCacheException var2) {
            ErrorLog.attention(this.getClass(), null, var2);
         }
      }

      return ImmutableList.of();
   }

   private ImmutableList<PipelinePluginManifest> askCacheForItsPlugins() throws DataCacheException {
      List var1 = this.currentCache.getCachedManifests();
      List var2 = KongaListUtils.filter(var1, new ScriptPluginProviderImpl.PluginFilter());
      return Ordering.from(ManifestComparators.DISPLAY_NAME).immutableSortedCopy(var2);
   }

   @Override
   public synchronized ServerInfo getCurrentServer() {
      return this.currentServer;
   }

   @Override
   public synchronized void refresh() {
      if (this.currentServer == null) {
         ActionUtils.run(this.loginAction, this, "login");
      } else {
         ScriptPluginProviderImpl.RefreshWorker var1 = new ScriptPluginProviderImpl.RefreshWorker(this.currentServer);
         Application.getWorker().submitForParallel(var1);
      }
   }

   @Override
   public void addListener(ScriptPluginProviderListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeListener(ScriptPluginProviderListener var1) {
      this.listeners.remove(var1);
   }

   @Override
   public void dispose() {
      this.connectionService.removeServerConnectionListener(this.serverListener);
      synchronized (this) {
         this.removeCacheListener();
      }
   }

   private final class CacheListener implements PipelinePluginManifestCacheListener {
      private CacheListener() {
      }

      @Override
      public void cacheWasCleared(PipelinePluginManifestCache var1) {
         this.fireAvailablePluginsChanged();
      }

      private void fireAvailablePluginsChanged() {
         synchronized (ScriptPluginProviderImpl.this) {
            ScriptPluginProviderImpl.this.knownManifests = null;
         }

         for (ScriptPluginProviderListener var2 : ScriptPluginProviderImpl.this.listeners) {
            var2.availablePluginsChanged();
         }
      }

      @Override
      public void manifestsWereCached(PipelinePluginManifestCache var1, List<PipelinePluginManifest> var2) {
         this.fireAvailablePluginsChanged();
      }
   }

   private static final class PluginFilter implements Predicate<PipelinePluginManifest> {
      private PluginFilter() {
      }

      public boolean apply(PipelinePluginManifest var1) {
         PipelinePosition[] var2 = var1.getPipelinePositions();

         for (PipelinePosition var6 : var2) {
            if (this.isApplicable(var6)) {
               return true;
            }
         }

         return false;
      }

      private boolean isApplicable(PipelinePosition var1) {
         return var1.getEntityType() == EntityType.Script && var1.getRelativePosition() == RelativePosition.INSIDE;
      }
   }

   private final class RefreshWorker implements ApplicationWorker.SafeRunnable, ExceptionHandler<Throwable> {
      private final ServerInfo server;

      public RefreshWorker(ServerInfo var2) {
         assert var2 != null;
         this.server = var2;
      }

      @Override
      public void run() {
         if (this.isStillConnected()) {
            this.refresh();
         }
      }

      private boolean isStillConnected() {
         synchronized (ScriptPluginProviderImpl.this) {
            return this.server == ScriptPluginProviderImpl.this.currentServer;
         }
      }

      private void refresh() {
         PipelinePluginCacheUpdateCall var1 = new PipelinePluginCacheUpdateCall(
            ScriptPluginProviderImpl.this.getServerCache(), ScriptPluginProviderImpl.this.pluginInfoProvider
         );
         var1.setErrorHandler(this);
         var1.call();
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.attention(ScriptPluginProviderImpl.class, "The plugin list could not be refreshed: " + var1.getMessage(), var1);
      }
   }

   private final class ServerListener implements ServerConnectionListener {
      private ServerListener() {
      }

      @Override
      public void serverConnectionChanged(ServerConnectionEvent var1) {
         ScriptPluginProviderImpl.this.setCurrentServer(var1.getServerInfo());
      }
   }
}
