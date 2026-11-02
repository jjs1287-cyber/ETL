package org.etlforge.integration.client.ui;

import com.google.common.base.Optional;
import java.awt.EventQueue;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import org.etlforge.application.Application;
import org.etlforge.application.DebugMode;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.application.ui.UncaughtExceptionLogger;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.cli.ApplicationCommands;
import org.etlforge.application.ui.help.ApplicationUiHelp;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.menu.Menu;
import org.etlforge.application.ui.menu.MenuExtension;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.view.View;
import org.etlforge.application.ui.view.ViewManagerAdapter;
import org.etlforge.application.ui.view.ViewManagerEvent;
import org.etlforge.application.ui.view.shell.ViewMenuBuilder;
import org.etlforge.application.ui.view.shell.ViewShell;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.FullScreenSupport;
import org.etlforge.application.ui.window.NoFullScreenSupport;
import org.etlforge.application.ui.window.PostAppWinPublishedJob;
import org.etlforge.application.ui.window.menu.ApplicationWindowMenuManager;
import org.etlforge.application.ui.window.toolbar.ApplicationWindowToolBarManager;
import org.etlforge.application.ui.window.toolbar.ToolBar;
import org.etlforge.cli.DefaultCommandCollection;
import org.etlforge.cli.ui.CommandConsole;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.runtime.FileInputArgumentHandler;
import org.etlforge.integration.client.runtime.StartupProgressMonitor;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.core.main.OpeningScreen;
import org.etlforge.integration.client.ui.core.window.MainWindow;
import org.etlforge.integration.client.ui.core.window.MainWindowFrame;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.event.ApplicationMessagePoster;
import org.etlforge.ui.util.StatusPanel;
import org.etlforge.ui.util.UiUtils;

public final class ApplicationWindowPublisher {
   private final MainWindow appWin;
   private final ProjectManager projectManager;
   private final OpeningScreen openingScreen;
   private final ViewManagerImpl viewManager;
   private final List<ViewShell> viewShells;
   private final StatusPanel statusPanel;
   private final CommandConsole commandConsole;
   private Map<String, Action> commandActions;
   private FileInputArgumentHandler fileInputHandler = FileInputArgumentHandler.NONE;
   private final ConnectionStatusIndicator connectionIndicator;
   private List<MenuExtension> menuExtensions;
   private List<? extends Menu> menus;
   private List<? extends ToolBar> toolbars;
   private Optional<Action> reopenLastClosedEditorAction = Optional.absent();
   private ApplicationAction loginAction;
   private volatile StartupProgressMonitor startupMonitor = StartupProgressMonitor.CONSOLE;
   private volatile FullScreenSupport fullScreenSupport = new NoFullScreenSupport();

   public ApplicationWindowPublisher(
      MainWindow var1, ProjectManager var2, OpeningScreen var3, ViewManagerImpl var4, List<ViewShell> var5, CommandConsole var6, ConnectionStatusIndicator var7
   ) {
      this.appWin = var1;
      this.projectManager = var2;
      this.openingScreen = var3;
      this.viewManager = var4;
      this.viewShells = var5;
      this.commandConsole = var6;
      this.connectionIndicator = var7;
      this.statusPanel = createStatusPanel();
   }

   private static StatusPanel createStatusPanel() {
      StatusPanel var0 = new StatusPanel();
      var0.setTextFont(TextStyles.InfoText.getFont());
      var0.setInfoIcon(CommonIcons.INFO_16);
      var0.setErrorIcon(CommonIcons.ERROR_16);
      return var0;
   }

   public void setConnectionService(ServerConnectionService var1) {
      var1.addServerConnectionListener(this.connectionIndicator);
   }

   public void setFileInputHandler(FileInputArgumentHandler var1) {
      this.fileInputHandler = var1;
   }

   public void setLoginAction(ApplicationAction var1) {
      this.loginAction = var1;
   }

   public void setCommandActions(Map<String, Action> var1) {
      this.commandActions = var1;
   }

   public void setMenus(List<? extends Menu> var1) {
      this.menus = var1;
   }

   public void setMenuExtensions(List<MenuExtension> var1) {
      this.menuExtensions = var1;
   }

   public void setToolbars(List<? extends ToolBar> var1) {
      this.toolbars = var1;
   }

   public void setFullScreenSupport(FullScreenSupport var1) {
      this.fullScreenSupport = var1;
   }

   public void setReopenLastClosedEditorAction(Action var1) {
      this.reopenLastClosedEditorAction = Optional.of(var1);
   }

   public void setStartupMonitor(StartupProgressMonitor var1) {
      this.startupMonitor = var1;
   }

   public ApplicationWindow wireUpApplicationWindow() {
      this.wireUpMenus();
      this.wireupToolBars();
      this.appWin.initializeEditorService();
      if (this.loginAction != null) {
         this.appWin.setLoginAction(this.loginAction);
      }

      this.appWin.startup();
      this.viewManager.initialize(this.viewShells);
      this.wireUpCommands();
      this.buildMenus();
      this.hookInStatusPanel();
      this.createMainFrame();
      this.fullScreenSupport.installFullScreenCapabilities(this.appWin);
      this.viewManager.addViewManagerListener(new ApplicationWindowPublisher.ViewListener());
      this.installProjectLoadingTracker();
      this.installCtrlXTracker();
      ApplicationUiHelp.getHelp().enableWindowHelpKey(this.appWin.getFrame(), "welcome");
      return this.appWin;
   }

   private void wireUpMenus() {
      ApplicationWindowMenuManager var1 = new ApplicationWindowMenuManager(this.appWin);
      this.appWin.setMenuManager(var1);
      if (this.menus != null) {
         var1.setMenus(this.menus);
      }

      if (this.menuExtensions != null) {
         this.appWin.setMenuExtensions(this.menuExtensions);
      }
   }

   private void wireupToolBars() {
      ApplicationWindowToolBarManager var1 = new ApplicationWindowToolBarManager();
      this.appWin.setToolBarManager(var1);
      if (this.toolbars != null) {
         var1.setToolBars(this.toolbars);
      }
   }

   private void wireUpCommands() {
      DefaultCommandCollection var1 = new DefaultCommandCollection();
      var1.add(new ApplicationCommands(this.appWin));
      if (this.commandActions != null) {
         var1.setActions(this.commandActions);
      }

      this.commandConsole.addCommand(var1);
   }

   private void buildMenus() {
      ViewMenuBuilder var1 = new ViewMenuBuilder(this.appWin);
      var1.buildMenu(this.viewShells);
   }

   private void hookInStatusPanel() {
      this.appWin.addApplicationMessageListener(this.statusPanel);
      ApplicationMessageEvent.setMessagePoster(new ApplicationMessagePoster() {
         @Override
         public void postMessage(ApplicationMessageEvent var1) {
            ApplicationWindowPublisher.this.appWin.postApplicationMessageEvent(var1);
         }
      });
   }

   private void createMainFrame() {
      MainWindowFrame var1 = new MainWindowFrame(this.appWin, this.statusPanel, this.connectionIndicator);
      if (this.loginAction != null) {
         var1.setLoginAction(this.loginAction);
      }

      if (this.reopenLastClosedEditorAction.isPresent()) {
         var1.installKeyBinding(this.reopenLastClosedEditorAction.get());
      }
   }

   private void installProjectLoadingTracker() {
      ProjectManagerConfigurer.configure(this.projectManager, this.appWin);
   }

   private void installCtrlXTracker() {
      if (DebugMode.IS_ENABLED) {
         CtrlXTracker.install();
      }
   }

   public void publishUi(String var1) {
      if (this.fileInputHandler.isEmpty()) {
         EventQueue.invokeLater(new ApplicationWindowPublisher.UiPublisher(var1));
      } else {
         this.processInputFile();
      }
   }

   private void processInputFile() {
      new Thread(new Runnable() {
         @Override
         public void run() {
            ApplicationWindowPublisher.this.fileInputHandler.process();
         }
      }).start();
   }

   private final class UiPublisher implements Runnable {
      private final String startupView;

      public UiPublisher(String var2) {
         this.startupView = var2;
      }

      @Override
      public void run() {
         if (this.startupView != null) {
            this.openView(this.startupView);
         } else if (ApplicationWindowPublisher.this.openingScreen != null) {
            this.publishOpeningScreen();
         } else {
            FatalConfigurationError.throwError(9, ApplicationWindowPublisher.class, null, "Must provide either a startup view or an OpeningScreen.");
         }

         this.openMainWindow();
      }

      private void openView(String var1) {
         ApplicationWindowPublisher.this.startupMonitor.publishMessage("Creating project UI");
         ApplicationWindowPublisher.this.viewManager.showView(var1);
      }

      private void publishOpeningScreen() {
         ApplicationWindowPublisher.this.startupMonitor.publishMessage("Publishing the startup screen");
         ApplicationWindowPublisher.this.openingScreen.publish(ApplicationWindowPublisher.this.appWin.getFrame());
      }

      private void openMainWindow() {
         ApplicationWindowPublisher.this.startupMonitor.publishMessage("Opening the main window");
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ApplicationWindowPublisher.this.appWin.getFrame().setVisible(true);
               Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionLogger());
               UiPublisher.this.notifyPostPublishingJobs();
            }
         });
      }

      private void notifyPostPublishingJobs() {
         for (PostAppWinPublishedJob var2 : Application.getBeansOfType(PostAppWinPublishedJob.class)) {
            var2.run(ApplicationWindowPublisher.this.appWin);
         }
      }
   }

   private final class ViewListener extends ViewManagerAdapter {
      private ViewListener() {
      }

      @Override
      public void viewIsDisplayed(ViewManagerEvent var1) {
         View var2 = var1.getView();
         ApplicationWindowPublisher.this.appWin.displayView(var2);
      }

      @Override
      public void viewIsLoaded(ViewManagerEvent var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               if (ApplicationWindowPublisher.this.openingScreen != null) {
                  ApplicationWindowPublisher.this.openingScreen.remove();
               }
            }
         });
      }
   }
}
