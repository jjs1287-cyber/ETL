package org.etlforge.integration.client.applications.deployer.commandline;

import java.io.File;
import org.etlforge.application.commandline.AbstractCommandLineOption;
import org.etlforge.application.commandline.CommandLineOptionException;
import org.etlforge.integration.client.applications.deployer.JitterPackDeployer;

public class JitterPackOption extends AbstractCommandLineOption {
   private final JitterPackDeployer deployer;

   public JitterPackOption(JitterPackDeployer var1) {
      super("jp", "jitterpak", "Path to an individual Jitterpak to deploy (use this or -d)", true, false);
      this.deployer = var1;
   }

   @Override
   public void handleArgument(String var1) throws CommandLineOptionException {
      if (var1 != null) {
         File var2 = new File(var1);
         if (!var2.exists()) {
            String var4 = "The file " + var1 + " does not exist";
            throw new CommandLineOptionException(var4, true);
         }

         if (var2.isDirectory()) {
            String var3 = "The path " + var1 + " does not represent a file.";
            throw new CommandLineOptionException(var3, true);
         }

         this.deployer.setIndividualFile(var2);
      }
   }
}
