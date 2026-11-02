package org.etlforge.cli;

public final class SingleArgumentCommand implements Command {
   private final Command impl;

   public SingleArgumentCommand(String var1, CommandExecutor var2) {
      this(var1, false, var2);
   }

   public SingleArgumentCommand(String var1, boolean var2, CommandExecutor var3) {
      String var4 = var1 + " " + (var2 ? "[{0}]" : "{0}");
      this.impl = CommandFactory.newCommand(var4, var3);
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
