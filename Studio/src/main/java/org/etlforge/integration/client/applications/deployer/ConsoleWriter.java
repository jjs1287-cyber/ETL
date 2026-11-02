package org.etlforge.integration.client.applications.deployer;

import java.io.PrintStream;

final class ConsoleWriter {
   private boolean quiet = false;

   static ConsoleWriter getWriter(boolean var0) {
      return new ConsoleWriter(var0);
   }

   ConsoleWriter(boolean var1) {
      this.quiet = var1;
   }

   PrintStream getStandardOut() {
      return System.out;
   }

   private PrintStream getStandardErr() {
      return System.err;
   }

   void log(String var1) {
      if (!this.quiet) {
         this.getStandardOut().println("@@@ " + var1);
      }
   }

   void log(Throwable var1) {
      var1.printStackTrace();
   }

   void log(String var1, Throwable var2) {
      this.getStandardErr().println(var1);
      if (var2 != null) {
         this.log(var2);
      }
   }

   void logError(String var1) {
      this.getStandardErr().println(var1);
   }
}
