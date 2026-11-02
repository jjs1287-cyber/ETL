package com.etlforge.integration.client.ui.project.download.console;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.admin.project.ProjectDownloader;
import com.etlforge.integration.client.admin.project.ProjectDownloaderCallback;
import com.etlforge.integration.client.ui.project.download.DownloadProjectJob;
import com.etlforge.integration.client.ui.project.download.ProjectDownloadParams;
import java.awt.EventQueue;
import java.util.List;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.info.ProjectInfoProvider;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCallback;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.ui.login.LoginServiceUi;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleModel;
import org.etlforge.integration.client.ui.project.info.ProjectsInfoDownloader;
import org.etlforge.integration.user.User;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public final class DownloadProjectController implements Disposable {
   private final ProjectConsoleModel model;
   private final ServerManager serverManager;
   private final DownloadProjectController.ConnectionListener connectionListener;
   private final ProjectInfoProvider projectInfoProvider;
   private final ProjectDownloader downloader;
   private DownloadProjectConsole console;
   private final List<ProjectDownloaderCallback> callbacks;
   private final Supplier<Boolean> condition;

   public DownloadProjectController(ProjectConsoleModel var1, ServerManager var2, ProjectInfoProvider var3, ProjectDownloader var4, Supplier<Boolean> var5) {
      this.model = var1;
      this.serverManager = var2;
      this.projectInfoProvider = var3;
      this.downloader = var4;
      this.condition = var5 == null ? Suppliers.ofInstance(Boolean.TRUE) : var5;
      this.callbacks = Lists.newArrayList();
      this.connectionListener = new DownloadProjectController.ConnectionListener();
      var2.addServerConnectionListener(this.connectionListener);
   }

   @Override
   public void dispose() {
      this.serverManager.removeServerConnectionListener(this.connectionListener);
   }

   public void setConsole(DownloadProjectConsole var1) {
      this.console = var1;
   }

   public void downloadProjectList() {
      ServerInfo var1 = this.serverManager.getCurrentServerInfo();
      this.model.setCurrentServer(var1);
      this.startDownloadProjectListJob();
   }

   private void startDownloadProjectListJob() {
      if (!this.model.isDownloadingInfo()) {
         ProjectsInfoDownloader var1 = new ProjectsInfoDownloader(this.model, this.projectInfoProvider);
         var1.submit();
      }
   }

   public void downloadProject(ProjectDownloadParams var1) {
      if (this.condition.get() && var1.isLocationOk()) {
         DownloadProjectJob var2 = new DownloadProjectJob(this.downloader, var1, new DownloadProjectController.DownloadCallback());
         var2.setUseProgressDialog(0);
         var2.submit(this.getWaitService(), 0);
         this.setDownloadEnabled(false);
      }
   }

   private WaitService getWaitService() {
      return this.console != null ? this.console.getWaitService() : WaitService.NONE;
   }

   private void setDownloadEnabled(final boolean var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (DownloadProjectController.this.console != null) {
               DownloadProjectController.this.console.setDownloadAvailable(var1);
            }
         }
      });
   }

   public void addCallback(ProjectDownloaderCallback var1) {
      this.callbacks.add(var1);
   }

   public void removeCallback(ProjectDownloaderCallback var1) {
      this.callbacks.remove(var1);
   }

   public void login() {
      if (this.console != null) {
         this.console.setSelectServerActionEnabled(false);
      }

      WaitLock var1 = this.getWaitService().startWait();
      LoginServiceUi var2 = new LoginServiceUi(this.serverManager);
      var2.login(new DownloadProjectController.LoginCallbackImpl(var1));
   }

   private final class ConnectionListener implements ServerConnectionListener {
      private ConnectionListener() {
      }

      @Override
      public void serverConnectionChanged(ServerConnectionEvent var1) {
         switch (var1.getStatus()) {
            case CONNECTED:
               DownloadProjectController.this.model.setCurrentServer(var1.getServerInfo());
               DownloadProjectController.this.startDownloadProjectListJob();
               break;
            case DISCONNECTED:
               DownloadProjectController.this.model.setCurrentServer(null);
         }
      }
   }

   private final class DownloadCallback extends ProjectDownloaderCallback.Adapter {
      private DownloadCallback() {
      }

      @Override
      public void downloadCompleted(ProjectLocation var1) {
         DownloadProjectController.this.setDownloadEnabled(true);

         for (ProjectDownloaderCallback var3 : DownloadProjectController.this.callbacks) {
            var3.downloadCompleted(var1);
         }
      }

      @Override
      public void caught(Throwable var1) {
         DownloadProjectController.this.setDownloadEnabled(true);

         for (ProjectDownloaderCallback var3 : DownloadProjectController.this.callbacks) {
            var3.caught(var1);
         }
      }

      @Override
      public void cancelled() {
         DownloadProjectController.this.setDownloadEnabled(true);

         for (ProjectDownloaderCallback var2 : DownloadProjectController.this.callbacks) {
            var2.cancelled();
         }
      }
   }

   private final class LoginCallbackImpl implements LoginCallback {
      private final WaitLock wait;

      private LoginCallbackImpl(WaitLock var2) {
         this.wait = var2;
      }

      @Override
      public void loginSucceeded(ServerInfo var1, LoginCredentials var2, User var3) {
         this.done();
      }

      @Override
      public void invalidCredentials(InvalidCredentialsException var1) {
         this.done();
      }

      @Override
      public void caught(Throwable var1) {
         this.done();
      }

      @Override
      public void cancelled() {
         this.done();
      }

      private void done() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               LoginCallbackImpl.this.wait.release();
               if (DownloadProjectController.this.console != null) {
                  DownloadProjectController.this.console.setSelectServerActionEnabled(true);
               }
            }
         });
      }
   }
}
