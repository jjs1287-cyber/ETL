package org.etlforge.integration.client.applications.deployer.commandline;

import org.etlforge.application.commandline.AbstractCommandLineOption;
import org.etlforge.application.commandline.CommandLineOptionException;
import org.etlforge.integration.client.applications.deployer.JitterPackDeployer;

public final class BackupOption extends AbstractCommandLineOption {
   private final JitterPackDeployer deployer;

   public BackupOption(JitterPackDeployer var1) {
      super("b", "backup", "If set a backup of the jitterpak will be sent to the server after a successful deploy.", false, false);
      this.deployer = var1;
   }

   @Override
   public void handleArgument(String var1) throws CommandLineOptionException {
      this.deployer.setBackupFlag();
   }
}
