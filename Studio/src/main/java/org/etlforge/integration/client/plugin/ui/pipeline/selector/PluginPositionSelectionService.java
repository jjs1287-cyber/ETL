package org.etlforge.integration.client.plugin.ui.pipeline.selector;

import com.google.common.collect.Sets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Set;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.plugin.pipeline.CacheBasedDisplayNameLookup;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCacheListener;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestServerCache;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.cache.ServerCacheListener;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;

public final class PluginPositionSelectionService implements Disposable {
   private final EntityType entityType;
   private final ServerManager serverManager;
   private final PluginPositionSelectionService.ServerListener serverListener;
   private final PluginPositionSelectionService.CacheListener cacheListener;
   private final PipelinePluginManifestServerCache serverCache;
   private final Set<PipelinePluginManifestCache> cachesListenedTo = Sets.newHashSet();
   private final PluginPositionSelectorModel model;
   private final BackgroundThread workThread = new BackgroundThread();
   private Receiver<List<SelectedPosition>> selectionCallback = NoReceiver.create();
   private final Object lock = new Object();

   public PluginPositionSelectionService(EntityType var1, ServerManager var2, PipelinePluginManifestServerCache var3) {
      this.entityType = var1;
      this.serverManager = var2;
      this.serverCache = var3;
      this.cacheListener = new PluginPositionSelectionService.CacheListener();
      this.model = this.createModel();
      this.serverListener = new PluginPositionSelectionService.ServerListener();
      this.addListeners();
      this.model.setSelectedServer(var2.getCurrentServerInfo());
   }

   private PluginPositionSelectorModel createModel() {
      PluginPositionSelectorModel var1 = new PluginPositionSelectorModel();
      var1.setKnownServers(this.serverManager.getRegisteredServers());
      return var1;
   }

   private void addListeners() {
      this.model.addPropertyChangeListener("selectedServer", new PluginPositionSelectionService.SelectedServerListener());
      this.serverCache.addCacheListener(this.cacheListener);
      this.serverManager.addServerConnectionListener(this.serverListener);
   }

   public void setSelectionCallback(Receiver<List<SelectedPosition>> var1) {
      this.selectionCallback = var1;
   }

   @Override
   public void dispose() {
      this.workThread.shutdown();
      this.serverManager.removeServerConnectionListener(this.serverListener);
      this.serverCache.removeCacheListener(this.cacheListener);
      synchronized (this.lock) {
         for (PipelinePluginManifestCache var3 : this.cachesListenedTo) {
            var3.removeCacheListener(this.cacheListener);
         }
      }
   }

   public PluginPositionSelectorModel getModel() {
      return this.model;
   }

   public PluginPositionSelectorController getController() {
      Receiver var1 = new Receiver<List<SelectedPosition>>() {
         public void handle(List<SelectedPosition> var1) {
            PluginPositionSelectionService.this.selectionCallback.handle(var1);
         }
      };
      return new ControllerImpl(this.serverManager, this.serverCache, this.model, var1, this.lock);
   }

   private void updateModelWithCache(PipelinePluginManifestCache var1) {
      synchronized (this.lock) {
         try {
            this.model.setPluginPositions(var1.getPipelinePositions(this.entityType), new CacheBasedDisplayNameLookup(var1));
         } catch (DataCacheException var5) {
            ErrorLog.log(PluginPositionSelectionService.class, "Failed to get cached plugin information.", var5);
            var5.printStackTrace();
            this.model.removeAllPluginPositions();
         }
      }
   }

   private void switchToCache(ServerGuid var1, PipelinePluginManifestCache var2) {
      this.model.addCache(var1, var2);
      this.listenToCache(var2);
      this.updateModelWithCache(var2);
   }

   private void listenToCache(PipelinePluginManifestCache var1) {
      if (this.cachesListenedTo.add(var1)) {
         var1.addCacheListener(this.cacheListener);
      }
   }

   private class CacheListener implements ServerCacheListener<PipelinePluginManifestCache>, PipelinePluginManifestCacheListener {
      private CacheListener() {
      }

      public void cacheAdded(ServerGuid var1, PipelinePluginManifestCache var2) {
         synchronized (PluginPositionSelectionService.this.lock) {
            if (this.isCurrentServer(var1)) {
               PluginPositionSelectionService.this.switchToCache(var1, var2);
            }
         }
      }

      private boolean isCurrentServer(ServerGuid var1) {
         ServerInfo var2 = PluginPositionSelectionService.this.model.getSelectedServer();
         return var2 != null && var2.getGuid().equals(var1);
      }

      @Override
      public void cacheWasCleared(PipelinePluginManifestCache var1) {
         PluginPositionSelectionService.this.updateModelWithCache(var1);
      }

      @Override
      public void manifestsWereCached(PipelinePluginManifestCache var1, List<PipelinePluginManifest> var2) {
         PluginPositionSelectionService.this.updateModelWithCache(var1);
      }
   }

   private class SelectedServerListener implements PropertyChangeListener {
      private SelectedServerListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "selectedServer".equals(var1.getPropertyName());
         ServerInfo var2 = (ServerInfo)var1.getNewValue();
         if (var2 == null) {
            PluginPositionSelectionService.this.model.removeAllPluginPositions();
         } else {
            this.switchServer(var2);
         }
      }

      private void switchServer(final ServerInfo var1) {
         PluginPositionSelectionService.this.workThread.execute(new Runnable() {
            @Override
            public void run() {
               synchronized (PluginPositionSelectionService.this.lock) {
                  PipelinePluginManifestCache var2 = PluginPositionSelectionService.this.serverCache.getCache(var1.getGuid());
                  if (var2 == null) {
                     PluginPositionSelectionService.this.model.removeAllPluginPositions();
                  } else {
                     PluginPositionSelectionService.this.switchToCache(var1.getGuid(), var2);
                  }
               }
            }
         });
      }
   }

   private class ServerListener implements ServerConnectionListener {
      private ServerListener() {
      }

      @Override
      public void serverConnectionChanged(ServerConnectionEvent var1) {
         PluginPositionSelectionService.this.model.setKnownServers(PluginPositionSelectionService.this.serverManager.getRegisteredServers());
         ServerInfo var2 = PluginPositionSelectionService.this.serverManager.getCurrentServerInfo();
         if (var2 != null) {
            PluginPositionSelectionService.this.model.setSelectedServer(var2);
         }
      }
   }
}
