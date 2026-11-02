package org.etlforge.integration.client.applications.deployer.commandline;

import org.etlforge.application.commandline.AbstractCommandLineOption;
import org.etlforge.integration.client.applications.deployer.JitterPackDeployer;

public class RecursiveOption extends AbstractCommandLineOption {
   private final JitterPackDeployer deployer;

   public RecursiveOption(JitterPackDeployer var1) {
      super("r", "recursive", "Defines if the specified directory should be searched recursively (use only with -d, default is false)", false, false);
      this.deployer = var1;
   }

   @Override
   public void handleArgument(String var1) {
      this.deployer.setRecursive(true);
   }
}
