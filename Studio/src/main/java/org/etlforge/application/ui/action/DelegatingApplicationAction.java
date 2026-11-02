package org.etlforge.application.ui.action;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.action.delegator.ActionDelegator;
import org.etlforge.application.ui.action.delegator.NoRegisteredDelegateException;
import org.etlforge.ui.MnemonicString;

public class DelegatingApplicationAction extends AbstractApplicationAction {
   private final ActionDelegator delegator;

   public DelegatingApplicationAction(String var1, ActionDictionary var2, ActionDelegator var3) {
      super(var1, var2);
      this.delegator = this.register(var3);
   }

   public DelegatingApplicationAction(String var1, MnemonicString var2, Icon var3, ActionDelegator var4) {
      super(var1, var2, var3);
      this.delegator = this.register(var4);
   }

   public DelegatingApplicationAction(String var1, String var2, Icon var3, ActionDelegator var4) {
      super(var1, var2, var3);
      this.delegator = this.register(var4);
   }

   @Override
   public final void execute(ActionEvent var1) {
      try {
         this.delegator.delegate(this, var1);
      } catch (NoRegisteredDelegateException var3) {
         this.handleDelegationError(var3);
      }
   }

   protected void handleDelegationError(NoRegisteredDelegateException var1) {
      ErrorLog.attention(this.getClass(), "Configuration error: No action handler has been registered for the action with ID = [" + this.getID() + "].", var1);
   }

   private ActionDelegator register(ActionDelegator var1) {
      var1.registerAction(this);
      return var1;
   }
}
