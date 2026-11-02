package com.etlforge.integration.server.engine.bulkload;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.server.conf.ServerConfig;

public final class BulkLoadEngineConfig {
   public static final String SECTION = "BulkLoadEngine";

   public static int getSessionTimeoutInMinutes() {
      return ServerConfig.getOption("BulkLoadEngine", "SessionTimeout", 300);
   }

   public static BulkLoadEndpoint getEndpointOverride() {
      String var0 = ServerConfig.getOption("BulkLoadEngine", "Endpoint", null);
      return StringUtils.isEmpty(var0) ? null : new TestEndpoint(var0);
   }

   private BulkLoadEngineConfig() {
   }
}
