package org.etlforge.integration.client.ui.interchange.component.menu.proxy;

import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.view.menu.BeanProxyAction;
import org.etlforge.application.ui.view.menu.ViewMenuItemFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.core.action.InterchangeActionDictionary;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class CopyEntityMenuItemFactory implements ViewMenuItemFactory {
   private final List<String> actionIds;

   public CopyEntityMenuItemFactory(List<String> var1) {
      this.actionIds = var1;
   }

   @Override
   public JMenuItem createMenuItem(ApplicationWindow var1) {
      KongaMenu var2 = new KongaMenu("&Copy");
      this.addMenuItems(var1, var2);
      var2.disableIfAllItemsAreDisabled();
      return var2;
   }

   private void addMenuItems(ApplicationWindow var1, JMenu var2) {
      for (String var4 : this.actionIds) {
         BeanProxyAction var5 = new BeanProxyAction(var1, "interchange", var4, InterchangeActionDictionary.DICTIONARY);
         var2.add(new KongaMenuItem(var5));
      }
   }
}
