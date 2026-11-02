package org.etlforge.integration.client.database.ui.drivers;

import java.awt.EventQueue;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.etlforge.application.Application;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.database.DatabaseDriverServerCache;
import org.etlforge.integration.client.database.DriverCacheRefresher;
import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.integration.client.info.ClientInfo;
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
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;

public final class DialogDriverSelector implements DriverSelector {
   private final ServerManager serverManager;
   private final DatabaseDriverServerCache driverCache;
   private DriverSelectionService service;
   private DriverSelectorView view;
   private final WizardStyleDialog ui;
   private JComponent focusReceiverWhenClosed;

   public DialogDriverSelector(ServerManager var1, DatabaseDriverServerCache var2, JComponent var3) {
      this.serverManager = var1;
      this.driverCache = var2;
      this.ui = new WizardStyleDialog(SwingUtilities.getWindowAncestor(var3), "Select Driver");
   }

   public void setFocusReceiverWhenClosed(JComponent var1) {
      this.focusReceiverWhenClosed = var1;
   }

   @Override
   public void closeIfOpen() {
      if (this.ui != null) {
         this.ui.setVisible(false);
         this.ui.dispose();
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
      DialogDriverSelector.Prompter var2 = new DialogDriverSelector.Prompter(this.serverManager.getCurrentServerInfo());
      boolean var3 = var2.ask();
      if (var3) {
         Application.getWorker().submitForParallel(new DialogDriverSelector.EmptyCachePopulator(var1));
      }
   }

   private void openSelector(Receiver<DriverDescriptor> var1) {
      this.createViewAndService(var1);
      this.createDialogContent();
      this.display(var1);
   }

   private void createViewAndService(Receiver<DriverDescriptor> var1) {
      this.view = new DriverSelectorViewImpl();
      this.view.addListener(new DialogDriverSelector.ViewListener());
      DriverSelectorController var2 = new DriverSelectorController(this.view, this.createDownloader());
      this.service = new DriverSelectionService(this.serverManager, this.driverCache, this.view, var2);
      this.service.setCallback(var1);
   }

   private DriverDownloader createDownloader() {
      DriverDownloaderUiJob var1 = new DriverDownloaderUiJob(this.serverManager);
      var1.setServerCache(this.driverCache);
      var1.setWaitService(this.ui);
      return var1;
   }

   private void createDialogContent() {
      JPanel var1 = BorderLayoutBuilder.fillWith(this.view);
      var1.setBorder(Empty.border(5, 10, 10, 10));
      JPanel var2 = BorderLayoutBuilder.fillWith(var1);
      var2.setOpaque(true);
      UiUtils.setPanelBackgrounds(var2, Colors.NEUTRAL_BACKGROUND);
      this.ui.getDefaultLayout().center(var2);
      this.ui.pack();
      this.ui.manageLocation(this.getClass());
   }

   private void display(Receiver<DriverDescriptor> var1) {
      this.updateOkEnabled(this.view.getSelectedDriver());
      this.ui.setLocationRelativeTo(null);
      this.ui.setOkButtonAsDefault();
      this.ui.setVisible(true);
      if (this.ui.wasOkPressed()) {
         var1.handle(this.view.getSelectedDriver());
      }

      this.restoreFocus();
   }

   private void updateOkEnabled(DriverDescriptor var1) {
      this.ui.getOKButton().setEnabled(var1 != null);
   }

   private void restoreFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (DialogDriverSelector.this.focusReceiverWhenClosed != null) {
               DialogDriverSelector.this.focusReceiverWhenClosed.requestFocusInWindow();
            }
         }
      });
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
         return DialogDriverSelector.this.serverManager.getConnectionStatus() == ConnectionStatus.CONNECTED;
      }

      private void refreshFromCurrentServer() {
         ServerInfo var1 = DialogDriverSelector.this.serverManager.getCurrentServerInfo();
         DriverCacheRefresher var2 = new DriverCacheRefresher(DialogDriverSelector.this.driverCache, var1);
         var2.start();
         this.openUi();
      }

      private void login() {
         LoginServiceUi var1 = new LoginServiceUi(DialogDriverSelector.this.serverManager);
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
               DialogDriverSelector.this.openSelector(EmptyCachePopulator.this.callback);
            }
         });
      }
   }

   private static class Prompter {
      private final ServerInfo currentServer;

      public Prompter(ServerInfo var1) {
         this.currentServer = var1;
      }

      public boolean ask() {
         if (ClientInfo.isDataLoader()) {
            return true;
         }

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
         if (DialogDriverSelector.this.ui != null) {
            DialogDriverSelector.this.updateOkEnabled(var1);
         }
      }

      @Override
      public void finalSelectionWasMade(DriverDescriptor var1) {
         DialogDriverSelector.this.closeIfOpen();
      }
   }
}
