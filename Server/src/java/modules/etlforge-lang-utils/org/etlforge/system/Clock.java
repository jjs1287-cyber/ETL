package org.etlforge.system;

public abstract class Clock {
   public abstract long currentTime();

   public static final Clock systemClock() {
      return new Clock() {
         @Override
         public long currentTime() {
            return System.currentTimeMillis();
         }

         @Override
         public String toString() {
            return "System Time";
         }
      };
   }
}
