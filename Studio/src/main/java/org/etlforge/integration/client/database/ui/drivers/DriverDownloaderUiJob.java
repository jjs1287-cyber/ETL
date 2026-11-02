package org.etlforge.integration.client.database.ui.drivers;

import com.google.common.base.Preconditions;
import java.util.EnumSet;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.database.DatabaseDriverCache;
import org.etlforge.integration.client.database.DatabaseDriverServerCache;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCallback;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.ui.login.LoginServiceUi;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.database.driver.DriverType;
import org.etlforge.integration.user.User;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public final class DriverDownloaderUiJob implements DriverDownloader {
   private final ServerManager serverManager;
   private DatabaseDriverServerCache serverCache;
   private WaitService waitService = WaitService.NONE;

   public DriverDownloaderUiJob(ServerManager var1) {
      this.serverManager = Preconditions.checkNotNull(var1, "serverManager");
   }

   public void setServerCache(DatabaseDriverServerCache var1) {
      this.serverCache = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   @Override
   public void downloadDrivers(ServerInfo var1, EnumSet<DriverType> var2) {
      if (var1 != null && !this.isCurrentServer(var1)) {
         this.refreshFromNewServer(var1);
      } else {
         this.refreshDriversFromCurrentServer(var2);
      }
   }

   private boolean isCurrentServer(ServerInfo var1) {
      ServerGuid var2 = this.serverManager.getCurrentServerGuid();
      return var1.getGuid().equals(var2);
   }

   private void refreshDriversFromCurrentServer(EnumSet<DriverType> var1) {
      GetAllDriversJob var2 = new GetAllDriversJob(var1);
      var2.submit(this.waitService, 0);
      if (this.serverCache != null) {
         Application.getWorker().submitForParallel(new DriverDownloaderUiJob.CacheUpdater(this.serverCache, var2));
      }
   }

   private void refreshFromNewServer(ServerInfo var1) {
      this.serverManager.logOff();
      this.serverManager.getLoginService().setNextServerToLoginTo(var1.getName());
      WaitLock var2 = this.waitService.startWait();
      LoginServiceUi var3 = new LoginServiceUi(this.serverManager);
      DriverDownloaderUiJob.LoginMonitor var4 = new DriverDownloaderUiJob.LoginMonitor(var2);
      var3.login(var4);
   }

   private static class CacheUpdater implements ApplicationWorker.SafeRunnable {
      private final DatabaseDriverServerCache serverCache;
      private final GetAllDriversJob job;

      public CacheUpdater(DatabaseDriverServerCache var1, GetAllDriversJob var2) {
         this.serverCache = var1;
         this.job = var2;
      }

      @Override
      public void run() {
         try {
            this.job.waitUntilDone();
            this.updateCache();
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
         }
      }

      private void updateCache() {
         try {
            ServerInfo var1 = this.job.getServerInfo();
            if (var1 != null) {
               DatabaseDriverCache var2 = this.serverCache.getCache(var1.getGuid());
               if (var2 != null) {
                  var2.update(this.job.getDownloadedDrivers());
               }
            }
         } catch (DataCacheException var3) {
            ErrorLog.log(DriverDownloaderUiJob.class, "Failed to update the local client cache with downloaded driver info.", var3);
         }
      }
   }

   private static class LoginMonitor implements LoginCallback {
      private final WaitLock waitLock;

      public LoginMonitor(WaitLock var1) {
         this.waitLock = var1;
      }

      @Override
      public void loginSucceeded(ServerInfo var1, LoginCredentials var2, User var3) {
         this.waitLock.release();
      }

      @Override
      public void invalidCredentials(InvalidCredentialsException var1) {
         this.waitLock.release();
      }

      @Override
      public void caught(Throwable var1) {
         this.waitLock.release();
      }

      @Override
      public void cancelled() {
         this.waitLock.release();
      }
   }
}
