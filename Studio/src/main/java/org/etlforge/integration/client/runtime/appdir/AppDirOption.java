package org.etlforge.integration.client.runtime.appdir;

import org.etlforge.application.commandline.CommandLineOption;
import org.etlforge.application.commandline.CommandLineOptionException;

public final class AppDirOption implements CommandLineOption {
   @Override
   public String getDescription() {
      return null;
   }

   @Override
   public String getLongName() {
      return "applicationDirectory";
   }

   @Override
   public String getName() {
      return "d";
   }

   @Override
   public void handleArgument(String var1) throws CommandLineOptionException {
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
