package org.etlforge.integration.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.integration.server.log.ServerLogBuilder;

public final class ServerDebugLog {
   public static final Logger LOG = createLogger();

   private static Logger createLogger() {
      ServerLogBuilder var0 = new ServerLogBuilder(ServerDebugLog.class);
      var0.useConfiguredLevel("JavaBackend", "DebugLogLevel", Level.OFF);
      return var0.createLogger();
   }

   private ServerDebugLog() {
   }
}
