package org.etlforge.util.net;

public interface CommandResult extends Iterable<String> {
   String getCommandName();

   int getNumberOfReturnValues();

   Throwable getException();
}
