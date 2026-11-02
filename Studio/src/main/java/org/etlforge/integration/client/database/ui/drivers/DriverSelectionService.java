package org.etlforge.integration.client.database.ui.drivers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.awt.EventQueue;
import java.util.Collection;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.integration.client.database.DatabaseDriverCache;
import org.etlforge.integration.client.database.DatabaseDriverCacheListener;
import org.etlforge.integration.client.database.DatabaseDriverServerCache;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.cache.ServerCacheListener;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;

public final class DriverSelectionService implements Disposable {
   private final ServerManager serverManager;
   private final ServerConnectionListener connectionListener;
   private final DatabaseDriverServerCache driverCache;
   private final DriverSelectionService.CacheListener cacheListener;
   private final DriverSelectorView view;
   private final DriverSelectionService.ViewListener viewListener;
   private final DriverSelectorController controller;
   private Receiver<DriverDescriptor> callback;

   public DriverSelectionService(ServerManager var1, DatabaseDriverServerCache var2, DriverSelectorView var3, DriverSelectorController var4) {
      this.serverManager = var1;
      this.driverCache = var2;
      this.view = var3;
      this.controller = var4;
      this.connectionListener = new DriverSelectionService.CurrentServerListener();
      var1.addServerConnectionListener(this.connectionListener);
      this.cacheListener = new DriverSelectionService.CacheListener();
      this.viewListener = new DriverSelectionService.ViewListener();
      this.callback = NoReceiver.create();
      this.listenToCaches();
      this.configureController();
      this.configureView();
   }

   private void listenToCaches() {
      this.driverCache.addCacheListener(this.cacheListener);

      for (DatabaseDriverCache var2 : this.driverCache.getAll()) {
         var2.addCacheListener(this.cacheListener);
      }
   }

   private void configureController() {
      this.giveDriversToController();
   }

   private void giveDriversToController() {
      this.controller.setDriverCache(this.createDriverMap());
   }

   private Multimap<ServerGuid, DriverDescriptor> createDriverMap() {
      ArrayListMultimap var1 = ArrayListMultimap.create();

      for (KnownServer var3 : this.serverManager.getRegisteredServers()) {
         try {
            ServerGuid var4 = var3.getServerInfo().getGuid();
            DatabaseDriverCache var5 = this.driverCache.getCache(var4);
            if (var5 != null) {
               var1.putAll(var4, var5.getDrivers());
            }
         } catch (DataCacheException var6) {
            var6.printStackTrace();
         }
      }

      return var1;
   }

   private void configureView() {
      this.view.addListener(this.viewListener);
      this.view.setKnownServers(this.serverManager.getRegisteredServers());
      ServerInfo var1 = this.serverManager.getCurrentServerInfo();
      if (var1 != null) {
         this.view.setSelectedServer(var1);
      }
   }

   public void setCallback(Receiver<DriverDescriptor> var1) {
      this.callback = var1;
   }

   @Override
   public void dispose() {
      this.view.removeListener(this.viewListener);
      this.serverManager.removeServerConnectionListener(this.connectionListener);
      this.stopListeningToCaches();
   }

   private void stopListeningToCaches() {
      this.driverCache.removeCacheListener(this.cacheListener);

      for (DatabaseDriverCache var2 : this.driverCache.getAll()) {
         var2.removeCacheListener(this.cacheListener);
      }
   }

   private final class CacheListener implements ServerCacheListener<DatabaseDriverCache>, DatabaseDriverCacheListener {
      private CacheListener() {
      }

      public void cacheAdded(ServerGuid var1, DatabaseDriverCache var2) {
         var2.addCacheListener(this);
         DriverSelectionService.this.giveDriversToController();
      }

      @Override
      public void cacheWasUpdated(Collection<? extends DriverDescriptor> var1) {
         DriverSelectionService.this.giveDriversToController();
      }
   }

   private final class CurrentServerListener implements ServerConnectionListener {
      private CurrentServerListener() {
      }

      @Override
      public void serverConnectionChanged(ServerConnectionEvent var1) {
         ServerInfo var2 = var1.getServerInfo();
         if (var2 != null) {
            this.makeServerSelectedInView(var2);
         }
      }

      private void makeServerSelectedInView(final ServerInfo var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DriverSelectionService.this.view.setKnownServers(DriverSelectionService.this.serverManager.getRegisteredServers());
               DriverSelectionService.this.view.setSelectedServer(var1);
            }
         });
      }
   }

   private class ViewListener extends DriverSelectorViewListener.Adapter {
      private ViewListener() {
      }

      @Override
      public void finalSelectionWasMade(DriverDescriptor var1) {
         DriverSelectionService.this.callback.handle(var1);
      }
   }
}
