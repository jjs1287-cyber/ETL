package org.etlforge.application.commandline;

public class CommandLineOptionException extends CommandLineParsingException {
   private final boolean fatal;

   public CommandLineOptionException(String var1, boolean var2) {
      super(var1);
      this.fatal = var2;
   }

   public CommandLineOptionException(String var1, Throwable var2, boolean var3) {
      super(var1, var2);
      this.fatal = var3;
   }

   public boolean fatal() {
      return this.fatal;
   }
}
