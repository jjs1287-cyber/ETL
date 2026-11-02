package org.etlforge.application.commandline;

public interface CommandLineParser {
   void addOption(CommandLineOption var1);

   void parse(String[] var1) throws CommandLineParsingException;

   CommandLineOptionCollection getOptions();
}
