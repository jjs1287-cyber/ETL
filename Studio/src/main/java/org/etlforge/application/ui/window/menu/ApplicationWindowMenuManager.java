package org.etlforge.application.ui.window.menu;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JMenu;
import org.etlforge.application.ui.menu.Menu;
import org.etlforge.application.ui.menu.MenuDictionary;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.application.ui.menu.MenuManager;
import org.etlforge.application.ui.menu.NewResourceBasedMenuDictionary;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.ApplicationWindow;

public final class ApplicationWindowMenuManager implements MenuManager {
   private static final String STRING_RESOURCES = "org.etlforge.application.ui.window.menu.resources.Strings";
   private static final String ICON_RESOURCES = "org.etlforge.application.ui.window.menu.resources.Icons";
   private final MenuDictionary dictionary;
   private final LinkedHashMap<String, Menu> menuMap;
   private final Map<String, JMenu> jmenuMap;
   private final Map<MenuExtensionPoint, MenuManager.Extension> extensions = Maps.newHashMap();

   public ApplicationWindowMenuManager(ApplicationWindow var1) {
      this.menuMap = Maps.newLinkedHashMap();
      this.jmenuMap = Maps.newHashMap();
      this.dictionary = createDictionary();
   }

   public void setMenus(List<? extends Menu> var1) {
      for (Menu var3 : var1) {
         if (var3 == null) {
            this.menuMap.put(null, null);
         } else {
            this.menuMap.put(var3.getID(), var3);
         }
      }
   }

   @Override
   public MenuDictionary getDictionary() {
      return this.dictionary;
   }

   @Override
   public List<JMenu> getJMenus() {
      this.jmenuMap.clear();
      ArrayList var1 = new ArrayList();

      for (Menu var3 : this.menuMap.values()) {
         if (var3 != null) {
            if (var3.isSupported()) {
               JMenu var4 = var3.createJMenu(this.dictionary);
               var1.add(var4);
               this.jmenuMap.put(var3.getID(), var4);
            }
         } else {
            this.createExtensionMenus(var1);
         }
      }

      return var1;
   }

   private void createExtensionMenus(List<JMenu> var1) {
      for (MenuExtensionPoint var5 : MenuExtensionPoint.values()) {
         MenuManager.Extension var6 = this.extensions.get(var5);
         if (var6 != null) {
            JMenu var7 = var6.getJMenu();
            var1.add(var7);
            this.jmenuMap.put(var6.getMenuID(), var7);
         }
      }
   }

   @Override
   public Menu getMenu(String var1) {
      return this.menuMap.get(var1);
   }

   @Override
   public JMenu getJMenu(String var1) {
      return this.jmenuMap.get(var1);
   }

   @Override
   public void addExtension(MenuExtensionPoint var1, Menu var2, MenuDictionary var3) {
      this.extensions.put(var1, new MenuManager.Extension(var2, var3));
   }

   @Override
   public void removeExtension(MenuExtensionPoint var1) {
      this.extensions.remove(var1);
   }

   @Override
   public void removeAllExtensions() {
      this.extensions.clear();
   }

   private static MenuDictionary createDictionary() {
      return NewResourceBasedMenuDictionary.newDictionary(
         "org.etlforge.application.ui.window.menu.resources.Strings",
         ApplicationResources.MNEMONIC_FACTORY,
         "org.etlforge.application.ui.window.menu.resources.Icons",
         null
      );
   }
}
