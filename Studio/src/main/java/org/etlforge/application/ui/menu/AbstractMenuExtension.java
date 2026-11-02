package org.etlforge.application.ui.menu;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import org.etlforge.application.DebugMode;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public abstract class AbstractMenuExtension implements MenuExtension {
   private final String menuId;
   private final Map<MenuExtensionPoint, MenuContribution> extensionItems;

   protected AbstractMenuExtension(String var1) {
      this.menuId = var1;
      this.extensionItems = Maps.newHashMap();
   }

   @Override
   public final String getMenuId() {
      return this.menuId;
   }

   @Override
   public final void addToMenu(Menu var1) {
      Preconditions.checkArgument(var1.getID().equals(this.menuId), "Unexpected menu ID. Expected: " + this.menuId + ". Got: " + var1.getID());

      for (MenuExtensionPoint var5 : MenuExtensionPoint.values()) {
         MenuContribution var6 = this.extensionItems.get(var5);
         if (var6 == null) {
            var6 = this.createExtension(var5);
            if (var6 != null) {
               this.extensionItems.put(var5, var6);
               var1.addExtension(var5, var6);
            }
         }
      }
   }

   @Override
   public final void removeFromMenu(Menu var1) {
      Preconditions.checkArgument(var1.getID().equals(this.menuId), "Unexpected menu ID. Expected: " + this.menuId + ". Got: " + var1.getID());

      for (MenuExtensionPoint var5 : MenuExtensionPoint.values()) {
         MenuContribution var6 = this.extensionItems.get(var5);
         if (var6 != null) {
            var1.removeExtension(var5, var6.getId());
         }
      }
   }

   public abstract MenuContribution createExtension(MenuExtensionPoint var1);

   protected final void addActions(MenuContribution var1, List<? extends Action> var2) {
      for (Action var4 : var2) {
         if (isActionIncluded(var4)) {
            var1.addItem(new KongaMenuItem(var4));
         }
      }
   }

   private static boolean isActionIncluded(Action var0) {
      if (var0 instanceof ApplicationAction) {
         boolean var1 = ((ApplicationAction)var0).isSupported();
         if (!var1) {
            return false;
         }
      }

      return DebugMode.IS_ENABLED ? true : !DebugMode.isDebugModeOnlyAction(var0);
   }
}
