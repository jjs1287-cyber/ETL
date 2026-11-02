package org.etlforge.integration.server.engine.jdbc;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.integration.server.log.ServerLogBuilder;

public final class JdbcEngineLog {
   private static final Level DEFAULT_LEVEL = Level.OFF;
   private static final String SECTION = "JdbcEngine";
   private static final String SETTING = "LogLevel";
   public static final Logger LOG = createLogger();

   private static Logger createLogger() {
      ServerLogBuilder var0 = new ServerLogBuilder(JdbcEngine.class);
      var0.useConfiguredLevel("JdbcEngine", "LogLevel", DEFAULT_LEVEL);
      return var0.createLogger();
   }

   public static void updateLogLevel() {
      LOG.setLevel(ServerLogBuilder.getLevelFromConfig("JdbcEngine", "LogLevel", DEFAULT_LEVEL));
   }

   private JdbcEngineLog() {
   }
}
