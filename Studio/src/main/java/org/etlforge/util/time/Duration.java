package org.etlforge.util.time;

import com.google.common.primitives.Longs;
import java.util.Date;

public final class Duration implements Comparable<Duration> {
   private static final long SECONDS = 1000L;
   private static final long MINUTES = 60000L;
   private static final long HOURS = 3600000L;
   private static final long DAYS = 86400000L;
   private final long milliSeconds;

   public Duration(Date var1, Date var2) {
      this.milliSeconds = Math.abs(var2.getTime() - var1.getTime());
   }

   public long inMilliSeconds() {
      return this.milliSeconds;
   }

   public int millisOfStartedSecond() {
      return (int)(this.milliSeconds % 1000L);
   }

   public long inSeconds() {
      return this.milliSeconds / 1000L;
   }

   public int secondsOfStartedMinute() {
      long var1 = this.milliSeconds - 60000L * this.inMinutes();
      return (int)(var1 / 1000L);
   }

   public long inMinutes() {
      return this.milliSeconds / 60000L;
   }

   public int minutesOfStartedHour() {
      long var1 = this.milliSeconds - 3600000L * this.inHours();
      return (int)(var1 / 60000L);
   }

   public long inHours() {
      return this.milliSeconds / 3600000L;
   }

   public int hoursOfStartedDay() {
      long var1 = this.milliSeconds - 86400000L * this.inDays();
      return (int)(var1 / 3600000L);
   }

   public long inDays() {
      return this.milliSeconds / 86400000L;
   }

   public int compareTo(Duration var1) {
      return Longs.compare(this.milliSeconds, var1.milliSeconds);
   }

   public String toString(boolean var1) {
      StringBuilder var2 = new StringBuilder();
      this.appendDays(var2);
      this.appendHours(var2);
      this.appendMinutes(var2);
      this.appendSeconds(var2, var2.length() == 0 && !var1);
      if (var1) {
         this.appendMilliSeconds(var2);
      }

      return var2.toString();
   }

   @Override
   public String toString() {
      return this.toString(true);
   }

   private void appendDays(StringBuilder var1) {
      this.append(this.inDays(), var1, "day", "days");
   }

   private void appendHours(StringBuilder var1) {
      this.append(this.hoursOfStartedDay(), var1, "h", "h");
   }

   private void appendMinutes(StringBuilder var1) {
      this.append(this.minutesOfStartedHour(), var1, "min", "min");
   }

   private void appendSeconds(StringBuilder var1, boolean var2) {
      int var3 = this.secondsOfStartedMinute();
      if (var3 > 0) {
         this.append(this.secondsOfStartedMinute(), var1, "sec", "sec");
      } else if (var2) {
         var1.append("< 1 sec");
      }
   }

   private void appendMilliSeconds(StringBuilder var1) {
      this.append(this.millisOfStartedSecond(), var1, "ms", "ms");
   }

   private void append(long var1, StringBuilder var3, String var4, String var5) {
      if (var1 != 0L) {
         if (var3.length() > 0) {
            var3.append(" ");
         }

         if (var1 == 1L) {
            var3.append("1 ").append(var4);
         } else {
            var3.append(var1).append(" ").append(var5);
         }
      }
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof Duration && this.milliSeconds == ((Duration)var1).milliSeconds;
   }

   @Override
   public int hashCode() {
      return Longs.hashCode(this.milliSeconds);
   }
}
