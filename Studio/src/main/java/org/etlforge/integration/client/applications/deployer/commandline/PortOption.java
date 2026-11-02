package org.etlforge.integration.client.applications.deployer.commandline;

import org.etlforge.application.commandline.AbstractCommandLineOption;
import org.etlforge.application.commandline.CommandLineOptionException;
import org.etlforge.integration.client.applications.deployer.JitterPackDeployer;

public final class PortOption extends AbstractCommandLineOption {
   private final JitterPackDeployer deployer;

   public PortOption(JitterPackDeployer var1) {
      super("po", "port", "The port to use when talking to the server. Default is 46908.", true, false);
      this.deployer = var1;
   }

   @Override
   public void handleArgument(String var1) throws CommandLineOptionException {
      if (var1 != null) {
         try {
            this.deployer.setPort(Integer.parseInt(var1));
         } catch (NumberFormatException var3) {
            throw new CommandLineOptionException("Invalid port number: " + var1, true);
         }
      }
   }
}
