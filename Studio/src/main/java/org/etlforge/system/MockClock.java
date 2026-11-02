package org.etlforge.system;

public final class MockClock extends Clock {
   private volatile long time;

   public static MockClock now() {
      return new MockClock(System.currentTimeMillis());
   }

   public MockClock() {
      this(0L);
   }

   public MockClock(long var1) {
      this.time = var1;
   }

   public void set(long var1) {
      this.time = var1;
   }

   @Override
   public long currentTime() {
      return this.time;
   }
}
