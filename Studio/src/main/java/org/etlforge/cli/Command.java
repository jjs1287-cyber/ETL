package org.etlforge.cli;

public interface Command {
   String getSyntax();

   String[] parse(String var1);

   String complete(String var1);

   CommandExecutor executor();
}
