package org.etlforge.integration.client.ui.admin.component.controlpanel;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import org.etlforge.cli.Command;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.cli.CommandFactory;
import org.etlforge.cli.CommandModule;
import org.etlforge.integration.client.ui.admin.core.console.ConsoleLauncher;

final class ConsoleCommands {
   public static CommandModule build(Iterable<ConsoleLauncher> var0) {
      CommandModule var1 = new CommandModule();
      var1.addCommands(Iterables.transform(var0, new ConsoleCommands.Converter()));
      return var1;
   }

   private ConsoleCommands() {
   }

   private static final class Converter implements Function<ConsoleLauncher, Command> {
      private Converter() {
      }

      public Command apply(final ConsoleLauncher var1) {
         return CommandFactory.newCommand(var1.getName().toLowerCase(), new CommandExecutor() {
            @Override
            public void execute(String[] var1x) throws CommandException {
               var1.launch();
            }
         });
      }
   }
}
