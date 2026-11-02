package org.etlforge.cli;

final class CommandImpl implements Command {
   private final String syntax;
   private final CommandParser parser;
   private final CommandExecutor executor;

   public CommandImpl(String var1, CommandExecutor var2) throws InvalidSyntaxException {
      this.syntax = var1;
      this.executor = var2;
      this.parser = new CommandParser(var1);
   }

   @Override
   public String getSyntax() {
      return this.syntax;
   }

   @Override
   public CommandExecutor executor() {
      return this.executor;
   }

   @Override
   public String[] parse(String var1) {
      return this.parser.parse(var1);
   }

   @Override
   public String toString() {
      return this.syntax;
   }

   @Override
   public String complete(String var1) {
      return this.parser.complete(var1);
   }
}
