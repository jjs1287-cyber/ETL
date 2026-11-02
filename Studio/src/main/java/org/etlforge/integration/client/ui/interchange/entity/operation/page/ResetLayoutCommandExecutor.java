package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;

final class ResetLayoutCommandExecutor implements CommandExecutor {
   private final OperationPage page;

   public ResetLayoutCommandExecutor(OperationPage var1) {
      this.page = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      this.page.getGraphController().resetLayout();
   }
}
