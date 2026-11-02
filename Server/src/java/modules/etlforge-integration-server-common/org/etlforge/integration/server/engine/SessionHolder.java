package org.etlforge.integration.server.engine;

import java.util.concurrent.TimeUnit;

final class SessionHolder<S extends EngineSession> {
   public final S session;
   private long lastAccessed;

   public SessionHolder(S var1) {
      this.session = (S)var1;
      this.accessed();
   }

   public void accessed() {
      this.lastAccessed = System.currentTimeMillis();
   }

   public long ageInMinutes(long var1) {
      long var3 = var1 - this.lastAccessed;
      return TimeUnit.MINUTES.convert(var3, TimeUnit.MILLISECONDS);
   }

   public boolean isInactive() {
      return !this.session.isActive();
   }
}
