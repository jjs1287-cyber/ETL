package org.etlforge.application.ui.action;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.wait.WaitService;

public abstract class AbstractApplicationAction extends AbstractAction implements ApplicationAction {
   private final String id;
   private WaitService waitService = WaitService.NONE;

   public AbstractApplicationAction(String var1, ActionDictionary var2) {
      this(var1, (String)null, null);
      ActionDecorators.decorateFromDictionary(this, var2, var1);
   }

   public AbstractApplicationAction(String var1, MnemonicString var2, Icon var3) {
      this(var1, var2.getText(), var3);
   }

   public AbstractApplicationAction(String var1, String var2, Icon var3) {
      super(var2, var3);
      this.id = var1;
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      if (this.isEnabled()) {
         this.runThis(var1);
      } else {
         this.invokedWhenDisabled();
      }
   }

   private void runThis(ActionEvent var1) {
      try {
         this.execute(var1);
      } catch (Exception var3) {
         ErrorLog.log(this.getClass(), var3.getMessage(), var3);
      }
   }

   protected void invokedWhenDisabled() {
   }

   @Override
   public final String getID() {
      return this.id;
   }

   @Override
   public void dispose() {
   }

   public void setEnabledSafely(final boolean var1) {
      if (EventQueue.isDispatchThread()) {
         this.setEnabled(var1);
      } else {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AbstractApplicationAction.this.setEnabled(var1);
            }
         });
      }
   }

   public final void setWaitService(WaitService var1) {
      Preconditions.checkNotNull(var1, "waitService");
      this.waitService = var1;
   }

   protected final WaitService getWaitService() {
      return this.waitService;
   }

   @Override
   public boolean isSupported() {
      return true;
   }
}
