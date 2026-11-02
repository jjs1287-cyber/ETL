package org.etlforge.integration.client.applications.deployer.commandline;

import org.etlforge.application.commandline.AbstractCommandLineOption;
import org.etlforge.integration.client.applications.deployer.JitterPackDeployer;

public class UserOption extends AbstractCommandLineOption {
   private final JitterPackDeployer deployer;

   public UserOption(JitterPackDeployer var1) {
      super("u", "user", "The user that the Jitterpaks should be deployed as", true, true);
      this.deployer = var1;
   }

   @Override
   public void handleArgument(String var1) {
      this.deployer.setUser(var1);
   }
}
