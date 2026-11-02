package org.etlforge.cli.ui;

import javax.swing.Action;
import org.etlforge.cli.Command;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.cli.NoArgumentCommand;
import org.etlforge.ui.action.ActionUtils;

public final class ActionCommand implements Command {
   private final Command impl;

   public ActionCommand(final String var1, final Action var2) {
      this.impl = new NoArgumentCommand(var1, new CommandExecutor() {
         @Override
         public void execute(String[] var1x) throws CommandException {
            if (var2.isEnabled()) {
               ActionUtils.run(var2, this, var1);
            }
         }
      });
   }

   @Override
   public String getSyntax() {
      return this.impl.getSyntax();
   }

   @Override
   public final CommandExecutor executor() {
      return this.impl.executor();
   }

   @Override
   public String complete(String var1) {
      return this.impl.complete(var1);
   }

   @Override
   public final String[] parse(String var1) {
      return this.impl.parse(var1);
   }
}
