package org.etlforge.application.commandline;

public abstract class CommandLineParserFactory {
   protected abstract CommandLineParser createNewParser();

   public final CommandLineParser newParser() {
      CommandLineParser var1 = this.createNewParser();
      this.populateParserWithOptions(var1);
      return var1;
   }

   protected abstract void populateParserWithOptions(CommandLineParser var1);
}
