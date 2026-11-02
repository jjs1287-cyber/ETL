package org.etlforge.util.time;

import com.google.common.base.Preconditions;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {
   private static String[] WEEKDAY_NAMES;
   private static String[] WEEKDAY_SHORT_NAMES;

   public static synchronized String getWeekDayName(int var0) {
      if (WEEKDAY_NAMES == null) {
         DateFormatSymbols var1 = new DateFormatSymbols();
         WEEKDAY_NAMES = var1.getWeekdays();
      }

      return WEEKDAY_NAMES[var0];
   }

   public static synchronized String getWeekDayShortName(int var0) {
      if (WEEKDAY_SHORT_NAMES == null) {
         DateFormatSymbols var1 = new DateFormatSymbols();
         WEEKDAY_SHORT_NAMES = var1.getShortWeekdays();
      }

      return WEEKDAY_SHORT_NAMES[var0];
   }

   public static boolean isToday(Date var0) {
      Calendar var1 = cal(var0);
      Calendar var2 = Calendar.getInstance();
      return var2.get(1) == var1.get(1) && var2.get(6) == var1.get(6);
   }

   public static boolean isYesterday(Date var0) {
      return var0.before(new Date()) && getDaysAgo(var0) == 1;
   }

   public static boolean isTomorrow(Date var0) {
      return System.currentTimeMillis() >= var0.getTime() ? false : getDaysBetween(new Date(), var0) == 1;
   }

   public static int getDaysAgo(Date var0) {
      return getDaysBetween(var0, new Date());
   }

   public static int getDaysBetween(Date var0, Date var1) {
      Preconditions.checkArgument(!var0.after(var1), "earlier is after later");
      Calendar var2 = cal(var0);
      Calendar var3 = cal(var1);
      int var4 = var2.get(1);
      int var5 = var3.get(1);
      if (var4 == var5) {
         return var3.get(6) - var2.get(6);
      }

      int var6 = 0;

      for (int var7 = var4; var7 < var5; var7++) {
         Calendar var8 = Calendar.getInstance();
         var8.set(1, var7);
         int var9 = var8.getActualMaximum(6);
         if (var7 == var4) {
            var6 += var9 - var2.get(6);
         } else {
            var6 += var9;
         }
      }

      return var6 + var3.get(6);
   }

   public static Calendar cal(Date var0) {
      Calendar var1 = Calendar.getInstance();
      var1.setTime(var0);
      return var1;
   }

   private DateUtils() {
   }
}
