package org.etlforge.integration.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.conf.ServerConfigListener;
import org.etlforge.integration.server.log.ServerLogBuilder;

public final class ServerUiBackendLog {
   private static final Level DEFAULT_LEVEL = Level.OFF;
   private static final String SECTION = "JavaUiBackend";
   private static final String SETTING = "LogLevel";
   public static final Logger LOG = createLogger();

   private static Logger createLogger() {
      ServerLogBuilder var0 = new ServerLogBuilder(ServerUiBackendLog.class);
      var0.useConfiguredLevel("JavaUiBackend", "LogLevel", DEFAULT_LEVEL);
      return var0.createLogger();
   }

   private ServerUiBackendLog() {
   }

   static {
      ServerConfig.addConfigurationListener(new ServerUiBackendLog.Listener());
   }

   private static final class Listener implements ServerConfigListener {
      private Listener() {
      }

      @Override
      public void configReloaded() {
         ServerUiBackendLog.LOG.setLevel(ServerLogBuilder.getLevelFromConfig("JavaUiBackend", "LogLevel", ServerUiBackendLog.DEFAULT_LEVEL));
      }
   }
}
