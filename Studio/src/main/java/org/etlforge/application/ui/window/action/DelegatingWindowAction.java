package org.etlforge.application.ui.window.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.delegator.NoRegisteredDelegateException;
import org.etlforge.application.ui.window.ApplicationWindow;

public class DelegatingWindowAction extends AbstractApplicationWindowAction {
   public DelegatingWindowAction(ApplicationWindow var1, String var2, ActionDictionary var3) {
      super(var1, var2, var3);
      var1.getActionDelegator().registerAction(this);
   }

   @Override
   public final void execute(ActionEvent var1) {
      try {
         this.getWindow().getActionDelegator().delegate(this, var1);
      } catch (NoRegisteredDelegateException var3) {
         ErrorLog.attention(
            this.getClass(), "Configuration error: No action handler has been registered for the action with ID = [" + this.getID() + "].", var3
         );
      }
   }
}
