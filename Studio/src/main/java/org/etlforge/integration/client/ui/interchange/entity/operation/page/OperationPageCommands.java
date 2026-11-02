package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import org.etlforge.cli.Command;
import org.etlforge.cli.CommandFactory;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;

final class OperationPageCommands {
   private final OperationRunnerFactory runnerFactory;
   private final UiEntityFactory entityFactory;
   private final OperationPage page;

   public OperationPageCommands(OperationRunnerFactory var1, UiEntityFactory var2, OperationPage var3) {
      this.runnerFactory = var1;
      this.entityFactory = var2;
      this.page = var3;
   }

   public Command[] build() {
      return new Command[]{
         CommandFactory.newCommand("run operation", new RunOperationExecutor(this.runnerFactory, this.page)),
         CommandFactory.newCommand("reset layout", new ResetLayoutCommandExecutor(this.page)),
         CommandFactory.newCommand("call from script", new CallFromScriptExecutor(this.entityFactory, this.page.getObject()))
      };
   }
}
