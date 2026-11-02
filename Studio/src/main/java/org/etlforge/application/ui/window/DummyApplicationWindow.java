package org.etlforge.application.ui.window;

import javax.swing.JFrame;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.action.delegator.ActionDelegator;
import org.etlforge.application.ui.menu.MenuManager;
import org.etlforge.application.ui.view.ViewManager;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.toolbar.ToolBarManager;
import org.etlforge.cli.ui.CommandConsole;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.wait.WaitLock;

public final class DummyApplicationWindow implements ApplicationWindow {
   private final JFrame frame = new JFrame();
   private ViewManager viewManager;

   @Override
   public JFrame getFrame() {
      return this.frame;
   }

   @Override
   public ActionDelegator getActionDelegator() {
      return new ActionDelegator();
   }

   @Override
   public void addFileDropHandler(WindowFileDropHandler var1) {
   }

   @Override
   public void addGlobalKeyDispatcher(KeyStroke var1, WindowKeyDispatcher var2) {
   }

   @Override
   public void displayContent(
      WindowSectionContentProvider var1, ApplicationWindow.SectionPosition var2, WindowSectionContentUpdater var3, AddContentCallback var4
   ) {
   }

   @Override
   public ActiveObjectService getActiveObjectService() {
      return null;
   }

   @Override
   public WindowSection getActiveWindowSection() {
      return null;
   }

   @Override
   public ToolBarManager getToolBarManager() {
      return null;
   }

   @Override
   public MenuManager getMenuManager() {
      return null;
   }

   @Override
   public EditorService getEditorService() {
      return null;
   }

   @Override
   public ViewManager getViewManager() {
      return this.viewManager;
   }

   public void setViewManager(ViewManager var1) {
      this.viewManager = var1;
   }

   @Override
   public WindowSection getWindowSection(ApplicationWindow.SectionPosition var1) {
      return null;
   }

   @Override
   public void postApplicationMessageEvent(ApplicationMessageEvent var1) {
   }

   @Override
   public void removeFileDropHandler(WindowFileDropHandler var1) {
   }

   @Override
   public void removeGlobalKeyDispatcher(KeyStroke var1, WindowKeyDispatcher var2) {
   }

   @Override
   public void setTitleAppendix(String var1) {
   }

   @Override
   public WaitLock startWait() {
      return new WaitLock() {
         @Override
         public void release() {
            System.out.println("WindowWaitLock released");
         }
      };
   }

   @Override
   public CommandConsole getCommandConsole() {
      return null;
   }

   @Override
   public WaitLock startWait(int var1) {
      return this.startWait();
   }
}
