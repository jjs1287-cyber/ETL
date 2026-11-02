package org.etlforge.integration.client.applications.deployer.commandline;

import org.etlforge.application.commandline.AbstractCommandLineOption;
import org.etlforge.application.commandline.CommandLineOptionException;
import org.etlforge.integration.client.applications.deployer.JitterPackDeployer;

public class PasswordHashOption extends AbstractCommandLineOption {
   private final JitterPackDeployer deployer;

   public PasswordHashOption(JitterPackDeployer var1) {
      super("ph", "password-hash", "The MD5 of the password of the ETLForge user to deploy as (use this or -p)", true, false);
      this.deployer = var1;
   }

   @Override
   public void handleArgument(String var1) throws CommandLineOptionException {
      this.deployer.setPasswordHash(var1);
   }
}
