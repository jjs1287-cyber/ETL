package org.etlforge.integration.data.entity.schedule;

import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.StringKeyValuePairs;

public class ScheduleDuration {
   public static final String DURATION_START_DATE_PROPERTY = "duration_start_date";
   public static final String DURATION_END_DATE_PROPERTY = "duration_end_date";
   public static final String DURATION_YESNO_END_PROPERTY = "duration_yesno_end";
   private String startDate;
   private String endDate;
   private boolean hasEndDate;

   public ScheduleDuration() {
   }

   public ScheduleDuration(StringKeyValuePairs var1) {
      this.startDate = var1.get("duration_start_date");
      this.endDate = var1.get("duration_end_date");

      try {
         int var2 = Integer.parseInt(var1.get("duration_yesno_end"));
         this.hasEndDate = var2 != 0;
      } catch (NumberFormatException var3) {
         this.hasEndDate = false;
      }
   }

   public void setDuration(Date var1, Date var2) {
      this.startDate = getDateString(var1);
      this.endDate = getDateString(var2);
      this.hasEndDate = var2 != null;
   }

   public Date getStartDate() {
      Date var1 = getDateFromString(this.startDate);
      return var1 == null ? new Date() : var1;
   }

   public Date getEndDate() {
      return getDateFromString(this.endDate);
   }

   public boolean hasEndDate() {
      return this.hasEndDate;
   }

   public void appendToData(StringKeyValuePairs var1) {
      var1.putString("duration_start_date", this.startDate == null ? "" : this.startDate);
      var1.putString("duration_end_date", this.endDate == null ? "" : this.endDate);
      var1.putString("duration_yesno_end", this.hasEndDate ? "1" : "0");
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
         ScheduleDuration var2 = (ScheduleDuration)var1;
         if (this.hasEndDate != var2.hasEndDate) {
            return false;
         }

         if (!StringUtils.equals(this.startDate, var2.startDate)) {
            return false;
         }

         if (this.hasEndDate) {
            return StringUtils.equals(this.endDate, var2.endDate);
         }
      }

      return false;
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + (this.hasEndDate ? 1 : 0);
      var1 = 31 * var1 + (this.startDate != null ? this.startDate.hashCode() : 0);
      return 31 * var1 + (this.endDate != null ? this.endDate.hashCode() : 0);
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("Starts at: ");
      if (this.startDate == null) {
         var1.append("[?]");
      } else {
         var1.append(this.startDate);
      }

      if (this.hasEndDate) {
         var1.append(" -- Ends at: ");
         if (this.endDate == null) {
            var1.append("[?]");
         } else {
            var1.append(this.endDate);
         }
      } else {
         var1.append(" -- Has no end date.");
      }

      return var1.toString();
   }

   public boolean isValid() {
      if (this.hasEndDate()) {
         if (this.endDate == null) {
            return false;
         }

         Date var1 = this.getStartDate();
         if (var1 != null) {
            Date var2 = this.getEndDate();
            if (var2 != null) {
               return !var1.after(var2);
            }
         }
      }

      return true;
   }

   private static String getDateString(Date var0) {
      return var0 == null ? null : ServerDateUtils.toServerFormat(var0);
   }

   private static Date getDateFromString(String var0) {
      if (var0 != null && var0.length() > 0) {
         try {
            return ServerDateUtils.fromServerFormat(var0);
         } catch (ParseException var2) {
            var2.printStackTrace();
         }
      }

      return null;
   }
}
