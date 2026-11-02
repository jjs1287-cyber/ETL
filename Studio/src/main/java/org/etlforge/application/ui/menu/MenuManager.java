package org.etlforge.application.ui.menu;

import java.util.List;
import javax.swing.JMenu;

public interface MenuManager {
   MenuDictionary getDictionary();

   List<JMenu> getJMenus();

   Menu getMenu(String var1);

   JMenu getJMenu(String var1);

   void addExtension(MenuExtensionPoint var1, Menu var2, MenuDictionary var3);

   void removeExtension(MenuExtensionPoint var1);

   void removeAllExtensions();

   class Extension {
      private final Menu menu;
      private final MenuDictionary dictionary;

      public Extension(Menu var1, MenuDictionary var2) {
         this.menu = var1;
         this.dictionary = var2;
      }

      public String getMenuID() {
         return this.menu.getID();
      }

      public JMenu getJMenu() {
         return this.menu.createJMenu(this.dictionary);
      }
   }
}
