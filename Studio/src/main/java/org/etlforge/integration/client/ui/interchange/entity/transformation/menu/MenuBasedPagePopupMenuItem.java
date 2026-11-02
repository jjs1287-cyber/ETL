package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import java.util.List;
import javax.swing.JMenu;
import org.etlforge.application.ui.action.popup.AbstractMenuBasedPopupMenuItem;
import org.etlforge.application.ui.action.popup.PopupMenuSite;
import org.etlforge.application.ui.menu.Menu;
import org.etlforge.integration.client.ui.interchange.component.menu.InterchangeMenuDictionary;
import org.etlforge.integration.client.ui.interchange.core.action.popup.PopupMenuSites;
import org.etlforge.integration.data.entity.Transformation;

final class MenuBasedPagePopupMenuItem extends AbstractMenuBasedPopupMenuItem {
   public MenuBasedPagePopupMenuItem(Menu var1) {
      super(var1.createJMenu(InterchangeMenuDictionary.DICTIONARY));
   }

   public MenuBasedPagePopupMenuItem(JMenu var1) {
      super(var1);
   }

   @Override
   public void updateState(PopupMenuSite var1, List<?> var2) {
      this.setVisible(this.isVisibleFor(var1, var2));
      this.setEnabled(this.isVisible());
   }

   private boolean isVisibleFor(PopupMenuSite var1, List<?> var2) {
      return isSupportedSite(var1) && var2.size() == 1 ? var2.get(0) instanceof Transformation : false;
   }

   private static boolean isSupportedSite(PopupMenuSite var0) {
      return var0 == PopupMenuSites.PAGE || var0 == PopupMenuSites.DETACHED_PAGE;
   }
}
