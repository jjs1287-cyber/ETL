package org.etlforge.integration.client.ui.interchange.component.menu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JMenu;
import org.etlforge.application.ui.menu.Menu;
import org.etlforge.application.ui.menu.MenuDictionary;
import org.etlforge.application.ui.menu.MenuExtension;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.application.ui.menu.MenuManager;
import org.etlforge.application.ui.view.menu.ViewMenuManager;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.menu.prefs.IncludeTransformationMenuPreference;
import org.etlforge.system.EnumWithIdLookup;

public final class InterchangeMenuManager implements ViewMenuManager {
   private final ApplicationWindow appWin;
   private final MenuDictionary dictionary;
   private final Map<String, Menu> menuMap;
   private final Map<String, JMenu> jmenuMap;
   private Map<String, Menu> subMenuMap;
   private Map<Integer, ? extends Menu> topLevelMenus;
   private List<? extends MenuExtension> extensions;

   public InterchangeMenuManager(ApplicationWindow var1) {
      this.appWin = var1;
      this.dictionary = InterchangeMenuDictionary.DICTIONARY;
      this.menuMap = Maps.newHashMap();
      this.jmenuMap = Maps.newHashMap();
      this.topLevelMenus = Maps.newHashMap();
      this.subMenuMap = Maps.newHashMap();
      this.extensions = Lists.newArrayList();
      this.listenToPreferences(var1);
   }

   public void setTopLevelMenus(Map<Integer, ? extends Menu> var1) {
      this.topLevelMenus = var1;

      for (Menu var3 : var1.values()) {
         this.addMenu(var3);
      }
   }

   public void setSubMenus(List<? extends Menu> var1) {
      this.subMenuMap = Maps.newHashMap();

      for (Menu var3 : var1) {
         this.addMenu(var3);
         this.subMenuMap.put(var3.getID(), var3);
      }
   }

   public void setExtensions(List<? extends MenuExtension> var1) {
      this.extensions = var1;
   }

   @Override
   public void addTopLevelExtensionsToWindow() {
      for (Entry var2 : this.topLevelMenus.entrySet()) {
         MenuExtensionPoint var3 = EnumWithIdLookup.lookup(MenuExtensionPoint.class, (Integer)var2.getKey());
         this.getMenuBar().addExtension(var3, (Menu)var2.getValue(), InterchangeMenuDictionary.DICTIONARY);
      }
   }

   private MenuManager getMenuBar() {
      return this.appWin.getMenuManager();
   }

   @Override
   public void addMenuLevelExtensionsToWindow() {
      for (MenuExtension var2 : this.extensions) {
         String var3 = var2.getMenuId();
         Menu var4 = this.getMenuBar().getMenu(var3);
         if (var4 != null) {
            var2.addToMenu(var4);
         }
      }
   }

   @Override
   public void removeMenuLevelExtensionsFromWindow() {
      for (MenuExtension var2 : this.extensions) {
         this.removeMenuLevelExtension(var2);
      }
   }

   private void removeMenuLevelExtension(MenuExtension var1) {
      String var2 = var1.getMenuId();
      Menu var3 = this.getMenu(var2);
      if (var3 != null) {
         var1.removeFromMenu(var3);
         JMenu var4 = this.getJMenu(var2);
         if (var4 != null) {
            var3.populate(var4);
         }
      }
   }

   @Override
   public void removeTopLevelExtensionsFromWindow() {
   }

   @Override
   public MenuDictionary getDictionary() {
      return this.dictionary;
   }

   @Override
   public Menu getMenu(String var1) {
      Menu var2 = this.menuMap.get(var1);
      return var2 != null ? var2 : this.getMenuBar().getMenu(var1);
   }

   @Override
   public JMenu getJMenu(String var1) {
      JMenu var2 = null;
      boolean var3 = this.subMenuMap.containsKey(var1);
      if (var3) {
         var2 = this.createJMenu(var1);
      } else {
         var2 = this.jmenuMap.get(var1);
         if (var2 == null) {
            var2 = this.createJMenu(var1);
            this.jmenuMap.put(var1, var2);
         }
      }

      return var2;
   }

   private JMenu createJMenu(String var1) {
      Menu var2 = this.menuMap.get(var1);
      return var2 != null ? var2.createJMenu(this.dictionary) : this.getMenuBar().getJMenu(var1);
   }

   @Override
   public List<JMenu> getJMenus() {
      throw new UnsupportedOperationException();
   }

   @Override
   public final void addExtension(MenuExtensionPoint var1, Menu var2, MenuDictionary var3) {
      throw new UnsupportedOperationException();
   }

   @Override
   public final void removeExtension(MenuExtensionPoint var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public final void removeAllExtensions() {
      throw new UnsupportedOperationException();
   }

   private void listenToPreferences(ApplicationWindow var1) {
      Menu var2 = this.getMenu("menu.ic.Transformation");
      TransformationMenuPreferenceListener var3 = new TransformationMenuPreferenceListener(var1, var2);
      IncludeTransformationMenuPreference.INSTANCE.addListener(var3);
   }

   private void addMenu(Menu var1) {
      this.menuMap.put(var1.getID(), var1);
   }
}
