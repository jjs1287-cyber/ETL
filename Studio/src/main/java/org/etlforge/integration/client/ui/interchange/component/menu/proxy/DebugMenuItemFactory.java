package org.etlforge.integration.client.ui.interchange.component.menu.proxy;

import javax.swing.JMenuItem;
import org.etlforge.application.ui.view.menu.BeanProxyAction;
import org.etlforge.application.ui.view.menu.ViewMenuItemFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.debug.action.DebugActionDictionary;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public final class DebugMenuItemFactory implements ViewMenuItemFactory {
   private final String actionId;

   public DebugMenuItemFactory(String var1) {
      this.actionId = var1;
   }

   @Override
   public final JMenuItem createMenuItem(ApplicationWindow var1) {
      BeanProxyAction var2 = new BeanProxyAction(var1, "interchange", this.actionId, DebugActionDictionary.DICTIONARY);
      return new KongaMenuItem(var2);
   }
}
