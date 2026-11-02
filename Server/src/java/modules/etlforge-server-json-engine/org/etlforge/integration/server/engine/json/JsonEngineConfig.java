package org.etlforge.integration.server.engine.json;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.log.ServerLogBuilder;

public final class JsonEngineConfig {
   public static final String SECTION = "JsonEngine";
   private static final Level DEFAULT_LOG_LEVEL = Level.OFF;
   private static final String LOG_LEVEL_SETTING = "LogLevel";
   public static final Logger LOG = createLogger();

   private static Logger createLogger() {
      ServerLogBuilder var0 = new ServerLogBuilder(JsonEngine.class);
      var0.useConfiguredLevel("JsonEngine", "LogLevel", DEFAULT_LOG_LEVEL);
      return var0.createLogger();
   }

   public static void updateLogLevel() {
      LOG.setLevel(ServerLogBuilder.getLevelFromConfig("JsonEngine", "LogLevel", DEFAULT_LOG_LEVEL));
   }

   public static int getTimeOutInSeconds() {
      return ServerConfig.getOption("JsonEngine", "SessionTimeout", 300);
   }
}
