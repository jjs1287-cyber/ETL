package org.etlforge.integration.client.database;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.database.serverapi.JdbcDriverListCallback;
import org.etlforge.integration.client.database.serverapi.JdbcInfoProvider;
import org.etlforge.integration.client.database.serverapi.OdbcDriverListCallback;
import org.etlforge.integration.client.database.serverapi.OdbcInfoProvider;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.driver.OdbcDriverDescriptor;

public final class DriverCacheRefresher implements OdbcDriverListCallback, JdbcDriverListCallback {
   private final DatabaseDriverServerCache serverCache;
   private final ServerInfo serverInfo;
   private final List<DriverDescriptor> drivers = Lists.newArrayList();
   private boolean failed;

   public DriverCacheRefresher(DatabaseDriverServerCache var1, ServerInfo var2) {
      this.serverCache = Preconditions.checkNotNull(var1, "serverCache");
      this.serverInfo = Preconditions.checkNotNull(var2, "serverInfo");
   }

   public void start() {
      this.failed = false;
      this.getOdbcDrivers();
      if (!this.failed) {
         this.getJdbcDrivers();
      }

      if (!this.drivers.isEmpty()) {
         this.updateCache();
      }
   }

   @Override
   public void driversRetrieved(List<OdbcDriverDescriptor> var1, ServerInfo var2) {
      for (DriverDescriptor var4 : var1) {
         this.drivers.add(var4);
      }
   }

   @Override
   public void driversRetrieved(JdbcDriverDescriptor[] var1, ServerInfo var2) {
      for (JdbcDriverDescriptor var6 : var1) {
         this.drivers.add(var6);
      }
   }

   @Override
   public void cancelled() {
   }

   @Override
   public void caught(Throwable var1) {
      this.failed = true;
      String var2 = DatabaseMessages.getFormattedMessage("DriverCacheRefresher.AutoUpdateFailed", new Object[]{this.serverInfo.getName(), var1.getMessage()});
      ErrorLog.log(DriverCacheRefresher.class, var2, var1);
   }

   private void getOdbcDrivers() {
      IntegrationServer var1 = IntegrationServer.getInstance();
      OdbcInfoProvider var2 = var1.getServerCall(OdbcInfoProvider.class);
      var2.getDrivers(this);
   }

   private void getJdbcDrivers() {
      IntegrationServer var1 = IntegrationServer.getInstance();
      JdbcInfoProvider var2 = var1.getServerCall(JdbcInfoProvider.class);
      var2.getDrivers(this);
   }

   private void updateCache() {
      try {
         DatabaseDriverCache var1 = this.getCache(this.serverInfo);
         var1.update(this.drivers);
      } catch (DataCacheException var2) {
         this.caught(var2);
      }
   }

   private DatabaseDriverCache getCache(ServerInfo var1) throws DataCacheException {
      DatabaseDriverCache var2 = this.serverCache.getCache(var1.getGuid());
      if (var2 == null) {
         var2 = this.serverCache.createCache(var1);
      }

      return var2;
   }
}
