package org.etlforge.integration.client.applications.deployer;

import java.io.File;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.commandline.CommandLineOptionFormatter;
import org.etlforge.application.commandline.CommandLineParser;
import org.etlforge.application.commandline.CommandLineParsingException;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.integration.client.applications.deployer.commandline.CommandLineParserBuilder;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.jitterpack.Importer;
import org.etlforge.integration.client.server.ServerManager;

public final class JitterPackDeployer {
   private static final File appDirLocation = ApplicationTempFolder.newUniqueFolder(true);
   private File directory;
   private boolean recursive;
   private File individualFile;
   private String server;
   private Integer port;
   private String user;
   private String password;
   private String passwordHash;
   private boolean doBackup;
   private ConsoleWriter consoleWriter = ConsoleWriter.getWriter(false);
   private CommandLineParser parser;
   private final ServerManager serverManager;
   private final ProjectManager projectManager;
   private final Importer importer;

   public static void main(String[] var0) {
      ApplicationLog.setFileLoggingEnabled(false);
      start(var0);
   }

   public static File getApplicationHome() {
      return appDirLocation;
   }

   public JitterPackDeployer() {
      ApplicationInitializer var1 = new ApplicationInitializer(this.consoleWriter);
      this.serverManager = var1.getServerManager();
      this.projectManager = var1.getProjectManager();
      this.importer = var1.getImporter();
   }

   public ServerManager getServerManager() {
      return this.serverManager;
   }

   public static void start(String[] var0) {
      System.setProperty("etlforge.noninteractivemode", "true");
      short var1 = 0;

      try {
         JitterPackDeployer var2 = new JitterPackDeployer();
         var2.parseCommandLineArguments(var0);
         String var3 = var2.buildServerAddress();
         var2.connectToServer(var3);
         if (!var2.doDeploy()) {
            var1 = 200;
         }
      } catch (Throwable var4) {
         var4.printStackTrace();
         var1 = 100;
      }

      System.exit(var1);
   }

   private void printUsageToStdOut() {
      if (this.parser != null) {
         CommandLineOptionFormatter var1 = new CommandLineOptionFormatter(28);
         this.consoleWriter.getStandardOut().println(var1.getUsageMessage("Usage: JitterpakDeployer -s server -u user [options]", this.parser.getOptions()));
      }
   }

   private void parseCommandLineArguments(String[] var1) {
      try {
         this.parser = CommandLineParserBuilder.createCommandLineParser(this);
         this.parser.parse(var1);
         this.validateCommandLineArguments();
      } catch (CommandLineParsingException var3) {
         this.printUsageToStdOut();
         this.consoleWriter.logError(var3.getMessage());
         System.exit(11);
      }
   }

   private void validateCommandLineArguments() throws CommandLineParsingException {
      if (this.individualFile == null && this.directory == null) {
         String var2 = "One of the -jp or -d flags must be specified.";
         throw new CommandLineParsingException(var2);
      }

      if (this.individualFile != null && this.directory != null) {
         String var1 = "Only one of the -jp and -d flags may be specified.";
         throw new CommandLineParsingException(var1);
      }
   }

   private String buildServerAddress() throws Exception {
      String var2 = "";
      int var3 = this.server.indexOf(":");
      String var1;
      if (var3 == -1) {
         var1 = this.server;
      } else {
         var1 = this.server.substring(0, var3);
         if (var3 < this.server.length() - 1) {
            var2 = this.server.substring(var3 + 1);
         }
      }

      int var4 = this.getPort(var2);
      return var1 + ":" + var4;
   }

   private int getPort(String var1) throws Exception {
      if (var1.isEmpty()) {
         return this.port != null ? this.port : 46908;
      } else if (this.port == null) {
         return Integer.valueOf(var1);
      } else if (!var1.equals(this.port.toString())) {
         throw new Exception("Conflicting port numbers given in the -s and -po arguments.");
      } else {
         return this.port;
      }
   }

   private void connectToServer(String var1) {
      try {
         boolean var2 = this.password == null;
         ServerConnector var3 = new ServerConnector(this.serverManager, this.consoleWriter);
         var3.connect(var1, this.user, var2 ? this.passwordHash : this.password, var2);
      } catch (Throwable var4) {
         this.consoleWriter.log(var4);
         System.exit(12);
      }
   }

   private boolean doDeploy() {
      JitterPackCollector var1 = this.createJitterPackCollector();
      DeployWorker var2 = new DeployWorker(this.projectManager, this.importer, this.consoleWriter);
      var2.setBackupEnabled(this.doBackup);

      try {
         var2.deploy(var1);
      } catch (Throwable var8) {
         this.consoleWriter.log(var8);
         return false;
      } finally {
         var2.dispose();
      }

      this.consoleWriter.log("Done!");
      return true;
   }

   private JitterPackCollector createJitterPackCollector() {
      return this.directory != null
         ? new DirectoryScanner(this.directory, this.recursive, this.consoleWriter)
         : new SingleFileCollector(this.importer, this.individualFile, this.consoleWriter);
   }

   public void setIndividualFile(File var1) {
      this.individualFile = var1;
   }

   public void setDirectory(File var1) {
      this.directory = var1;
   }

   public void setRecursive(boolean var1) {
      this.recursive = var1;
   }

   public void setServer(String var1) {
      this.server = var1;
   }

   public void setPort(int var1) {
      this.port = var1;
   }

   public void setUser(String var1) {
      this.user = var1;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public void setPasswordHash(String var1) {
      this.passwordHash = var1;
   }

   public void setQuietFlag() {
      this.consoleWriter = ConsoleWriter.getWriter(true);
   }

   public void setBackupFlag() {
      this.doBackup = true;
   }
}
