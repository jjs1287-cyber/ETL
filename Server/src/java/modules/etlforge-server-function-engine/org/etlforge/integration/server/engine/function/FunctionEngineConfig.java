package org.etlforge.integration.server.engine.function;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.log.ServerLogBuilder;

public final class FunctionEngineConfig {
   public static final String SECTION = "FunctionEngine";
   private static final Level DEFAULT_LOG_LEVEL = Level.OFF;
   private static final String LOG_LEVEL_SETTING = "LogLevel";
   public static final Logger LOG = createLogger();

   private static Logger createLogger() {
      ServerLogBuilder var0 = new ServerLogBuilder(FunctionEngine.class);
      var0.useConfiguredLevel("FunctionEngine", "LogLevel", DEFAULT_LOG_LEVEL);
      return var0.createLogger();
   }

   public static void updateLogLevel() {
      LOG.setLevel(ServerLogBuilder.getLevelFromConfig("FunctionEngine", "LogLevel", DEFAULT_LOG_LEVEL));
   }

   public static int getTimeOutInSeconds() {
      return ServerConfig.getOption("FunctionEngine", "SessionTimeout", 300);
   }
}
