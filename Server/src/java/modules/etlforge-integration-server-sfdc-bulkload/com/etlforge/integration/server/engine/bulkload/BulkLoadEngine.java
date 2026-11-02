package com.etlforge.integration.server.engine.bulkload;

import com.etlforge.integration.server.engine.bulkload.batch.BatchProcessConfig;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.logging.Logger;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.engine.EngineSessionException;
import org.etlforge.integration.server.engine.EngineSessionFactory;
import org.etlforge.integration.server.engine.EngineSessionManager;
import org.etlforge.system.SystemUtils;

public final class BulkLoadEngine {
   private final EngineSessionManager<BulkLoadSession> sessions;
   private final Logger logger;

   public BulkLoadEngine() {
      this(Logger.getAnonymousLogger());
   }

   public BulkLoadEngine(Logger var1) {
      this.logger = var1;
      this.sessions = EngineSessionManager.newManager(new EngineSessionFactory<BulkLoadSession>() {
         public BulkLoadSession newSession(ExternalApiSupport var1) {
            return new BulkLoadSession(var1, BulkLoadEngine.this.logger);
         }
      });
      this.logStartup();
      this.configureTimeout();
      this.sessions.setLogger(var1);
   }

   private void logStartup() {
      String var1 = "Created new BulkLoadEngine." + SystemUtils.LINE_SEPARATOR + "Log level: " + this.logger.getLevel().getName();
      this.logger.info(var1);
      MemoryMXBean var2 = ManagementFactory.getMemoryMXBean();
      this.logger.info("Heap memory: " + var2.getHeapMemoryUsage());
   }

   public void configureTimeout() {
      int var1 = BulkLoadEngineConfig.getSessionTimeoutInMinutes();
      this.sessions.setSessionTimeout(var1);
   }

   public BulkLoadSession newSession(String var1, String var2) throws ServerAccessException {
      return this.sessions.newSession(var1, var2);
   }

   public void closeSession(BulkLoadSessionCredentials var1) throws EngineSessionException {
      this.sessions.closeSession(var1.sessionId, var1.userName, var1.md5Password);
   }

   public BulkLoadResult runBulkLoad(BulkLoadSessionCredentials var1, BulkLoadParams var2, BatchProcessConfig var3) throws EngineSessionException {
      BulkLoadSession var4 = this.getSession(var1);

      try {
         return new BulkLoadExecutor(var4, var2, var3).run();
      } catch (Exception var6) {
         throw new EngineSessionException(var6);
      }
   }

   private BulkLoadSession getSession(BulkLoadSessionCredentials var1) throws EngineSessionException {
      BulkLoadSession var2 = this.sessions.getSession(var1.sessionId, var1.userName, var1.md5Password);
      if (var2 == null) {
         String var3 = missingSessionErrorMessage(var1);
         throw new EngineSessionException(var3);
      } else {
         return var2;
      }
   }

   private static String missingSessionErrorMessage(BulkLoadSessionCredentials var0) {
      return "The BulkLoad engine was asked for a session that does not exist. This can happen if you restarted the ETLForge Tomcat Service while a Bulk Load operation was running, or if the session has timed out. If the latter, change the \"SessionTimeOut\" setting under the \"[BulkLoadEngine]\" section in etlforge.conf to configure the timeout. (You must restart the ETLForge Tomcat Service for the change to take effect.)\n\nAnother possibility is that the session has already been closed by the ETLForge Engine. \n\nThe requested session ID was: "
         + var0.sessionId
         + "\nThe user name requesting the session was: "
         + var0.userName;
   }
}
