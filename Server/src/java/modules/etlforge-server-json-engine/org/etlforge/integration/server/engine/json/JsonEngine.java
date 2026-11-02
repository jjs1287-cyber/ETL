package org.etlforge.integration.server.engine.json;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.logging.Logger;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.engine.EngineSessionException;
import org.etlforge.integration.server.engine.EngineSessionFactory;
import org.etlforge.integration.server.engine.EngineSessionManager;
import org.etlforge.system.SystemUtils;

public final class JsonEngine {
   private final EngineSessionManager<JsonSession> sessions;
   private final Logger logger;

   public JsonEngine() {
      this(Logger.getAnonymousLogger());
   }

   public JsonEngine(Logger var1) {
      this.logger = var1;
      this.sessions = EngineSessionManager.newManager(new EngineSessionFactory<JsonSession>() {
         public JsonSession newSession(ExternalApiSupport var1) {
            return new JsonSession(var1, JsonEngine.this.logger);
         }
      });
      this.logStartup();
      this.configureTimeout();
      this.sessions.setLogger(var1);
   }

   private void logStartup() {
      String var1 = "Created new JsonEngine." + SystemUtils.LINE_SEPARATOR + "Log level: " + this.logger.getLevel().getName();
      this.logger.info(var1);
      MemoryMXBean var2 = ManagementFactory.getMemoryMXBean();
      this.logger.info("Heap memory: " + var2.getHeapMemoryUsage());
   }

   public void configureTimeout() {
      int var1 = JsonEngineConfig.getTimeOutInSeconds();
      this.sessions.setSessionTimeout(var1);
   }

   public JsonSession newSession(String var1, String var2) throws ServerAccessException {
      return this.sessions.newSession(var1, var2);
   }

   public void closeSession(JsonSessionCredentials var1) throws EngineSessionException {
      this.sessions.closeSession(var1.getSessionId(), var1.getUserName(), var1.getMd5Pwd());
   }

   public void jsonToXml(JsonSessionCredentials var1, ConversionParams var2) throws Exception {
      JsonSession var3 = this.getSession(var1);

      try {
         DbXsdLocator var4 = this.createXsdLocator(var3);
         new JsonToXmlExecutor(var3, var2, var4).run();
      } catch (Exception var5) {
         throw new EngineSessionException(var5);
      }
   }

   private DbXsdLocator createXsdLocator(JsonSession var1) throws ServerAccessException {
      return new DbXsdLocator(var1.getBackendConnection());
   }

   private JsonSession getSession(JsonSessionCredentials var1) throws EngineSessionException {
      JsonSession var2 = this.sessions.getSession(var1.getSessionId(), var1.getUserName(), var1.getMd5Pwd());
      if (var2 == null) {
         String var3 = missingSessionErrorMessage(var1);
         throw new EngineSessionException(var3);
      } else {
         return var2;
      }
   }

   private static String missingSessionErrorMessage(JsonSessionCredentials var0) {
      return "The JSON engine was asked for a session that does not exist. This can happen if you restarted the ETLForge Tomcat Service while a JSON operation was running, or if the session has timed out. If the latter, change the \"SessionTimeOut\" setting under the \"[JsonEngine]\" section in etlforge.conf to configure the timeout. (You must restart the ETLForge Tomcat Service for the change to take effect.)\n\nAnother possibility is that the session has already been closed by the ETLForge Engine. \n\nThe requested session ID was: "
         + var0.getSessionId()
         + "\nThe user name requesting the session was: "
         + var0.getUserName();
   }

   public void xmlToJson(JsonSessionCredentials var1, ConversionParams var2) throws Exception {
      JsonSession var3 = this.getSession(var1);

      try {
         DbXsdLocator var4 = this.createXsdLocator(var3);
         new XmlToJsonExecutor(var3, var2, var4).run();
      } catch (Exception var5) {
         throw new EngineSessionException(var5);
      }
   }
}
