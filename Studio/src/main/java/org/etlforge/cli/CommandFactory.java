package org.etlforge.cli;

public final class CommandFactory {
   public static Command newCommand(String var0, CommandExecutor var1) throws InvalidSyntaxException {
      return new CommandImpl(var0, var1);
   }
}
