package org.etlforge.integration.client.applications.deployer.commandline;

import java.io.File;
import org.etlforge.application.commandline.AbstractCommandLineOption;
import org.etlforge.application.commandline.CommandLineOptionException;
import org.etlforge.integration.client.applications.deployer.JitterPackDeployer;

public class DirectoryOption extends AbstractCommandLineOption {
   private final JitterPackDeployer deployer;

   public DirectoryOption(JitterPackDeployer var1) {
      super("d", "directory", "Path to the directory in which the Jitterpaks are located (use this or -jp)", true, false);
      this.deployer = var1;
   }

   @Override
   public void handleArgument(String var1) throws CommandLineOptionException {
      if (var1 != null) {
         File var2 = new File(var1);
         this.deployer.setDirectory(var2);
      }
   }
}
