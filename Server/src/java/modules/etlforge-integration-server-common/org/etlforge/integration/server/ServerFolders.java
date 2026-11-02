package org.etlforge.integration.server;

import java.io.File;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.exception.ServerConfigurationException;

public final class ServerFolders {
   public static final File SERVER_HOME = getServerHome();
   public static final File JDBC_INSTALL_DIR = new File(SERVER_HOME, "Jdbc");

   private static File getServerHome() {
      try {
         return new File(ServerConfig.getServerHome());
      } catch (ServerConfigurationException var1) {
         throw new RuntimeException(var1);
      }
   }

   private ServerFolders() {
   }
}
