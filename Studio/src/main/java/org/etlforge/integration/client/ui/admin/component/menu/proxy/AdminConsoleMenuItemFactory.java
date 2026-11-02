package org.etlforge.integration.client.ui.admin.component.menu.proxy;

import javax.swing.Action;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.menu.MenuDictionary;
import org.etlforge.application.ui.menu.NewResourceBasedMenuDictionary;
import org.etlforge.application.ui.view.menu.ViewMenuItemFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.admin.resources.AdminViewResources;
import org.etlforge.ui.widget.menu.KongaMenu;

public final class AdminConsoleMenuItemFactory implements ViewMenuItemFactory {
   private static final String ID = "Console";
   private static final MenuDictionary MENU_DICTIONARY = new NewResourceBasedMenuDictionary(AdminViewResources.STRINGS, AdminViewResources.ICONS, null);
   private final Iterable<Action> actions;

   public AdminConsoleMenuItemFactory(Iterable<Action> var1) {
      this.actions = var1;
   }

   @Override
   public JMenuItem createMenuItem(ApplicationWindow var1) {
      MenuDictionary var2 = MENU_DICTIONARY;
      KongaMenu var3 = new KongaMenu(var2.getName("Console"));
      var3.setMnemonic(var2.getMnemonic("Console"));

      for (Action var5 : this.actions) {
         var3.add(new DefaultProxyAction(var5, var1.getViewManager()));
      }

      return var3;
   }
}
