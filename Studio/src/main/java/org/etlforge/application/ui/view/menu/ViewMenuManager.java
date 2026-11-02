package org.etlforge.application.ui.view.menu;

import org.etlforge.application.ui.menu.MenuManager;

public interface ViewMenuManager extends MenuManager {
   void addTopLevelExtensionsToWindow();

   void removeTopLevelExtensionsFromWindow();

   void addMenuLevelExtensionsToWindow();

   void removeMenuLevelExtensionsFromWindow();
}
