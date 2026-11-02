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
import org.etlforge.ui.wait.WaitService;

public interface ApplicationWindow extends WaitService {
   ApplicationWindow.SectionPosition CENTER = ApplicationWindow.SectionPosition.CENTER;
   ApplicationWindow.SectionPosition WEST = ApplicationWindow.SectionPosition.WEST;
   ApplicationWindow.SectionPosition SOUTH = ApplicationWindow.SectionPosition.SOUTH;
   ApplicationWindow.SectionPosition EAST = ApplicationWindow.SectionPosition.EAST;

   JFrame getFrame();

   EditorService getEditorService();

   ViewManager getViewManager();

   MenuManager getMenuManager();

   ToolBarManager getToolBarManager();

   WindowSection getWindowSection(ApplicationWindow.SectionPosition var1);

   WindowSection getActiveWindowSection();

   ActiveObjectService getActiveObjectService();

   void setTitleAppendix(String var1);

   void postApplicationMessageEvent(ApplicationMessageEvent var1);

   void addGlobalKeyDispatcher(KeyStroke var1, WindowKeyDispatcher var2);

   void removeGlobalKeyDispatcher(KeyStroke var1, WindowKeyDispatcher var2);

   void addFileDropHandler(WindowFileDropHandler var1);

   void removeFileDropHandler(WindowFileDropHandler var1);

   void displayContent(WindowSectionContentProvider var1, ApplicationWindow.SectionPosition var2, WindowSectionContentUpdater var3, AddContentCallback var4);

   @Override
   WaitLock startWait();

   @Override
   WaitLock startWait(int var1);

   ActionDelegator getActionDelegator();

   CommandConsole getCommandConsole();

   enum SectionPosition {
      CENTER,
      WEST,
      SOUTH,
      EAST;
   }
}
