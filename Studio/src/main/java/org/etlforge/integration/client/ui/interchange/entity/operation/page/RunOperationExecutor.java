package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import java.awt.EventQueue;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.ui.operation.runner.OperationRunner;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.system.callback.Receiver;

final class RunOperationExecutor implements CommandExecutor {
   private final OperationRunnerFactory factory;
   private final OperationPage page;

   public RunOperationExecutor(OperationRunnerFactory var1, OperationPage var2) {
      this.page = var2;
      this.factory = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      Operation var2 = this.page.getObject();
      OperationRunner var3 = this.factory.newRunner();
      var3.run(var2, 1, new Receiver<Long>() {
         public void handle(Long var1) {
            RunOperationExecutor.this.openMonitor(var1);
         }
      });
   }

   private void openMonitor(final long var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RunOperationExecutor.this.page.openActivityMonitor(var1);
         }
      });
   }
}
