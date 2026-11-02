package org.etlforge.integration.activity.ui.core.menu.proxy;

import java.util.List;
import javax.swing.Action;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.menu.AbstractMenu;
import org.etlforge.application.ui.menu.MenuDictionary;
import org.etlforge.application.ui.menu.NewResourceBasedMenuDictionary;
import org.etlforge.application.ui.view.menu.ViewMenuItemFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.activity.ui.resources.ActivityResources;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public class OperationActivityMenuItemFactory implements ViewMenuItemFactory {
   private final List<Action> actions;

   public OperationActivityMenuItemFactory(List<Action> var1) {
      this.actions = var1;
   }

   @Override
   public JMenuItem createMenuItem(ApplicationWindow var1) {
      OperationActivityMenuItemFactory.MenuImpl var2 = new OperationActivityMenuItemFactory.MenuImpl(this.actions);
      return var2.createJMenu(createDictionary());
   }

   private static MenuDictionary createDictionary() {
      return new NewResourceBasedMenuDictionary(ActivityResources.STRINGS, ActivityResources.ICONS, null);
   }

   private static final class MenuImpl extends AbstractMenu {
      private final List<Action> actions;

      public MenuImpl(List<Action> var1) {
         super("Operations");
         this.actions = var1;
      }

      @Override
      protected List<? extends JMenuItem> getMenuItems() {
         return KongaListUtils.transform(this.actions, KongaMenuItem.FROM_ACTION);
      }
   }
}
