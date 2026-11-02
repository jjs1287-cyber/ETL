package org.etlforge.integration.server.engine.jdbc;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.sql.SQLException;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.conf.ServerConfigListener;
import org.etlforge.integration.server.db.ExternalDatabaseException;
import org.etlforge.integration.server.db.ServerDbException;
import org.etlforge.integration.server.db.lookup.DbExecuteResult;
import org.etlforge.integration.server.db.lookup.DbLookupResult;
import org.etlforge.integration.server.db.source.ReadSourceParams;
import org.etlforge.integration.server.db.storedproc.SPResult;
import org.etlforge.integration.server.db.target.WriteTargetParams;
import org.etlforge.integration.server.engine.EngineSessionException;
import org.etlforge.integration.server.engine.EngineSessionManager;
import org.etlforge.system.SystemUtils;

public final class JdbcEngine {
   private final EngineSessionManager<JdbcSession> sessions;
   private final boolean checkAccess = false;

   public static JdbcEngine getEngine() {
      return JdbcEngine.EngineHolder.INSTANCE;
   }

   private JdbcEngine() {
      this.sessions = EngineSessionManager.newManager(new JdbcSessionFactory());
      this.sessions.setLogger(JdbcEngineLog.LOG);
      this.logStartup();
      this.configureTimeout();
      ServerConfig.addConfigurationListener(new JdbcEngine.ConfigurationListener());
   }

   private void logStartup() {
      String var1 = "Created new JdbcEngine." + SystemUtils.LINE_SEPARATOR + "Log level: " + JdbcEngineLog.LOG.getLevel().getName();
      JdbcEngineLog.LOG.info(var1);
      MemoryMXBean var2 = ManagementFactory.getMemoryMXBean();
      JdbcEngineLog.LOG.info("Heap memory: " + var2.getHeapMemoryUsage());
   }

   private void configureTimeout() {
      int var1 = this.readSessionTimeout();
      this.sessions.setSessionTimeout(var1);
   }

   private int readSessionTimeout() {
      return ServerConfig.getOption("JdbcEngine", "SessionTimeout", 60);
   }

   public JdbcSession newSession(String var1, String var2) throws ServerAccessException {
      JdbcSession var3 = this.sessions.newSession(var1, var2);
      var3.setCheckAccess(this.checkAccess);
      return var3;
   }

   public void readSource(JdbcSessionCredentials var1, ReadSourceParams var2) throws EngineSessionException, ServerAccessException, ExternalDatabaseException {
      JdbcSession var3 = this.getSession(var1);

      try {
         new ReadSourceExecutor(var3, var2).run();
      } catch (ServerDbException var5) {
         throw new EngineSessionException(var5);
      }
   }

   public void writeTarget(JdbcSessionCredentials var1, WriteTargetParams var2, StringBuilder var3) throws EngineSessionException, ServerAccessException, ExternalDatabaseException, SQLException {
      try {
         JdbcSession var4 = this.getSession(var1);
         new WriteTargetExecutor(var4, var2).run();
      } catch (ServerDbException var5) {
         throw new EngineSessionException(var5);
      }
   }

   public DbLookupResult dbLookup(JdbcSessionCredentials var1, IntegrationEntityId var2, ConnectionParams var3, String var4, boolean var5) throws EngineSessionException, ServerAccessException, ServerDbException, SQLException {
      JdbcSession var6 = this.getSession(var1);
      return new DbLookupExecutor(var6, var2, var3, var4, var5).run();
   }

   public DbLookupResult cachedDbLookup(JdbcSessionCredentials var1, IntegrationEntityId var2, ConnectionParams var3, String var4, boolean var5) throws EngineSessionException, ServerAccessException, ServerDbException, SQLException {
      JdbcSession var6 = this.getSession(var1);
      return new CachedDbLookupExecutor(var6, var2, var3, var4, var5).run();
   }

   public DbExecuteResult dbExecute(JdbcSessionCredentials var1, IntegrationEntityId var2, ConnectionParams var3, String var4, int var5, boolean var6) throws EngineSessionException, ServerAccessException, ServerDbException, SQLException {
      JdbcSession var7 = this.getSession(var1);
      return new DbExecuteExecutor(var7, var2, var3, var4, var6, var5).run();
   }

   public SPResult callStoredProcedure(
      JdbcSessionCredentials var1, IntegrationEntityId var2, ConnectionParams var3, String var4, String[] var5, int var6, boolean var7
   ) throws EngineSessionException, ServerAccessException, ServerDbException, SQLException {
      JdbcSession var8 = this.getSession(var1);
      return new CallStoredProcedureExecutor(var8, var2, var3, var4, var5, var7, var6).run();
   }

   public JdbcSession getSession(JdbcSessionCredentials var1) throws EngineSessionException {
      JdbcSession var2 = this.sessions.getSession(var1.sessionId, var1.userName, var1.md5Password);
      if (var2 == null) {
         String var3 = missingSessionErrorMessage(var1);
         throw new EngineSessionException(var3);
      } else {
         return var2;
      }
   }

   private static String missingSessionErrorMessage(JdbcSessionCredentials var0) {
      return "The JDBC engine was asked for a session that does not exist. This can happen if you restarted the ETLForge Tomcat Service while a JDBC operation was running, or if the session has timed out. If the latter, change the \"SessionTimeOut\" setting under the \"[JdbcEngine]\" section in etlforge.conf to configure the timeout. (You must restart the ETLForge Tomcat Service for the change to take effect.)\n\nAnother possibility is that the session has already been closed by the ETLForge Engine. \n\nThe requested session ID was: "
         + var0.sessionId
         + "\nThe user name requesting the session was: "
         + var0.userName;
   }

   public void closeSession(JdbcSessionId var1, String var2, String var3) throws EngineSessionException {
      this.sessions.closeSession(var1, var2, var3);
   }

   private final class ConfigurationListener implements ServerConfigListener {
      private ConfigurationListener() {
      }

      @Override
      public void configReloaded() {
         JdbcEngineLog.LOG.info("The server configuration was reloaded.");
         JdbcEngineLog.updateLogLevel();
         JdbcEngine.this.configureTimeout();
      }
   }

   private static class EngineHolder {
      private static final JdbcEngine INSTANCE = new JdbcEngine();
   }
}
