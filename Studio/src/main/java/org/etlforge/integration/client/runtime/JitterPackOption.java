package org.etlforge.integration.client.runtime;

import java.io.File;
import org.etlforge.application.commandline.CommandLineOption;
import org.etlforge.application.commandline.CommandLineOptionException;
import org.etlforge.application.log.ApplicationLog;

public final class JitterPackOption implements CommandLineOption {
   private final FileInputArgumentHandler handler;

   public JitterPackOption(FileInputArgumentHandler var1) {
      this.handler = var1;
   }

   @Override
   public String getDescription() {
      return "path to a Jitterpak to import";
   }

   @Override
   public String getName() {
      return "jp";
   }

   @Override
   public String getLongName() {
      return "jitterpak";
   }

   @Override
   public void handleArgument(String var1) throws CommandLineOptionException {
      if (var1 != null && var1.length() != 0) {
         File var2 = new File(var1);
         if (var2.exists() && var2.isFile()) {
            ApplicationLog.getMainLogger().info("Asking the ClientPlatform to handle Jitterpak: " + var2.getAbsolutePath());
            this.handler.setJitterpackToImport(var2);
         } else {
            CommandLineOptionException var3 = new CommandLineOptionException(
               "Invalid value of the command line argument -" + this.getName() + ": " + var1 + ". The specified file does not exist or is not a file.", false
            );
            ApplicationLog.getMainLogger().throwing(this.getClass().getName(), "handleArgument", var3);
            throw var3;
         }
      }
   }

   @Override
   public boolean hasArgument() {
      return true;
   }

   @Override
   public boolean isRequired() {
      return false;
   }
}
