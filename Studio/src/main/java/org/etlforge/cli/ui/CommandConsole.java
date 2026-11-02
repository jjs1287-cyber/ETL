package org.etlforge.cli.ui;

import com.google.common.base.Function;
import org.etlforge.cli.Command;
import org.etlforge.cli.CommandModule;
import org.etlforge.cli.Result;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public final class CommandConsole implements UiProvider {
   private final CommandProducer producer;
   private CommandModule commands;

   public CommandConsole(CommandProducer var1) {
      this.producer = var1;
      this.commands = new CommandModule();
      this.commands.addCommand(this.helpCommand());
      var1.setCommandConsumer(new CommandConsole.Consumer());
      var1.setCompleter(new CommandConsole.Completer());
   }

   private ActionCommand helpCommand() {
      return new ActionCommand("?", new ShowAvailableCommandsAction(this.commands));
   }

   public void addCommand(Command var1) {
      this.commands.addCommand(var1);
   }

   public void removeCommand(Command var1) {
      this.commands.removeCommand(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.producer.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.producer.requestFocus();
   }

   private final class Completer implements Function<String, String> {
      private Completer() {
      }

      public String apply(String var1) {
         return CommandConsole.this.commands.complete(var1);
      }
   }

   private final class Consumer implements Receiver<String> {
      private Consumer() {
      }

      public void handle(String var1) {
         Result var2 = CommandConsole.this.commands.execute(var1.trim());
         switch (var2) {
            case UNKNOWN_COMMAND:
               this.showError("I do not know what to do with the command\n\n" + var1, "Unknown Command");
               break;
            case ERROR:
               this.showError(CommandConsole.this.commands.lastError().getMessage(), "Error");
            case SUCCESS:
         }
      }

      private void showError(String var1, String var2) {
      }
   }
}
