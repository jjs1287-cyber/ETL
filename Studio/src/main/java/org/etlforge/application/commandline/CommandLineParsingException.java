package org.etlforge.application.commandline;

public class CommandLineParsingException extends Exception {
   public CommandLineParsingException(Throwable var1) {
      super(var1);
   }

   public CommandLineParsingException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public CommandLineParsingException(String var1) {
      super(var1);
   }
}
