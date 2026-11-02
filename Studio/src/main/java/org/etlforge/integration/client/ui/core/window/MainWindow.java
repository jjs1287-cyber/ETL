package org.etlforge.integration.client.ui.core.window;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.action.delegator.ActionDelegator;
import org.etlforge.application.ui.menu.MenuExtension;
import org.etlforge.application.ui.menu.MenuManager;
import org.etlforge.application.ui.view.View;
import org.etlforge.application.ui.view.ViewManager;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.AddContentCallback;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.DefaultActiveObjectService;
import org.etlforge.application.ui.window.WindowFileDropHandler;
import org.etlforge.application.ui.window.WindowKeyDispatcher;
import org.etlforge.application.ui.window.WindowMenuController;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.application.ui.window.WindowSectionContentUpdater;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.service.DefaultEditorService;
import org.etlforge.application.ui.window.toolbar.ToolBar;
import org.etlforge.application.ui.window.toolbar.ToolBarExtensionPoint;
import org.etlforge.application.ui.window.toolbar.ToolBarManager;
import org.etlforge.cli.ui.CommandConsole;
import org.etlforge.integration.client.ui.core.window.toolbar.ActionsToolBarExtension;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.event.ApplicationMessageListener;
import org.etlforge.ui.event.ApplicationMessageListeners;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitLock;

public final class MainWindow implements ApplicationWindow {
   private final ViewManager viewManager;
   private MainWindowFrame frame;
   private final DefaultEditorService editorService;
   private MenuManager menuManager;
   private ToolBarManager toolBarManager;
   private final ActiveObjectService activeObjectService;
   private Map<ApplicationWindow.SectionPosition, WindowSection> sections;
   private WindowMenuController windowMenuController;
   private final MainWindowGlobalKeyDispatcher globalKeyDispatcher;
   private final List<WindowFileDropHandler> fileDropHandlers;
   private final ApplicationMessageListeners appMsgListeners;
   private final ActionDelegator actionDelegator;
   private final CommandConsole commandConsole;

   public MainWindow(ViewManager var1, CommandConsole var2) {
      this.viewManager = var1;
      this.commandConsole = var2;
      this.fileDropHandlers = Lists.newArrayList();
      this.appMsgListeners = new ApplicationMessageListeners();
      this.actionDelegator = new ActionDelegator();
      this.editorService = new DefaultEditorService(this);
      this.activeObjectService = new DefaultActiveObjectService();
      this.activeObjectService.addActiveObjectProvider(this.editorService);
      StatusPanelErrorPublisher.installInWindow(this);
      this.globalKeyDispatcher = new MainWindowGlobalKeyDispatcher(this);
      this.globalKeyDispatcher.install();
      CloseEditorShortcuts.install(this);
   }

   void setFrame(MainWindowFrame var1) {
      assert var1 != null;
      assert this.frame == null;
      this.frame = var1;
      this.frame.installDropHandler(new MainWindow.DropHandler());
   }

   public void setToolBarManager(ToolBarManager var1) {
      this.toolBarManager = var1;
   }

   public void setMenuManager(MenuManager var1) {
      this.menuManager = var1;
   }

   public void initializeEditorService() {
      this.editorService.initializeActions();
   }

   @Override
   public JFrame getFrame() {
      return this.frame == null ? null : this.frame.getFrame();
   }

   @Override
   public void setTitleAppendix(final String var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            MainWindow.this.frame.setTitleAppendix(var1);
         }
      });
   }

   @Override
   public EditorService getEditorService() {
      return this.editorService;
   }

   @Override
   public ViewManager getViewManager() {
      return this.viewManager;
   }

   @Override
   public MenuManager getMenuManager() {
      return this.menuManager;
   }

   public void setMenuExtensions(List<MenuExtension> var1) {
      for (MenuExtension var3 : var1) {
         String var4 = var3.getMenuId();
         var3.addToMenu(this.menuManager.getMenu(var4));
      }
   }

   @Override
   public ToolBarManager getToolBarManager() {
      return this.toolBarManager;
   }

   @Override
   public ActiveObjectService getActiveObjectService() {
      return this.activeObjectService;
   }

   @Override
   public ActionDelegator getActionDelegator() {
      return this.actionDelegator;
   }

   @Override
   public void displayContent(
      WindowSectionContentProvider var1, ApplicationWindow.SectionPosition var2, WindowSectionContentUpdater var3, AddContentCallback var4
   ) {
      WindowSection var5 = this.getWindowSection(var2);
      String var6 = var1.getContentId();
      if (var5.containsContent(var6)) {
         this.switchToAlreadyDisplayedContent(var5, var6, var3, var4);
      } else {
         this.createAndDisplayNewContent(var1, var5, var4);
      }
   }

   private void switchToAlreadyDisplayedContent(WindowSection var1, String var2, WindowSectionContentUpdater var3, AddContentCallback var4) {
      try {
         WindowSectionContent var5 = var1.getContent(var2);
         if (var3 != null) {
            var3.update(var5);
         }

         var1.setActiveContent(var5);
         var1.ensureVisible();
         if (var4 != null) {
            var4.contentAdded(var1, var5);
         }
      } catch (RuntimeException var6) {
         if (var4 != null) {
            var4.failed(var6);
         }
      }
   }

   private void createAndDisplayNewContent(final WindowSectionContentProvider var1, final WindowSection var2, final AddContentCallback var3) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            try {
               WindowSectionContent var1x = var1.getContent(var2);
               var2.addContent(var1x, var3);
            } catch (RuntimeException var2x) {
               if (var3 != null) {
                  var3.failed(var2x);
               } else {
                  ErrorLog.log(MainWindow.class, "An unexpected error occurred when adding the window content " + var1.getContentId(), var2x);
               }
            }
         }
      });
   }

   @Override
   public WindowSection getWindowSection(ApplicationWindow.SectionPosition var1) {
      return this.sections.get(var1);
   }

   @Override
   public WindowSection getActiveWindowSection() {
      return null;
   }

   public void displayView(View var1) {
      this.frame.displayView(var1);
      this.windowMenuController.synchronize();
   }

   @Override
   public void postApplicationMessageEvent(ApplicationMessageEvent var1) {
      this.appMsgListeners.call().incomingMessage(var1);
   }

   public void addApplicationMessageListener(ApplicationMessageListener var1) {
      this.appMsgListeners.add(var1);
   }

   public void removeApplicationMessageListener(ApplicationMessageListener var1) {
      this.appMsgListeners.remove(var1);
   }

   @Override
   public void addGlobalKeyDispatcher(KeyStroke var1, WindowKeyDispatcher var2) {
      this.globalKeyDispatcher.addDispatcher(var1, var2);
   }

   @Override
   public void removeGlobalKeyDispatcher(KeyStroke var1, WindowKeyDispatcher var2) {
      this.globalKeyDispatcher.removeDispatcher(var1, var2);
   }

   @Override
   public WaitLock startWait() {
      return this.frame.getWaitService().startWait();
   }

   @Override
   public WaitLock startWait(int var1) {
      return this.frame.getWaitService().startWait(var1);
   }

   @Override
   public CommandConsole getCommandConsole() {
      return this.commandConsole;
   }

   public void startup() {
      this.windowMenuController = new WindowMenuController(this);
      this.windowMenuController.startup();
   }

   @Override
   public void addFileDropHandler(WindowFileDropHandler var1) {
      this.fileDropHandlers.add(var1);
   }

   @Override
   public void removeFileDropHandler(WindowFileDropHandler var1) {
      this.fileDropHandlers.remove(var1);
   }

   ApplicationWindow.SectionPosition getSectionPosition(WindowSection var1) {
      return Iterables.getFirst(Maps.filterValues(this.sections, Predicates.equalTo(var1)).keySet(), null);
   }

   public void setSections(Map<ApplicationWindow.SectionPosition, WindowSection> var1) {
      this.sections = Maps.newEnumMap(var1);
   }

   public void setLoginAction(ApplicationAction var1) {
      ToolBar var2 = this.toolBarManager.getToolBar("toolbar.Actions");
      ActionsToolBarExtension var3 = new ActionsToolBarExtension(var1);
      ToolBarExtensionPoint var4 = ToolBarExtensionPoint.EXTENSION_POINT_2;
      var2.addExtension(var4, var3.getEntries(var4));
   }

   private class DropHandler extends TransferHandler {
      private DropHandler() {
      }

      @Override
      public boolean canImport(JComponent var1, DataFlavor[] var2) {
         if (this.isFileDrop(var2)) {
            for (WindowFileDropHandler var4 : MainWindow.this.fileDropHandlers) {
               if (var4.isAvailable()) {
                  return true;
               }
            }
         }

         return false;
      }

      private boolean isFileDrop(DataFlavor[] var1) {
         for (DataFlavor var5 : var1) {
            if (var5.isFlavorJavaFileListType()) {
               return true;
            }
         }

         return false;
      }

      @Override
      public boolean importData(JComponent var1, Transferable var2) {
         try {
            if (var2.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
               List var3 = (List)var2.getTransferData(DataFlavor.javaFileListFlavor);

               for (WindowFileDropHandler var5 : MainWindow.this.fileDropHandlers) {
                  if (var5.canImport(var3) && var5.doImport(var3)) {
                     return true;
                  }
               }
            }
         } catch (UnsupportedFlavorException var6) {
            var6.printStackTrace();
         } catch (IOException var7) {
            var7.printStackTrace();
         }

         return false;
      }
   }
}
