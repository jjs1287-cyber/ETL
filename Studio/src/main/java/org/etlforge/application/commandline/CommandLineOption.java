package org.etlforge.application.commandline;

public interface CommandLineOption {
   String getName();

   String getLongName();

   String getDescription();

   boolean isRequired();

   boolean hasArgument();

   void handleArgument(String var1) throws CommandLineOptionException;
}
