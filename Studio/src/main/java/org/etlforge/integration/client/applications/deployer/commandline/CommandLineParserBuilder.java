package org.etlforge.integration.client.applications.deployer.commandline;

import org.etlforge.application.commandline.CommandLineParser;
import org.etlforge.application.commandline.JakartaCLICommandLineParser;
import org.etlforge.integration.client.applications.deployer.JitterPackDeployer;

public final class CommandLineParserBuilder {
   public static CommandLineParser createCommandLineParser(JitterPackDeployer var0) {
      JakartaCLICommandLineParser var1 = new JakartaCLICommandLineParser();
      var1.addOption(new DirectoryOption(var0));
      var1.addOption(new JitterPackOption(var0));
      var1.addOption(new RecursiveOption(var0));
      var1.addOption(new ServerOption(var0));
      var1.addOption(new PortOption(var0));
      var1.addOption(new UserOption(var0));
      var1.addOption(new PasswordOption(var0));
      var1.addOption(new PasswordHashOption(var0));
      var1.addOption(new QuietOption(var0));
      var1.addOption(new BackupOption(var0));
      return var1;
   }

   private CommandLineParserBuilder() {
   }
}
