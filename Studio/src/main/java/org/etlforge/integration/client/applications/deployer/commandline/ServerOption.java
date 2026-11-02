package org.etlforge.integration.client.applications.deployer.commandline;

import org.etlforge.application.commandline.AbstractCommandLineOption;
import org.etlforge.integration.client.applications.deployer.JitterPackDeployer;

public class ServerOption extends AbstractCommandLineOption {
   private final JitterPackDeployer deployer;

   public ServerOption(JitterPackDeployer var1) {
      super("s", "server", "The host name or IP-number of the server to which the Jitterpaks are to be deployed (host:port is allowed)", true, true);
      this.deployer = var1;
   }

   @Override
   public void handleArgument(String var1) {
      this.deployer.setServer(var1);
   }
}
