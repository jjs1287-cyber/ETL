package org.etlforge.application.ui.view.menu;

import org.etlforge.application.Application;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.view.View;
import org.etlforge.application.ui.window.ApplicationWindow;

public final class BeanProxyAction extends AbstractProxyAction {
   public BeanProxyAction(ApplicationWindow var1, String var2, String var3, ActionDictionary var4) {
      super(var1, var2, var3, var4);
   }

   @Override
   protected ApplicationAction createAction(String var1, View var2) {
      return Application.getBean(var1, ApplicationAction.class);
   }
}
