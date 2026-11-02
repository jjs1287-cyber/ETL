package org.etlforge.application.ui.view.menu;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.system.EnumWithIdLookup;

public final class MenuExtensionData {
   private final String menuId;
   private final MenuExtensionPoint extensionPoint;
   private List<ViewMenuItemFactory> items;

   public MenuExtensionData(String var1, int var2) {
      this(var1, EnumWithIdLookup.lookup(MenuExtensionPoint.class, var2));
   }

   public MenuExtensionData(String var1, MenuExtensionPoint var2) {
      this.menuId = var1;
      this.extensionPoint = var2;
      this.items = Lists.newArrayList();
   }

   public String getMenuID() {
      return this.menuId;
   }

   public MenuExtensionPoint getExtensionPoint() {
      return this.extensionPoint;
   }

   public void setExtensionItems(List<ViewMenuItemFactory> var1) {
      this.items = Lists.newArrayList(var1);
   }

   public List<ViewMenuItemFactory> getItems() {
      return Lists.newArrayList(this.items);
   }
}
