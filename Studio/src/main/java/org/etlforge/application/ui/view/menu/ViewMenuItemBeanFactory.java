package org.etlforge.application.ui.view.menu;

import javax.swing.JMenuItem;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.ui.widget.menu.KongaMenuItem;

public class ViewMenuItemBeanFactory implements ViewMenuItemFactory {
   private final String viewId;
   private final String actionId;
   private final ActionDictionary dictionary;

   public ViewMenuItemBeanFactory(String var1, String var2, ActionDictionary var3) {
      this.viewId = var1;
      this.actionId = var2;
      this.dictionary = var3;
   }

   @Override
   public final JMenuItem createMenuItem(ApplicationWindow var1) {
      BeanProxyAction var2 = new BeanProxyAction(var1, this.viewId, this.actionId, this.dictionary);
      return new KongaMenuItem(var2);
   }
}
