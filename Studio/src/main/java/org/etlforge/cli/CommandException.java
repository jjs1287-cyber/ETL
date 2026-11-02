package org.etlforge.cli;

public final class CommandException extends Exception {
   public CommandException(String var1) {
      super(var1);
   }

   public CommandException(Throwable var1) {
      super(var1);
   }

   public CommandException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
