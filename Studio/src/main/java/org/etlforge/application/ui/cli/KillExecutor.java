package org.etlforge.application.ui.cli;

import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;

final class KillExecutor implements CommandExecutor {
   @Override
   public void execute(String[] var1) throws CommandException {
      System.exit(0);
   }
}
