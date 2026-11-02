package org.etlforge.integration.server.engine;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.logging.Logger;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;

public final class EngineSessionManager<S extends EngineSession<S>> {
   private final Object lock = new Object();
   private final Map<EngineSessionId<S>, SessionHolder<S>> sessions;
   private final EngineSessionFactory<S> sessionFactory;
   private SessionTimeoutManager<S> timeoutManager;
   private Logger logger;

   public static <E extends EngineSession<E>> EngineSessionManager<E> newManager(EngineSessionFactory<E> var0) {
      return new EngineSessionManager<>(var0);
   }

   public EngineSessionManager(EngineSessionFactory<S> var1) {
      this.sessionFactory = var1;
      this.sessions = Maps.newHashMap();
      this.logger = Logger.getAnonymousLogger();
      this.addShutdownHook();
   }

   private void addShutdownHook() {
      Runtime var1 = Runtime.getRuntime();
      var1.addShutdownHook(new EngineSessionManager.ShutdownHook());
   }

   public void setLogger(Logger var1) {
      synchronized (this.lock) {
         this.logger = var1;
         if (this.timeoutManager != null) {
            this.timeoutManager.setLogger(var1);
         }
      }
   }

   public void setSessionTimeout(int var1) {
      synchronized (this.lock) {
         if (var1 > 0) {
            if (this.timeoutManager == null) {
               this.timeoutManager = new SessionTimeoutManager<>(this.sessions, var1, this.lock);
               this.timeoutManager.setLogger(this.logger);
               this.timeoutManager.start();
            } else {
               this.timeoutManager.changeTimeout(var1);
            }
         } else if (this.timeoutManager != null) {
            this.timeoutManager.stop();
            this.timeoutManager = null;
         }
      }
   }

   public S newSession(String var1, String var2) throws ServerAccessException {
      ExternalApiSupport var3 = ExternalApiSupport.forCall(var1, var2);
      EngineSession var4 = this.sessionFactory.newSession(var3);
      this.logger.fine("New session [" + var4.getId() + "] created by user " + var1);
      synchronized (this.lock) {
         this.sessions.put(var4.getId(), new SessionHolder<>((S)var4));
         return (S)var4;
      }
   }

   public S getSession(EngineSessionId<S> var1, String var2, String var3) throws EngineSessionException {
      synchronized (this.lock) {
         SessionHolder<S> var5 = this.sessions.get(var1);
         if (var5 != null) {
            var5.session.verifyCredentials(var2, var3);
            var5.accessed();
            return var5.session;
         } else {
            return null;
         }
      }
   }

   public S closeSession(EngineSessionId<S> var1, String var2, String var3) throws EngineSessionException {
      this.logger.fine("Close session requested. Session ID: " + var1 + ". User: " + var2);
      EngineSession var4 = null;
      synchronized (this.lock) {
         var4 = this.getSession(var1, var2, var3);
         if (var4 != null) {
            this.sessions.remove(var1);
         }
      }

      if (var4 != null) {
         var4.dispose();
         this.logger.fine("Session [" + var4.getId() + "] was closed.");
      } else {
         this.logger.warning("The requested session " + var1 + " was not found.");
      }

      return (S)var4;
   }

   private final class ShutdownHook extends Thread {
      private ShutdownHook() {
      }

      @Override
      public void run() {
         try {
            EngineSessionManager.this.logger.info("Disposing all open sessions.");
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         synchronized (EngineSessionManager.this.lock) {
            this.disposeSessions();
         }
      }

      private void disposeSessions() {
         for (SessionHolder var2 : EngineSessionManager.this.sessions.values()) {
            try {
               var2.session.dispose();
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }
      }
   }
}
