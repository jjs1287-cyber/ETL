package org.etlforge.application.ui.groovy;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.ui.util.KeyUtils;

final class ExecuteAction extends AbstractAction {
   private final Executor executor;
   private final Supplier<String> scriptSupplier;

   public ExecuteAction(Executor var1, Supplier<String> var2) {
      super("Execute Script");
      this.executor = var1;
      this.scriptSupplier = var2;
      this.putValue("AcceleratorKey", KeyUtils.F5);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ExecuteAction.this.execute();
         }
      });
   }

   private void execute() {
      String var1 = this.scriptSupplier.get();
      this.executor.execute(var1);
   }
}
