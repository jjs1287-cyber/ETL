package org.etlforge.application.commandline;

public interface CommandLineOptionCollection {
   void add(CommandLineOption var1);

   CommandLineOption[] getRequiredOptions();

   CommandLineOption[] getOptionalOptions();
}
