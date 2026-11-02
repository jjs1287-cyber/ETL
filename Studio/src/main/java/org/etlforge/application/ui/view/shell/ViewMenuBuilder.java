package org.etlforge.application.ui.view.shell;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.menu.Menu;
import org.etlforge.application.ui.menu.MenuContribution;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.application.ui.menu.MenuManager;
import org.etlforge.application.ui.view.menu.MenuExtensionData;
import org.etlforge.application.ui.view.menu.ViewMenuItemFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class ViewMenuBuilder {
   private final ApplicationWindow appWin;
   private final Map<String, ViewMenuBuilder.MenuExtensionManager> menuExtensionManagers;

   public ViewMenuBuilder(ApplicationWindow var1) {
      this.appWin = var1;
      this.menuExtensionManagers = Maps.newHashMap();
   }

   public void buildMenu(List<ViewShell> var1) {
      MenuManager var2 = this.appWin.getMenuManager();
      Menu var3 = var2.getMenu("menu.View");
      if (var3 != null) {
         for (ViewShell var5 : var1) {
            this.addMenuSwitch(var3, var5);
            this.addMenuExtensionsFromShell(var5);
         }

         this.populateMenus();
      }
   }

   private void addMenuSwitch(Menu var1, ViewShell var2) {
      MenuContribution var3 = new MenuContribution("ViewMenu.ViewSwitches");
      Action var4 = var2.getSwitchAction();
      var3.addItem(new KongaMenuItem(var4));
      var1.addExtension(MenuExtensionPoint.EXTENSION_POINT_1, var3);
   }

   private void addMenuExtensionsFromShell(ViewShell var1) {
      for (MenuExtensionData var3 : var1.getMenuExtensions()) {
         String var4 = var3.getMenuID();
         ViewMenuBuilder.MenuExtensionManager var5 = this.menuExtensionManagers.get(var4);
         if (var5 == null) {
            var5 = new ViewMenuBuilder.MenuExtensionManager(var4, this.appWin);
            this.menuExtensionManagers.put(var4, var5);
         }

         var5.addExtension(var3);
      }
   }

   private void populateMenus() {
      MenuManager var1 = this.appWin.getMenuManager();

      for (ViewMenuBuilder.MenuExtensionManager var3 : this.menuExtensionManagers.values()) {
         var3.populateMenu(var1);
      }
   }

   private static final class MenuExtensionManager {
      private final ApplicationWindow appWin;
      private final String menuId;
      private final ListMultimap<MenuExtensionPoint, ViewMenuItemFactory> extensions = ArrayListMultimap.create();

      public MenuExtensionManager(String var1, ApplicationWindow var2) {
         this.menuId = var1;
         this.appWin = var2;
      }

      public void addExtension(MenuExtensionData var1) {
         MenuExtensionPoint var2 = var1.getExtensionPoint();
         List var3 = var1.getItems();
         this.extensions.putAll(var2, var3);
      }

      public void populateMenu(MenuManager var1) {
         Menu var2 = var1.getMenu(this.menuId);
         if (var2 != null) {
            for (MenuExtensionPoint var6 : MenuExtensionPoint.values()) {
               List var7 = this.extensions.get(var6);
               if (!var7.isEmpty()) {
                  MenuContribution var8 = new MenuContribution("ViewMenu." + var6.name());

                  for (Object __etlItem93 : var7) {
                     ViewMenuItemFactory var10 = (ViewMenuItemFactory) __etlItem93;
                     JMenuItem var11 = var10.createMenuItem(this.appWin);
                     if (var11 != null) {
                        var8.addItem(var11);
                     } else {
                        var8.addSeparator();
                     }
                  }

                  var2.addExtension(var6, var8);
               }
            }
         }
      }
   }
}
