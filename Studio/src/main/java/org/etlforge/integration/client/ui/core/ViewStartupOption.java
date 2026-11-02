package org.etlforge.integration.client.ui.core;

import org.apache.commons.lang.StringUtils;
import org.etlforge.application.commandline.AbstractCommandLineOption;
import org.etlforge.application.commandline.CommandLineOptionException;
import org.etlforge.integration.client.ui.ClientUi;

public class ViewStartupOption extends AbstractCommandLineOption {
   public ViewStartupOption() {
      super("v", "view", "the ID of the view to open on startup", true, false);
   }

   @Override
   public void handleArgument(String var1) throws CommandLineOptionException {
      if (StringUtils.isEmpty(var1)) {
         throw new CommandLineOptionException("Missing value for the -" + this.getName() + " option", false);
      }

      ClientUi.setStartupView(var1);
   }
}
