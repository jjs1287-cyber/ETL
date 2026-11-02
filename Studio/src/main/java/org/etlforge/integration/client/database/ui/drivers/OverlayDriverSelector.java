package org.etlforge.integration.client.database.ui.drivers;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.Application;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.database.DatabaseDriverServerCache;
import org.etlforge.integration.client.database.DriverCacheRefresher;
import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.integration.client.server.ConnectionStatus;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginCallback;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.ui.login.LoginServiceUi;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.user.User;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public final class OverlayDriverSelector implements DriverSelector {
   private final ServerManager serverManager;
   private final DatabaseDriverServerCache driverCache;
   private final OverlayContainer container;
   private DriverSelectionService service;
   private DriverSelectorView view;
   private OverlayDecorator ui;
   private JComponent focusReceiverWhenClosed;

   public OverlayDriverSelector(ServerManager var1, DatabaseDriverServerCache var2, OverlayContainer var3) {
      this.serverManager = var1;
      this.driverCache = var2;
      this.container = var3;
   }

   public void setFocusReceiverWhenClosed(JComponent var1) {
      this.focusReceiverWhenClosed = var1;
   }

   @Override
   public void closeIfOpen() {
      if (this.ui != null) {
         this.ui.hide();
         this.ui = null;
      }
   }

   @Override
   public void selectDriver(Receiver<DriverDescriptor> var1) {
      if (this.driverCache.isEmpty()) {
         this.promptForDownload(var1);
      } else {
         this.openSelector(var1);
      }
   }

   private void promptForDownload(Receiver<DriverDescriptor> var1) {
      boolean var2 = UiThreadSupplier.call(new OverlayDriverSelector.Prompter(this.serverManager.getCurrentServerInfo()));
      if (var2) {
         Application.getWorker().submitForParallel(new OverlayDriverSelector.EmptyCachePopulator(var1));
      }
   }

   private void openSelector(Receiver<DriverDescriptor> var1) {
      this.createViewAndService(var1);
      this.createDecorator(var1);
      this.display();
   }

   private void createViewAndService(Receiver<DriverDescriptor> var1) {
      this.view = new DriverSelectorViewImpl();
      this.view.addListener(new OverlayDriverSelector.ViewListener());
      DriverSelectorController var2 = new DriverSelectorController(this.view, this.createDownloader());
      this.service = new DriverSelectionService(this.serverManager, this.driverCache, this.view, var2);
      this.service.setCallback(var1);
   }

   private DriverDownloader createDownloader() {
      DriverDownloaderUiJob var1 = new DriverDownloaderUiJob(this.serverManager);
      var1.setServerCache(this.driverCache);
      if (this.container instanceof WaitService) {
         var1.setWaitService((WaitService)this.container);
      }

      return var1;
   }

   private void createDecorator(Receiver<DriverDescriptor> var1) {
      JPanel var2 = BorderLayoutBuilder.fillWith(this.view);
      var2.setBorder(Empty.border(10));
      this.ui = new OverlayDriverSelector.DecoratorImpl(var2, var1);
      this.ui.setFocusReceiverWhenClosed(this.focusReceiverWhenClosed);
   }

   private void display() {
      this.ui.show(this.container, new OverlayCenterPositioner());
      this.updateOkEnabled(this.view.getSelectedDriver());
      this.view.requestFocus();
   }

   private void updateOkEnabled(DriverDescriptor var1) {
      ActionUtils.setEnabledSafely(this.ui.getOkAction(), var1 != null);
   }

   private final class DecoratorImpl extends OverlayDecorator {
      private final Receiver<DriverDescriptor> callback;

      public DecoratorImpl(JComponent var2, Receiver<DriverDescriptor> var3) {
         super(var2, "Select Driver");
         this.callback = var3;
         this.setBlocking(true);
         this.useOkAndCancel();
      }

      @Override
      protected void onOk() {
         DriverDescriptor var1 = OverlayDriverSelector.this.view.getSelectedDriver();
         if (var1 != null) {
            this.callback.handle(var1);
         }

         super.onOk();
      }

      @Override
      protected void onHidden() {
         super.onHidden();
         OverlayDriverSelector.this.service.dispose();
         OverlayDriverSelector.this.ui = null;
      }
   }

   private class EmptyCachePopulator implements ApplicationWorker.SafeRunnable {
      private final Receiver<DriverDescriptor> callback;

      public EmptyCachePopulator(Receiver<DriverDescriptor> var2) {
         this.callback = var2;
      }

      @Override
      public void run() {
         if (this.isConnected()) {
            this.refreshFromCurrentServer();
         } else {
            this.login();
         }
      }

      private boolean isConnected() {
         return OverlayDriverSelector.this.serverManager.getConnectionStatus() == ConnectionStatus.CONNECTED;
      }

      private void refreshFromCurrentServer() {
         ServerInfo var1 = OverlayDriverSelector.this.serverManager.getCurrentServerInfo();
         DriverCacheRefresher var2 = new DriverCacheRefresher(OverlayDriverSelector.this.driverCache, var1);
         var2.start();
         this.openUi();
      }

      private void login() {
         LoginServiceUi var1 = new LoginServiceUi(OverlayDriverSelector.this.serverManager);
         var1.login(new LoginCallback.Adapter() {
            @Override
            public void loginSucceeded(ServerInfo var1, LoginCredentials var2, User var3) {
               EmptyCachePopulator.this.openUi();
            }
         });
      }

      private void openUi() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               OverlayDriverSelector.this.openSelector(EmptyCachePopulator.this.callback);
            }
         });
      }
   }

   private static class Prompter implements Supplier<Boolean> {
      private final ServerInfo currentServer;

      public Prompter(ServerInfo var1) {
         this.currentServer = var1;
      }

      public Boolean get() {
         Answer var1 = Ask.yesNoOrCancel(this.getQuestion(), DatabaseUiResources.getString("DownloadDriversPrompt.Title"));
         return var1.isYes();
      }

      private String getQuestion() {
         return this.currentServer == null
            ? DatabaseUiResources.getString("DownloadDriversPrompt.NotConnected")
            : DatabaseUiResources.formatMessage("DownloadDriversPrompt.Connected", this.currentServer.getName());
      }
   }

   private class ViewListener extends DriverSelectorViewListener.Adapter {
      private ViewListener() {
      }

      @Override
      public void driverSelectionChanged(DriverDescriptor var1) {
         if (OverlayDriverSelector.this.ui != null) {
            OverlayDriverSelector.this.updateOkEnabled(var1);
         }
      }

      @Override
      public void finalSelectionWasMade(DriverDescriptor var1) {
         OverlayDriverSelector.this.ui.hide();
      }
   }
}
