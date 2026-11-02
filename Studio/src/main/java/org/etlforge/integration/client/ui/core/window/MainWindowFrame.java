package org.etlforge.integration.client.ui.core.window;

import com.google.common.base.Preconditions;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import org.etlforge.application.Application;
import org.etlforge.application.DebugMode;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.view.View;
import org.etlforge.application.ui.widget.ApplicationToolBar;
import org.etlforge.application.ui.window.ApplicationKeyLogger;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.ErrorLogQuickLauncher;
import org.etlforge.application.ui.window.FocusedComponentInfoViewer;
import org.etlforge.application.ui.window.MainWindowProcessDisplayer;
import org.etlforge.application.ui.window.QuickLaunchAndIndicatorPanel;
import org.etlforge.application.ui.window.RunningProcessProgressBar;
import org.etlforge.application.ui.window.WindowKeyDispatcher;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.event.WindowSectionEvent;
import org.etlforge.application.ui.window.event.WindowSectionListener;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.info.ProductNames;
import org.etlforge.integration.client.ui.ConnectionStatusIndicator;
import org.etlforge.integration.client.ui.core.main.TopPanel;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.StatusPanel;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.wait.WindowWaitService;
import org.etlforge.ui.widget.window.WindowUtils;

public final class MainWindowFrame {
   private final MainWindowPreferences prefs = new MainWindowPreferences();
   private final JFrame frame = this.createFrame();
   private final MainWindow appWin;
   private final ApplicationToolBar toolBarPanel;
   private final TopPanel topPanel;
   private boolean topPanelAdded;
   private final StatusPanel statusPanel;
   private final MainWindowContentArea content;
   private boolean contentShowing;
   private final WindowWaitService waitService = new WindowWaitService(this.frame);
   private final QuickLaunchAndIndicatorPanel quickLaunchPanel;
   private final ErrorLogQuickLauncher errorLogQuickLauncher;

   public MainWindowFrame(MainWindow var1, StatusPanel var2, ConnectionStatusIndicator var3) {
      this.appWin = var1;
      this.appWin.setFrame(this);
      this.content = new MainWindowContentArea(var1);
      this.statusPanel = var2;
      this.errorLogQuickLauncher = new ErrorLogQuickLauncher(var1);
      this.quickLaunchPanel = this.createQuickLaunchPanel();
      this.toolBarPanel = new ApplicationToolBar(new JButton[0]);
      this.toolBarPanel.setToolBarOpaque(true);
      this.topPanel = new TopPanel(var3);
      var1.getWindowSection(ApplicationWindow.SectionPosition.SOUTH).addWindowSectionListener(new MainWindowFrame.ErrorLogOpenedListener());
      this.layoutComponents();
      this.setWindowSize();
      this.addListeners();
      this.addGlobalKeyDispatchers();
   }

   public JFrame getFrame() {
      return this.frame;
   }

   public void setLoginAction(Action var1) {
      this.topPanel.setLoginAction(var1);
   }

   void installDropHandler(TransferHandler var1) {
      assert this.frame != null;
      this.frame.getRootPane().setTransferHandler(var1);
   }

   private JFrame createFrame() {
      JFrame var1 = new JFrame(getWindowTitle());
      var1.setIconImages(Arrays.asList(ClientIcons.APPLICATION_16.getImage(), ClientIcons.APPLICATION_32.getImage()));
      var1.setDefaultCloseOperation(0);
      return var1;
   }

   private void addListeners() {
      this.frame.addWindowListener(new MainWindowFrame.ExitListener());
      this.frame.addWindowStateListener(new MainWindowFrame.StateListener());
      this.frame.addComponentListener(new MainWindowFrame.ComponentListenerImpl());
   }

   private void addGlobalKeyDispatchers() {
      this.addStandardGlobalKeyDispatchers();
      this.addGlobalKeyDispatchersForDebugging();
   }

   private void addStandardGlobalKeyDispatchers() {
      this.installGarbageCollectionTrigger();
      this.installFrameRevalidator();
      this.installCommandLineController();
      this.installWaitCanceling();
      this.installFocusedComponentInfoViewer();
      this.installUiHierarchyReporter();
   }

   private void installGarbageCollectionTrigger() {
      GarbageCollectionTrigger.install(this.appWin);
   }

   private void installFrameRevalidator() {
      KeyStroke var1 = ApplicationUi.requestGlobalKeyStroke(KeyUtils.getCtrlShiftDown(46), this.getClass());
      if (var1 != null) {
         this.appWin.addGlobalKeyDispatcher(var1, new MainWindowFrame.Revalidator());
      }
   }

   private void installWaitCanceling() {
      KeyStroke var1 = ApplicationUi.requestGlobalKeyStroke(KeyUtils.getCtrlShiftDown(81), this.getClass());
      if (var1 != null) {
         this.appWin.addGlobalKeyDispatcher(var1, new MainWindowFrame.WaitStateRevoker());
      }
   }

   private void installCommandLineController() {
      new CommandConsoleController(this.appWin, this).install();
   }

   private void installFocusedComponentInfoViewer() {
      FocusedComponentInfoViewer.install(this.appWin);
   }

   private void installUiHierarchyReporter() {
      UiHierarchyPrinter var1 = new UiHierarchyPrinter(this.appWin);
      var1.install();
   }

   private void addGlobalKeyDispatchersForDebugging() {
      if (DebugMode.IS_ENABLED && DebugMode.LOG_KEYSTROKES) {
         ApplicationKeyLogger.getLogger().install();
      }
   }

   public void displayView(View var1) {
      Preconditions.checkNotNull(var1, "v");
      this.publishMenus();
      this.publishToolBar();
      this.publishViewContent();
      this.quickLaunchPanel.publish();
      this.refresh();
   }

   private void refresh() {
      this.frame.invalidate();
      this.frame.validate();
      this.frame.repaint();
   }

   private void publishViewContent() {
      if (!this.contentShowing) {
         this.content.decorate(this.frame);
         this.contentShowing = true;
      }
   }

   private void publishToolBar() {
      List var1 = this.appWin.getToolBarManager().getJToolBars();
      this.toolBarPanel.setToolBars((JToolBar[]) var1.toArray(new JToolBar[var1.size()]));
      this.topPanel.setToolBar(this.toolBarPanel);
      if (!this.topPanelAdded) {
         this.addTopPanel();
      }
   }

   private void addTopPanel() {
      this.topPanel.setBorder(BorderFactory.createEtchedBorder());
      this.topPanel.addToParent(this.frame.getContentPane(), "North");
      this.topPanelAdded = true;
   }

   private QuickLaunchAndIndicatorPanel createQuickLaunchPanel() {
      QuickLaunchAndIndicatorPanel var1 = new QuickLaunchAndIndicatorPanel(this.errorLogQuickLauncher, new MainWindowProcessDisplayer(this.appWin));
      var1.setProcessIndicator(new RunningProcessProgressBar());
      return var1;
   }

   public void setTitleAppendix(String var1) {
      String var2 = getWindowTitle();
      if (var1 != null && var1.length() > 0) {
         var2 = var2 + " - ";
         var2 = var2 + var1;
      }

      this.frame.setTitle(var2);
   }

   public WaitService getWaitService() {
      return this.waitService;
   }

   private void publishMenus() {
      JMenuBar var1 = new JMenuBar();

      for (JMenu var3 : this.appWin.getMenuManager().getJMenus()) {
         var1.add(var3);
      }

      this.frame.setJMenuBar(var1);
   }

   private void layoutComponents() {
      this.frame.setLayout(new BorderLayout());
      this.layoutSouthPart();
   }

   private void layoutSouthPart() {
      final JComponent var1 = this.createSouthComponent();
      UiCanvas var2 = new UiCanvas() {
         @Override
         public void display(Component var1x) {
            var1.add(var1x);
         }
      };
      this.quickLaunchPanel.decorate(var2);
      this.frame.getContentPane().add(var1, "South");
      var1.setBackground(Colors.NEUTRAL_BACKGROUND);
   }

   private JComponent createSouthComponent() {
      this.statusPanel.setOpaque(true);
      this.statusPanel.setBackground(Colors.NEUTRAL_BACKGROUND);
      return Horizontal.box().of(this.statusPanel).glue().container();
   }

   private void setWindowSize() {
      Toolkit var1 = Toolkit.getDefaultToolkit();
      this.frame.setSize(this.getWindowDimension(var1));
      this.setPositionOnScreen();
      this.setInitialExtendedState(var1);
   }

   private Dimension getWindowDimension(Toolkit var1) {
      Dimension var2 = this.prefs.getSize();
      Dimension var3 = var1.getScreenSize();
      int var4 = Math.min(var3.width, var2.width);
      int var5 = Math.min(var3.height, var2.height);
      return new Dimension(var4, var5);
   }

   private void setPositionOnScreen() {
      Point var1 = this.prefs.getLocation();
      if (var1 != null) {
         this.frame.setLocation(var1);
         if (WindowUtils.isWindowCompletelyVisibleOnScreen(this.frame)) {
            return;
         }
      }

      this.frame.setLocationRelativeTo(null);
   }

   private void setInitialExtendedState(Toolkit var1) {
      if (var1.isFrameStateSupported(6) && this.prefs.isMaximized()) {
         this.frame.setExtendedState(6);
      }
   }

   private static String getWindowTitle() {
      String var0 = ApplicationConfiguration.getMainWindowTitle();
      if (var0 == null) {
         var0 = ProductNames.CLIENT + " " + ClientInfo.getCurrentVersion();
      }

      return var0;
   }

   public void setCommandConsoleVisible(boolean var1) {
      this.content.setCommandConsoleVisible(var1);
   }

   public boolean isCommandConsoleVisible() {
      return this.content.isCommandConsoleVisible();
   }

   public void installKeyBinding(Action var1) {
      ActionKeyBinding.install(var1, this.frame.getRootPane());
   }

   private class ComponentListenerImpl extends ComponentAdapter {
      private ComponentListenerImpl() {
      }

      @Override
      public void componentResized(ComponentEvent var1) {
         MainWindowFrame.this.prefs.setSize(MainWindowFrame.this.frame.getSize());
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               MainWindowFrame.this.frame.getRootPane().validate();
               MainWindowFrame.this.frame.getRootPane().repaint();
            }
         });
      }

      @Override
      public void componentMoved(ComponentEvent var1) {
         MainWindowFrame.this.prefs.setLocation(MainWindowFrame.this.frame.getLocation());
      }
   }

   private class ErrorLogOpenedListener extends WindowSectionListener.Adapter {
      private ErrorLogOpenedListener() {
      }

      @Override
      public void activeContentChanged(WindowSectionEvent var1) {
         this.checkIfLauncherShouldBeHidden(var1);
      }

      private void checkIfLauncherShouldBeHidden(WindowSectionEvent var1) {
         WindowSectionContent var2 = var1.getContent();
         if (this.isErrorLogView(var2) && MainWindowFrame.this.errorLogQuickLauncher != null) {
            MainWindowFrame.this.errorLogQuickLauncher.hide();
         }
      }

      private boolean isErrorLogView(WindowSectionContent var1) {
         return var1 != null && var1.getId().equals("error_log");
      }

      @Override
      public void contentRemoved(WindowSectionEvent var1) {
         this.checkIfLauncherShouldBeHidden(var1);
      }
   }

   private static class ExitListener extends WindowAdapter {
      private ExitListener() {
      }

      @Override
      public void windowClosing(WindowEvent var1) {
         Application.exit();
      }
   }

   private class Revalidator implements WindowKeyDispatcher {
      private Revalidator() {
      }

      @Override
      public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
         this.revalidate();
         return false;
      }

      @Override
      public String getId() {
         return this.getClass().getName();
      }

      private void revalidate() {
         MainWindowFrame.this.frame.getRootPane().validate();
         MainWindowFrame.this.frame.getRootPane().repaint();
      }
   }

   private class StateListener implements WindowStateListener {
      private StateListener() {
      }

      @Override
      public void windowStateChanged(WindowEvent var1) {
         MainWindowFrame.this.prefs.setMaximized(var1.getNewState() == 6);
      }
   }

   private class WaitStateRevoker implements WindowKeyDispatcher {
      private WaitStateRevoker() {
      }

      @Override
      public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
         MainWindowFrame.this.waitService.cancelAllWaits();
         return true;
      }

      @Override
      public String getId() {
         return this.getClass().getName();
      }
   }
}
