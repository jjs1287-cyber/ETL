package org.etlforge.integration.salesforce;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Ticker;
import java.util.concurrent.TimeUnit;

public final class SessionTimeoutWindow {
   private final Ticker ticker;
   private final long startsAt;
   private final long expiresAt;
   private final long cushion;

   public static SessionTimeoutWindow infinite() {
      return new SessionTimeoutWindow(Ticker.systemTicker(), Long.MAX_VALUE);
   }

   public static SessionTimeoutWindow exipred() {
      Ticker var0 = Ticker.systemTicker();
      return new SessionTimeoutWindow(var0, var0.read() - 1L);
   }

   public SessionTimeoutWindow(int var1, int var2) {
      this(Ticker.systemTicker(), var1, var2);
   }

   @VisibleForTesting
   SessionTimeoutWindow(Ticker var1, int var2) {
      this(var1, var2, 0);
   }

   @VisibleForTesting
   SessionTimeoutWindow(Ticker var1, int var2, int var3) {
      this(var1, var1.read(), TimeUnit.SECONDS.toNanos(var2), TimeUnit.SECONDS.toNanos(var3));
   }

   private SessionTimeoutWindow(Ticker var1, long var2, long var4, long var6) {
      this.ticker = var1;
      this.startsAt = var2;
      this.expiresAt = var2 + var4 - var6;
      this.cushion = var6;
   }

   private SessionTimeoutWindow(Ticker var1, long var2) {
      this.ticker = var1;
      this.startsAt = var1.read();
      this.expiresAt = var2;
      this.cushion = 0L;
   }

   public SessionTimeoutWindow sessionAccessed() {
      this.checkAccessable();
      long var1 = this.ticker.read();
      long var3 = this.expiresAt - this.startsAt + this.cushion;
      long var5 = this.startsAt + var3 / 2L;
      return var1 < var5 ? this : new SessionTimeoutWindow(this.ticker, var1, var3, this.cushion);
   }

   private void checkAccessable() {
      if (this.expiresAt == Long.MAX_VALUE) {
         throw new UnsupportedOperationException("Calling sessionAccessed on an infinite SessionTimeoutWindow is a programming error.");
      }

      if (this.isExpired()) {
         throw new UnsupportedOperationException("Calling sessionAccessed on an expired SessionTimeoutWindow is a programming error.");
      }
   }

   public boolean isExpired() {
      long var1 = this.ticker.read();
      return var1 >= this.expiresAt;
   }

   @VisibleForTesting
   long secondsToExpiration() {
      return TimeUnit.NANOSECONDS.toSeconds(this.expiresAt - this.ticker.read());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.ticker, this.startsAt, this.expiresAt, this.cushion);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof SessionTimeoutWindow)) {
         return false;
      }

      SessionTimeoutWindow var2 = (SessionTimeoutWindow)var1;
      return this.ticker.equals(var2.ticker) && this.startsAt == var2.startsAt && this.expiresAt == var2.expiresAt && this.cushion == var2.cushion;
   }

   @Override
   public String toString() {
      if (!this.isExpired()) {
         String var1 = this.constructTimeLeftString();
         if (!this.isExpired()) {
            return var1;
         }
      }

      return "**Expired**";
   }

   private String constructTimeLeftString() {
      long var1 = this.secondsToExpiration();
      long var3 = var1 / 3600L;
      var1 -= var3 * 3600L;
      long var5 = var1 / 60L;
      var1 -= var5 * 60L;
      return String.format("Expires in [hh:mm:ss]: %02d:%02d:%02d", var3, var5, var1);
   }
}
