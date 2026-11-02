package org.etlforge.integration.client.database;

import org.etlforge.application.cache.DataCacheFactory;
import org.etlforge.integration.client.server.ServerDirectory;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.login.LoginInfo;
import org.etlforge.integration.client.server.login.LoginService;
import org.etlforge.integration.client.server.login.event.LoginEvent;
import org.etlforge.integration.client.server.login.event.LoginListener;

public final class DatabaseDriverServerCacheFactory implements DataCacheFactory<DatabaseDriverServerCache> {
   private final DatabaseDriverServerCache cache;

   public DatabaseDriverServerCacheFactory(ServerDirectory var1, LoginService var2) {
      this.cache = new DatabaseDriverServerFileCache(var1);
      this.registerPostLoginJob(var2);
   }

   private void registerPostLoginJob(LoginService var1) {
      var1.addLoginListener(new LoginListener.Adapter() {
         @Override
         public void postLogin(LoginEvent var1) {
            LoginInfo var2 = var1.getInfo();
            ServerInfo var3 = var2.getServerInfo();
            DriverCacheRefresher var4 = new DriverCacheRefresher(DatabaseDriverServerCacheFactory.this.cache, var3);
            var4.start();
         }
      });
   }

   @Override
   public Class<DatabaseDriverServerCache> getCacheType() {
      return DatabaseDriverServerCache.class;
   }

   public DatabaseDriverServerCache get() {
      return this.cache;
   }
}
