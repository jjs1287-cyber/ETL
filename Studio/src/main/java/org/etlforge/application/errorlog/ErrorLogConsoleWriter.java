package org.etlforge.application.errorlog;

public class ErrorLogConsoleWriter implements ErrorLogListener {
   @Override
   public void errorOccurred(ErrorLogEntry var1) {
      String var2 = var1.getReport();
      System.err.println(var2);
   }
}
