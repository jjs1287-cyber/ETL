package org.etlforge.application.ui.window.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.delegator.NoRegisteredDelegateException;
import org.etlforge.application.ui.window.ApplicationWindow;

public abstract class AbstractApplicationWindowAction extends AbstractApplicationAction {
   private final ApplicationWindow window;

   public AbstractApplicationWindowAction(ApplicationWindow var1, String var2, ActionDictionary var3) {
      super(var2, var3);
      this.window = var1;
   }

   protected final ApplicationWindow getWindow() {
      return this.window;
   }

   protected final boolean isDelegated() {
      return this.window.getActionDelegator().isDelegated(this.getID());
   }

   protected final void delegate(ActionEvent var1) {
      try {
         this.window.getActionDelegator().delegate(this, var1);
      } catch (NoRegisteredDelegateException var3) {
         ErrorLog.attention(this.getClass(), "Application configuration error", var3);
      }
   }
}
