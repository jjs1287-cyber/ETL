package org.etlforge.integration.data.entity.schedule;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.lang.Intervals;
import org.etlforge.util.time.DayOfTheWeek;

public final class ScheduleOccurrence {
   public static final int EVERY_X_DAYS_MIN = 1;
   public static final int EVERY_X_DAYS_MAX = 365;
   public static final int EVERY_X_WEEKS_MIN = 1;
   public static final int EVERY_X_WEEKS_MAX = 99;
   public static final String OCCURS_PROPERTY = "occurs";
   public static final String DAILY_PROPERTY = "daily";
   public static final String WEEKLY_PROPERTY = "weekly";
   public static final String MONTHLY_PROPERTY = "monthly";
   public static final String MONTHLY_A_DAY_PROPERTY = "monthly_a_day";
   public static final String MONTHLY_A_OF_EVERY_X_MONTH_PROPERTY = "monthly_a_of_every_x_month";
   public static final String MONTHLY_B_NUM_PROPERTY = "monthly_b_num";
   public static final String MONTHLY_B_DAY_PROPERTY = "monthly_b_day";
   public static final String MONTHLY_B_OF_EVERY_X_MONTH_PROPERTY = "monthly_b_of_every_x_month";
   public static final int DAILY = 1;
   public static final int WEEKLY = 2;
   public static final int MONTHLY = 3;
   private int type;
   private int every_X_Days;
   private int every_X_Weeks;
   private final boolean[] weekly_On_This_Day = new boolean[7];
   private int monthlyType;
   private int monthly_A_Day;
   private int monthly_A_of_Every_X_Month;
   private int monthly_B_Num;
   private DayOfTheWeek monthly_B_Day_Of_The_Week;
   private int monthly_B_of_Every_X_Month;

   public static String[] getWeekDayProperties() {
      return new String[]{"sun", "mon", "tue", "wed", "thu", "fri", "sat"};
   }

   public ScheduleOccurrence() {
   }

   public ScheduleOccurrence(StringKeyValuePairs var1) {
      this.setDetails(var1);
   }

   public int getType() {
      return this.type;
   }

   public int getMonthlyType() {
      return this.monthlyType;
   }

   public void setDailyOccurrence(int var1) {
      this.type = 1;
      this.every_X_Days = var1;
      this.every_X_Weeks = 1;
      Arrays.fill(this.weekly_On_This_Day, false);
      this.monthlyType = 1;
      this.monthly_A_Day = 1;
      this.monthly_A_of_Every_X_Month = 1;
      this.monthly_B_Num = 1;
      this.monthly_B_Day_Of_The_Week = null;
      this.monthly_B_of_Every_X_Month = 1;
   }

   public int get_Every_X_Days() {
      return this.every_X_Days;
   }

   public void setWeeklyOccurrence(int var1, DayOfTheWeek[] var2) {
      if (var2 == null) {
         var2 = new DayOfTheWeek[0];
      }

      this.type = 2;
      this.every_X_Weeks = var1;
      Arrays.fill(this.weekly_On_This_Day, false);

      for (DayOfTheWeek var6 : var2) {
         this.weekly_On_This_Day[var6.ordinal()] = true;
      }

      this.every_X_Days = 1;
      this.monthlyType = 1;
      this.monthly_A_Day = 1;
      this.monthly_A_of_Every_X_Month = 1;
      this.monthly_B_Num = 1;
      this.monthly_B_Day_Of_The_Week = null;
      this.monthly_B_of_Every_X_Month = 1;
   }

   public int get_Every_X_Weeks() {
      return this.every_X_Weeks;
   }

   public DayOfTheWeek[] get_Weekly_On_These_Days() {
      int var1 = 0;

      for (boolean var5 : this.weekly_On_This_Day) {
         if (var5) {
            var1++;
         }
      }

      DayOfTheWeek[] var9 = new DayOfTheWeek[var1];
      var1 = 0;
      int var10 = 0;

      for (DayOfTheWeek var7 : DayOfTheWeek.values()) {
         if (this.weekly_On_This_Day[var1]) {
            var9[var10++] = var7;
         }

         var1++;
      }

      return var9;
   }

   public void setMonthlyOccurrence(int var1, int var2) {
      this.type = 3;
      this.monthlyType = 0;
      this.monthly_A_Day = var1;
      this.monthly_A_of_Every_X_Month = var2;
      this.every_X_Days = 1;
      this.every_X_Weeks = 1;
      Arrays.fill(this.weekly_On_This_Day, false);
      this.monthly_B_Num = 1;
      this.monthly_B_Day_Of_The_Week = null;
      this.monthly_B_of_Every_X_Month = 1;
   }

   public int get_Monthly_Day_In_Month() {
      return this.monthly_A_Day;
   }

   public int get_Monthly_of_Every_X_Months() {
      return this.monthly_A_of_Every_X_Month;
   }

   public void setMonthlyOccurence(int var1, DayOfTheWeek var2, int var3) {
      Preconditions.checkNotNull(var2);
      this.type = 3;
      this.monthlyType = 1;
      this.monthly_B_Num = var1;
      this.monthly_B_Day_Of_The_Week = var2;
      this.monthly_B_of_Every_X_Month = var3;
      this.every_X_Days = 1;
      this.every_X_Weeks = 1;
      Arrays.fill(this.weekly_On_This_Day, false);
      this.monthly_A_Day = 1;
      this.monthly_A_of_Every_X_Month = 1;
   }

   public int get_Monthly_B_Num() {
      return this.monthly_B_Num;
   }

   public DayOfTheWeek get_Monthly_B_Day_Of_The_Week() {
      return this.monthly_B_Day_Of_The_Week;
   }

   public int get_Monthly_B_Every_X_Month() {
      return this.monthly_B_of_Every_X_Month;
   }

   public void appendToData(StringKeyValuePairs var1) {
      var1.putInt("occurs", this.type);
      var1.putInt("daily", this.every_X_Days);
      var1.putInt("weekly", this.every_X_Weeks);
      String[] var2 = getWeekDayProperties();

      for (int var3 = 0; var3 < 7; var3++) {
         var1.putString(var2[var3], this.weekly_On_This_Day[var3] ? "1" : "-1");
      }

      var1.putInt("monthly", this.monthlyType);
      var1.putInt("monthly_a_day", this.monthly_A_Day);
      var1.putInt("monthly_a_of_every_x_month", this.monthly_A_of_Every_X_Month);
      var1.putInt("monthly_b_num", this.monthly_B_Num);
      if (this.monthly_B_Day_Of_The_Week != null) {
         var1.putInt("monthly_b_day", this.monthly_B_Day_Of_The_Week.ordinal());
      } else {
         var1.putInt("monthly_b_day", -1);
      }

      var1.putInt("monthly_b_of_every_x_month", this.monthly_B_of_Every_X_Month);
   }

   private void setDetails(StringKeyValuePairs var1) {
      this.type = var1.getInt("occurs", 1);
      switch (this.type) {
         case 1:
            this.setDailyDetails(var1);
            break;
         case 2:
            this.setWeeklyDetails(var1);
            break;
         case 3:
            this.setMonthlyDetails(var1);
      }
   }

   private void setDailyDetails(StringKeyValuePairs var1) {
      int var2 = var1.getInt("daily", 1);
      this.setDailyOccurrence(var2);
   }

   private void setWeeklyDetails(StringKeyValuePairs var1) {
      this.every_X_Weeks = var1.getInt("weekly", 1);
      String[] var2 = getWeekDayProperties();

      for (int var3 = 0; var3 < 7; var3++) {
         try {
            this.weekly_On_This_Day[var3] = Integer.parseInt(var1.get(var2[var3])) > 0;
         } catch (NumberFormatException var5) {
            this.weekly_On_This_Day[var3] = false;
         }
      }

      this.every_X_Days = 1;
      this.monthlyType = 1;
      this.monthly_A_Day = 1;
      this.monthly_A_of_Every_X_Month = 1;
      this.monthly_B_Num = 1;
      this.monthly_B_Day_Of_The_Week = null;
      this.monthly_B_of_Every_X_Month = 1;
   }

   private void setMonthlyDetails(StringKeyValuePairs var1) {
      this.monthlyType = var1.getInt("monthly", 1);
      switch (this.monthlyType) {
         case 0:
            this.setMonthlyDetailsA(var1);
            break;
         case 1:
            this.setMonthlyDetailsB(var1);
      }
   }

   private void setMonthlyDetailsA(StringKeyValuePairs var1) {
      this.monthly_A_Day = var1.getInt("monthly_a_day", 1);
      this.monthly_A_of_Every_X_Month = var1.getInt("monthly_a_of_every_x_month", 1);
      this.every_X_Days = 1;
      this.every_X_Weeks = 1;
      Arrays.fill(this.weekly_On_This_Day, false);
      this.monthly_B_Num = 1;
      this.monthly_B_Day_Of_The_Week = null;
      this.monthly_B_of_Every_X_Month = 1;
   }

   private void setMonthlyDetailsB(StringKeyValuePairs var1) {
      this.monthly_B_Num = Intervals.valueWithin(var1.getInt("monthly_b_num", 1), 1, 5);

      try {
         int var2 = Integer.parseInt(var1.get("monthly_b_day"));
         if (var2 >= 0 && var2 < 7) {
            this.monthly_B_Day_Of_The_Week = DayOfTheWeek.values()[var2];
         } else {
            this.monthly_B_Day_Of_The_Week = DayOfTheWeek.Sunday;
         }
      } catch (NumberFormatException var3) {
         this.monthly_B_Day_Of_The_Week = DayOfTheWeek.Sunday;
      }

      this.monthly_B_of_Every_X_Month = var1.getInt("monthly_b_of_every_x_month", 1);
      this.every_X_Days = 1;
      this.every_X_Weeks = 1;
      Arrays.fill(this.weekly_On_This_Day, false);
      this.monthly_A_Day = 1;
      this.monthly_A_of_Every_X_Month = 1;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (var1 == null) {
         return false;
      }

      if (var1.getClass().equals(this.getClass())) {
         ScheduleOccurrence var2 = (ScheduleOccurrence)var1;
         if (this.type != var2.type) {
            return false;
         }

         if (this.every_X_Days != var2.every_X_Days) {
            return false;
         }

         if (this.every_X_Weeks != var2.every_X_Weeks) {
            return false;
         }

         for (int var3 = 0; var3 < 7; var3++) {
            if (this.weekly_On_This_Day[var3] != var2.weekly_On_This_Day[var3]) {
               return false;
            }
         }

         if (this.monthlyType != var2.monthlyType) {
            return false;
         } else if (this.monthly_A_of_Every_X_Month != var2.monthly_A_of_Every_X_Month) {
            return false;
         } else if (this.monthly_B_Num != var2.monthly_B_Num) {
            return false;
         } else {
            return this.monthly_B_Day_Of_The_Week != var2.monthly_B_Day_Of_The_Week
               ? false
               : this.monthly_B_of_Every_X_Month == var2.monthly_B_of_Every_X_Month;
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.type;
      var1 = 31 * var1 + this.every_X_Days;
      var1 = 31 * var1 + this.every_X_Weeks;

      for (int var2 = 0; var2 < 7; var2++) {
         var1 = 31 * var1 + (this.weekly_On_This_Day[var2] ? 1 : 0);
      }

      var1 = 31 * var1 + this.monthlyType;
      var1 = 31 * var1 + this.monthly_A_of_Every_X_Month;
      var1 = 31 * var1 + this.monthly_B_Num;
      var1 = 31 * var1 + (this.monthly_B_Day_Of_The_Week != null ? this.monthly_B_Day_Of_The_Week.hashCode() : 0);
      return 31 * var1 + this.monthly_B_of_Every_X_Month;
   }

   @Override
   public String toString() {
      if (this.type == 1) {
         return "Occurs every " + this.every_X_Days + " day(s)";
      }

      if (this.type == 2) {
         StringBuilder var4 = new StringBuilder("Occurs every ");
         var4.append(this.every_X_Weeks);
         var4.append(" Week(s), on: ");

         for (int var2 = 0; var2 < 7; var2++) {
            if (this.weekly_On_This_Day[var2]) {
               var4.append(DayOfTheWeek.values()[var2].toString());
               var4.append(" ");
            }
         }

         return var4.toString();
      } else {
         if (this.type == 3) {
            if (this.monthlyType == 0) {
               StringBuilder var3 = new StringBuilder("Occurs on day ");
               var3.append(this.monthly_A_Day);
               var3.append(" of every ");
               var3.append(this.monthly_A_of_Every_X_Month);
               var3.append(" month(s)");
               return var3.toString();
            }

            if (this.monthlyType == 1) {
               StringBuilder var1 = new StringBuilder("Occurs on the ");
               switch (this.monthly_B_Num) {
                  case 1:
                     var1.append("1st ");
                     break;
                  case 2:
                     var1.append("2nd ");
                     break;
                  case 3:
                     var1.append("3rd ");
                     break;
                  case 4:
                     var1.append("4th ");
                     break;
                  case 5:
                     var1.append("last ");
                     break;
                  default:
                     var1.append("[?] ");
               }

               var1.append(this.monthly_B_Day_Of_The_Week != null ? this.monthly_B_Day_Of_The_Week.toString() : "[?]");
               var1.append(" of every ");
               var1.append(this.monthly_B_of_Every_X_Month);
               var1.append(" month(s)");
               return var1.toString();
            }
         }

         return "Unknown type.";
      }
   }

   public boolean isValid() {
      switch (this.getType()) {
         case 1:
            return this.validateDailyOccurrence();
         case 2:
            return this.validateWeeklyOccurrence();
         case 3:
            return this.validateMontlyOccurrence();
         default:
            throw new RuntimeException("Unexpected occurrence type: " + this.getType());
      }
   }

   private boolean validateDailyOccurrence() {
      int var1 = this.get_Every_X_Days();
      return var1 >= 1 && var1 <= 365;
   }

   private boolean validateWeeklyOccurrence() {
      int var1 = this.get_Every_X_Weeks();
      if (var1 >= 1 && var1 <= 99) {
         DayOfTheWeek[] var2 = this.get_Weekly_On_These_Days();
         return var2 != null && var2.length != 0;
      } else {
         return false;
      }
   }

   private boolean validateMontlyOccurrence() {
      if (this.getMonthlyType() == 0) {
         int var3 = this.get_Monthly_Day_In_Month();
         if (var3 >= 1 && var3 <= 31) {
            int var4 = this.get_Monthly_of_Every_X_Months();
            return isValidMonth(var4);
         } else {
            return false;
         }
      } else {
         int var1 = this.get_Monthly_B_Num();
         if (var1 < 1 || var1 > 5) {
            return false;
         }

         if (this.get_Monthly_B_Day_Of_The_Week() == null) {
            return false;
         }

         int var2 = this.get_Monthly_B_Every_X_Month();
         return isValidMonth(var2);
      }
   }

   private static boolean isValidMonth(int var0) {
      return var0 >= 1 && var0 <= 12;
   }
}
