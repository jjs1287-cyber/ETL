package org.etlforge.integration.client.plugin.ui.pipeline.selector;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginCacheUpdateCall;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestServerCache;
import org.etlforge.integration.client.plugin.serverapi.PipelinePluginInfoProvider;
import org.etlforge.integration.client.plugin.ui.resources.PluginUiResources;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCallback;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.ui.login.LoginServiceUi;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.integration.user.User;
import org.etlforge.system.callback.Receiver;

final class ControllerImpl implements PluginPositionSelectorController {
   private final ServerManager serverManager;
   private final PipelinePluginManifestServerCache serverCache;
   private final PluginPositionSelectorModel model;
   private final Receiver<List<SelectedPosition>> selectionCallback;
   private final Object lock;

   public ControllerImpl(
      ServerManager var1, PipelinePluginManifestServerCache var2, PluginPositionSelectorModel var3, Receiver<List<SelectedPosition>> var4, Object var5
   ) {
      this.serverManager = var1;
      this.serverCache = var2;
      this.model = var3;
      this.selectionCallback = var4;
      this.lock = var5;
   }

   @Override
   public void refreshListOfPlugins() {
      ControllerImpl.Refresher var1 = new ControllerImpl.Refresher();
      var1.start();
   }

   @Override
   public void selectedPositions(List<PipelinePosition> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (PipelinePosition var4 : var1) {
         PipelinePluginManifestCache var5 = this.model.getManifestCache(var4.getPluginId());
         if (var5 == null) {
            throw new RuntimeException("Could not find the manifest cache for the plugin " + var4.getPluginId());
         }

         var2.add(new SelectedPosition(var4, var5));
      }

      this.selectionCallback.handle(var2);
   }

   private class Refresher {
      private final ServerInfo server = ControllerImpl.this.model.getSelectedServer();

      private Refresher() {
      }

      public void start() {
         if (this.server != null) {
            ControllerImpl.this.model.setBusy(true);
            this.doRefresh();
         }
      }

      private void doRefresh() {
         synchronized (ControllerImpl.this.lock) {
            if (this.isCurrentServer()) {
               this.refreshFromCurrentServer();
            } else {
               this.refreshFromNewServer();
            }
         }
      }

      private boolean isCurrentServer() {
         ServerGuid var1 = ControllerImpl.this.serverManager.getCurrentServerGuid();
         return this.server.getGuid().equals(var1);
      }

      private void refreshFromCurrentServer() {
         final PipelinePluginCacheUpdateCall var1 = this.createUpdateCall();
         ApplicationWorker var2 = Application.getWorker();
         var2.submitForParallel(ApplicationWorker.GUARD.protect(new Runnable() {
            @Override
            public void run() {
               try {
                  var1.call();
               } finally {
                  ControllerImpl.this.model.setBusy(false);
               }
            }
         }));
      }

      private PipelinePluginCacheUpdateCall createUpdateCall() {
         IntegrationServer var1 = IntegrationServer.getInstance();
         PipelinePluginInfoProvider var2 = var1.getServerCall(PipelinePluginInfoProvider.class);
         LoggingExceptionHandler var3 = new LoggingExceptionHandler(
            ControllerImpl.class, ErrorLevel.ATTENTION, PluginUiResources.getString("PipelinePlugin.Error.UpdateAvailablePlugins")
         );
         PipelinePluginCacheUpdateCall var4 = new PipelinePluginCacheUpdateCall(ControllerImpl.this.serverCache, var2);
         var4.setErrorHandler(var3);
         return var4;
      }

      private void refreshFromNewServer() {
         ControllerImpl.this.serverManager.logOff();
         ControllerImpl.this.serverManager.getLoginService().setNextServerToLoginTo(this.server.getName());
         LoginServiceUi var1 = new LoginServiceUi(ControllerImpl.this.serverManager);
         var1.login(new LoginCallback() {
            @Override
            public void loginSucceeded(ServerInfo var1, LoginCredentials var2, User var3) {
               ControllerImpl.this.model.setBusy(false);
            }

            @Override
            public void invalidCredentials(InvalidCredentialsException var1) {
               ControllerImpl.this.model.setBusy(false);
            }

            @Override
            public void caught(Throwable var1) {
               ControllerImpl.this.model.setBusy(false);
            }

            @Override
            public void cancelled() {
               ControllerImpl.this.model.setBusy(false);
            }
         });
      }
   }
}
