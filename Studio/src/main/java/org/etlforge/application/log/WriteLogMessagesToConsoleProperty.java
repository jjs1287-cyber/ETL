package org.etlforge.application.log;

final class WriteLogMessagesToConsoleProperty {
   static final boolean ENABLED = Boolean.getBoolean("WriteLogMessagesToConsole");

   private WriteLogMessagesToConsoleProperty() {
      throw new UnsupportedOperationException();
   }
}
