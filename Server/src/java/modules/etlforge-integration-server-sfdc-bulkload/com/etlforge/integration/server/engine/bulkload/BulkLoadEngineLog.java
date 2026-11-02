package com.etlforge.integration.server.engine.bulkload;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.integration.server.log.ServerLogBuilder;

public final class BulkLoadEngineLog {
   private static final Level DEFAULT_LEVEL = Level.OFF;
   private static final String SETTING = "LogLevel";
   public static final Logger LOG = createLogger();

   private static Logger createLogger() {
      ServerLogBuilder var0 = new ServerLogBuilder(BulkLoadEngine.class);
      var0.useConfiguredLevel("BulkLoadEngine", "LogLevel", DEFAULT_LEVEL);
      return var0.createLogger();
   }

   public static void updateLogLevel() {
      LOG.setLevel(ServerLogBuilder.getLevelFromConfig("BulkLoadEngine", "LogLevel", DEFAULT_LEVEL));
   }

   private BulkLoadEngineLog() {
   }
}
