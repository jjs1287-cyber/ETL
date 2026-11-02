package org.etlforge.integration.client.applications.deployer;

import org.etlforge.application.Application;
import org.etlforge.integration.client.info.BuildProperties;
import org.etlforge.integration.client.info.KeepPasswordsOnClientProperty;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.jitterpack.Importer;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.springframework.context.support.ClassPathXmlApplicationContext;

final class ApplicationInitializer {
   private final ServerManager serverManager;
   private final ProjectManager projectManager;
   private final Importer importer;

   public ApplicationInitializer(ConsoleWriter var1) {
      var1.log("Initializing the JitterPackDeployer");
      BuildProperties.init();
      this.setSystemProperties();
      this.loadAppContext();
      this.loadMetaData();
      this.serverManager = Application.getBean("ServerManager", ServerManager.class);
      this.projectManager = Application.getBean("ProjectManager", ProjectManager.class);
      this.importer = Application.getBean("Importer", Importer.class);
   }

   private void setSystemProperties() {
      System.setProperty("SystemOutToConsole", "true");
      System.setProperty("SystemErrToConsole", "true");
      KeepPasswordsOnClientProperty.set(true);
   }

   private void loadAppContext() {
      ClassPathXmlApplicationContext var1 = new ClassPathXmlApplicationContext("Deployer.AppContext.xml");
      Application.setAppContext(var1);
   }

   private void loadMetaData() {
      try {
         OperationType.load();
      } catch (Exception var2) {
         System.err.println(var2);
         System.exit(1);
      }
   }

   public ServerManager getServerManager() {
      return this.serverManager;
   }

   public ProjectManager getProjectManager() {
      return this.projectManager;
   }

   public Importer getImporter() {
      return this.importer;
   }
}
