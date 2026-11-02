package org.etlforge.integration.client.applications.deployer.commandline;

import org.etlforge.application.commandline.AbstractCommandLineOption;
import org.etlforge.application.commandline.CommandLineOptionException;
import org.etlforge.integration.client.applications.deployer.JitterPackDeployer;

public class QuietOption extends AbstractCommandLineOption {
   private final JitterPackDeployer deployer;

   public QuietOption(JitterPackDeployer var1) {
      super("q", "quiet", "If set to true, no informative messages will be written to stdout", false, false);
      this.deployer = var1;
   }

   @Override
   public void handleArgument(String var1) throws CommandLineOptionException {
      this.deployer.setQuietFlag();
   }
}
