package org.etlforge.integration.server.engine;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

final class SessionTimeoutManager<S extends EngineSession> {
   private final Object lock;
   private final Map<EngineSessionId<S>, SessionHolder<S>> sessions;
   private volatile boolean stopped;
   private volatile int sessionTimeoutInMinutes;
   private volatile Logger logger = Logger.getAnonymousLogger();

   public SessionTimeoutManager(Map<EngineSessionId<S>, SessionHolder<S>> var1, int var2, Object var3) {
      this.sessions = var1;
      this.lock = var3;
      this.sessionTimeoutInMinutes = var2;
   }

   public void setLogger(Logger var1) {
      this.logger = var1;
   }

   public void changeTimeout(int var1) {
      this.sessionTimeoutInMinutes = var1;
   }

   public void stop() {
      this.stopped = true;
   }

   public void start() {
      Thread var1 = new Thread(new Runnable() {
         @Override
         public void run() {
            while (!SessionTimeoutManager.this.stopped) {
               try {
                  TimeUnit.MINUTES.sleep(5L);
                  SessionTimeoutManager.this.checkTimeout();
               } catch (Throwable var2) {
                  SessionTimeoutManager.this.logger.info("An error occurred in the timeout checker: " + var2.getMessage());
               }
            }
         }
      });
      var1.setDaemon(true);
      var1.start();
   }

   private void checkTimeout() {
      this.logger.fine("Checking timed out sessions");
      long var1 = System.currentTimeMillis();
      synchronized (this.lock) {
         ArrayList var4 = Lists.newArrayList();

         for (SessionHolder var6 : this.sessions.values()) {
            if (this.isTooOld(var1, var6)) {
               var4.add(var6);
            }
         }

         for (Object __etlItem64 : var4) {
            SessionHolder var10 = (SessionHolder) __etlItem64;
            this.sessions.remove(var10.session.getId());
            var10.session.dispose();
            this.logger
               .warning(
                  "Session ["
                     + var10.session.getId()
                     + "] timed out and was closed. This might indicate a problem in the server backend, since sessions should be closed explicitly during normal operations."
               );
         }
      }
   }

   private boolean isTooOld(long var1, SessionHolder var3) {
      return var3.isInactive() && var3.ageInMinutes(var1) > this.sessionTimeoutInMinutes;
   }
}
