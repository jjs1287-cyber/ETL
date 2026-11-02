package org.etlforge.application.ui.view.menu;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.application.ui.menu.MenuExtensionPoint;
import org.etlforge.system.EnumWithIdLookup;

public final class MenuExtensionDataBuilder {
   private final String menuId;
   private final MenuExtensionPoint extensionPoint;
   private final List<ViewMenuItemFactory> items;

   public MenuExtensionDataBuilder(String var1, int var2, List<ViewMenuItemFactory> var3) {
      this(var1, EnumWithIdLookup.lookup(MenuExtensionPoint.class, var2), var3);
   }

   public MenuExtensionDataBuilder(String var1, MenuExtensionPoint var2, List<ViewMenuItemFactory> var3) {
      this.menuId = var1;
      this.extensionPoint = var2;
      this.items = Lists.newArrayList(var3);
   }

   public void setAdditionalItems(List<ViewMenuItemFactory> var1) {
      this.items.addAll(var1);
   }

   public MenuExtensionData buildExtension() {
      MenuExtensionData var1 = new MenuExtensionData(this.menuId, this.extensionPoint);
      var1.setExtensionItems(this.items);
      return var1;
   }
}
