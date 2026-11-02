package org.etlforge.cli;

public final class NoArgumentCommand implements Command {
   private final Command impl;

   public NoArgumentCommand(String var1, CommandExecutor var2) {
      this.impl = CommandFactory.newCommand(var1, var2);
   }

   @Override
   public String getSyntax() {
      return this.impl.getSyntax();
   }

   @Override
   public String[] parse(String var1) {
      return this.impl.parse(var1);
   }

   @Override
   public String complete(String var1) {
      return this.impl.complete(var1);
   }

   @Override
   public CommandExecutor executor() {
      return this.impl.executor();
   }
}
