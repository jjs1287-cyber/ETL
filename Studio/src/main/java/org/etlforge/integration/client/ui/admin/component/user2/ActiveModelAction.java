package org.etlforge.integration.client.ui.admin.component.user2;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.etlforge.system.Disposable;

public abstract class ActiveModelAction extends AbstractAction implements Disposable {
   private UserConsoleModel model;
   private UserConsoleModelListener listener;

   protected ActiveModelAction(String var1, Icon var2) {
      super(var1, var2);
      this.setEnabled(false);
   }

   protected ActiveModelAction(UserConsoleModel var1, String var2, Icon var3) {
      super(var2, var3);
      this.setModel(var1);
   }

   public void setModel(UserConsoleModel var1) {
      if (var1 != this.model) {
         if (this.model != null) {
            this.stopListening();
         }

         this.model = var1;
         if (var1 != null) {
            this.setEnabled(var1.isActive());
            var1.addModelListener(this.listener = this.createListener());
         } else {
            this.setEnabled(false);
         }
      }
   }

   @Override
   public void dispose() {
      if (this.model != null) {
         this.stopListening();
      }
   }

   protected final UserConsoleModel getModel() {
      return this.model;
   }

   protected UserConsoleModelListener createListener() {
      return new ActiveModelAction.Activator();
   }

   private void stopListening() {
      this.model.removeModelListener(this.listener);
   }

   protected class Activator extends UserConsoleModelListener.Adapter {
      @Override
      public void activeStatusChanged(UserConsoleModel var1, boolean var2) {
         this.checkEnabledState(var1);
      }

      @Override
      public void readOnlyStateChanged(UserConsoleModel var1, boolean var2) {
         this.checkEnabledState(var1);
      }

      private void checkEnabledState(UserConsoleModel var1) {
         ActiveModelAction.this.setEnabled(var1.isActive() && !var1.isReadOnly());
      }
   }
}
