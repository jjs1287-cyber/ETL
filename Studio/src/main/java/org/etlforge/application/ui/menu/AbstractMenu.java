package org.etlforge.application.ui.menu;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.ui.widget.menu.DefaultMenuItemContainer;
import org.etlforge.ui.widget.menu.MenuDisabler;
import org.etlforge.ui.widget.menu.MenuItemContainer;
import org.etlforge.ui.widget.menu.PopupMenuItemContainer;

public abstract class AbstractMenu implements Menu {
   private final String id;
   private final Multimap<MenuExtensionPoint, MenuContribution> extensionsMap;

   public AbstractMenu(String var1) {
      this.id = var1;
      this.extensionsMap = ArrayListMultimap.create();
   }

   @Override
   public final String getID() {
      return this.id;
   }

   @Override
   public final void addExtension(MenuExtensionPoint var1, MenuContribution var2) {
      this.extensionsMap.put(var1, var2);
   }

   @Override
   public final void removeExtension(MenuExtensionPoint var1, String var2) {
      for (MenuContribution var4 : this.extensionsMap.get(var1)) {
         if (var4.getId().equals(var2)) {
            this.extensionsMap.remove(var1, var4);
            break;
         }
      }
   }

   @Override
   public final void removeAllExtensions() {
      this.extensionsMap.clear();
   }

   protected final Collection<MenuContribution> getExtensions(MenuExtensionPoint var1) {
      return this.extensionsMap.get(var1);
   }

   @Override
   public final JMenu createJMenu(MenuDictionary var1) {
      JMenu var2 = new JMenuFactory(this.id, var1).createEmptyMenu();
      this.populate(var2);
      MenuDisabler.install(var2);
      return var2;
   }

   protected abstract List<? extends JMenuItem> getMenuItems();

   @Override
   public final void populate(JMenu var1) {
      this.populate(new DefaultMenuItemContainer(var1));
   }

   @Override
   public final void populate(JPopupMenu var1) {
      this.populate(new PopupMenuItemContainer(var1));
   }

   private void populate(MenuItemContainer var1) {
      var1.removeAll();

      for (JMenuItem var3 : this.prepareMenuItemList()) {
         if (var3 == null) {
            var1.addSeparator();
         } else {
            var1.add(var3);
         }
      }
   }

   protected final void addExtensionItems(List<JMenuItem> var1, MenuExtensionPoint var2) {
      for (MenuContribution var4 : this.getExtensions(var2)) {
         var1.addAll(var4.getMenuItems());
      }
   }

   private List<? extends JMenuItem> prepareMenuItemList() {
      List var1 = this.getMenuItems();
      boolean var2 = false;
      Iterator var3 = var1.iterator();

      while (var3.hasNext()) {
         if (var3.next() == null) {
            if (var2) {
               var3.remove();
            }

            var2 = true;
         } else {
            var2 = false;
         }
      }

      while (!var1.isEmpty() && var1.get(0) == null) {
         var1.remove(0);
      }

      if (!var1.isEmpty()) {
         while (var1.get(var1.size() - 1) == null) {
            var1.remove(var1.size() - 1);
         }
      }

      return var1;
   }

   @Override
   public boolean isSupported() {
      return true;
   }
}
