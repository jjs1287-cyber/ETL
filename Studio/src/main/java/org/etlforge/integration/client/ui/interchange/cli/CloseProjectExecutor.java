package org.etlforge.integration.client.ui.interchange.cli;

import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.CurrentProjectService;

public final class CloseProjectExecutor implements CommandExecutor {
   private final CurrentProjectService service;

   public CloseProjectExecutor(CurrentProjectService var1) {
      this.service = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      try {
         this.service.closeProject();
      } catch (CloseProjectVetoException var3) {
         throw new CommandException(var3);
      }
   }
}
