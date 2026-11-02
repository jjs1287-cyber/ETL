package org.etlforge.integration.client.applications.deployer.commandline;

import org.etlforge.application.commandline.AbstractCommandLineOption;
import org.etlforge.integration.client.applications.deployer.JitterPackDeployer;

public class PasswordOption extends AbstractCommandLineOption {
   private final JitterPackDeployer deployer;

   public PasswordOption(JitterPackDeployer var1) {
      super("p", "password", "The password of the ETLForge user to deploy as (use this or -ph)", true, false);
      this.deployer = var1;
   }

   @Override
   public void handleArgument(String var1) {
      this.deployer.setPassword(var1);
   }
}
