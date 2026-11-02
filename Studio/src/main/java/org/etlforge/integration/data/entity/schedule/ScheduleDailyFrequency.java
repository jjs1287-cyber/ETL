package org.etlforge.integration.data.entity.schedule;

import com.google.common.base.Preconditions;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.StringKeyValuePairs;

public final class ScheduleDailyFrequency {
   public static final String FREQUENCY_PROPERTY = "frequency";
   public static final String FREQUENCY_ONCE_TIME_PROPERTY = "frequency_once_time";
   public static final String FREQUENCY_EVERY_NUM = "frequency_every_num";
   public static final String FREQUENCY_EVERY_HMS = "frequency_every_hm";
   public static final String FREQUENCY_START_TIME = "frequency_start_time";
   public static final String FREQUENCY_END_TIME = "frequency_end_time";
   public static final int OCCURS_EVERY_X = 0;
   public static final int OCCURS_ONCE_AT = 1;
   static int MAGIC_VALUE = 46908;
   private int frequencyType;
   private String frequency_Once_Time;
   private int frequency_Every_Num;
   private OccursEvery frequency_Every_HMS = OccursEvery.MINUTE;
   private String frequency_Start_Time;
   private String frequency_End_Time;

   public static boolean isDefiningSubMinuteResolution(StringKeyValuePairs var0) {
      int var1 = var0.getInt("frequency", -1);
      if (var1 == 0) {
         int var2 = var0.getInt("frequency_every_hm", -1);
         return var2 == OccursEvery.SECOND.getCode();
      } else {
         return false;
      }
   }

   public ScheduleDailyFrequency() {
   }

   public ScheduleDailyFrequency(StringKeyValuePairs var1) {
      this.setDetails(var1);
   }

   public int getFrequencyType() {
      return this.frequencyType;
   }

   public void setOccursOnce(Date var1) {
      Preconditions.checkNotNull(var1, "time");
      this.frequency_Once_Time = ServerTimeUtils.toServerFormat(var1);
      this.frequencyType = 1;
   }

   public Date getOccursOnceTime() {
      return getTimeFromString(this.frequency_Once_Time);
   }

   public void setOccursEvery(int var1, OccursEvery var2, Date var3, Date var4) {
      Preconditions.checkNotNull(var2, "everyHMS");
      Preconditions.checkNotNull(var3, "startTime");
      Preconditions.checkNotNull(var4, "endTime");
      this.frequencyType = 0;
      this.frequency_Every_Num = var1;
      this.frequency_Every_HMS = var2;
      this.frequency_Start_Time = ServerTimeUtils.toServerFormat(var3);
      this.frequency_End_Time = ServerTimeUtils.toServerFormat(var4);
   }

   public int get_Every_Num() {
      return this.frequency_Every_Num;
   }

   public OccursEvery get_Every_HMS() {
      return this.frequency_Every_HMS;
   }

   public Date getStartTime() {
      return getTimeFromString(this.frequency_Start_Time);
   }

   public Date getEndTime() {
      return getTimeFromString(this.frequency_End_Time);
   }

   public void appendToData(StringKeyValuePairs var1) {
      var1.putInt("frequency", this.frequencyType);
      if (this.frequency_Once_Time != null) {
         var1.putString("frequency_once_time", this.frequency_Once_Time);
      } else {
         var1.putString("frequency_once_time", "");
      }

      var1.putInt("frequency_every_num", this.frequency_Every_Num);
      var1.putInt("frequency_every_hm", this.frequency_Every_HMS.getCode());
      if (this.frequency_Start_Time != null) {
         var1.putString("frequency_start_time", this.frequency_Start_Time);
      } else {
         var1.putString("frequency_start_time", "");
      }

      if (this.frequency_End_Time != null) {
         var1.putString("frequency_end_time", this.frequency_End_Time);
      } else {
         var1.putString("frequency_end_time", "");
      }
   }

   private void setDetails(StringKeyValuePairs var1) {
      this.frequencyType = var1.getInt("frequency", 1);
      switch (this.frequencyType) {
         case 0:
            this.setOccursEveryDetails(var1);
            break;
         case 1:
            this.setOccursOnceAtDetails(var1);
      }
   }

   private void setOccursEveryDetails(StringKeyValuePairs var1) {
      try {
         int var2 = Integer.parseInt(var1.get("frequency_every_hm"));
         this.frequency_Every_HMS = OccursEvery.fromCode(var2);
      } catch (Exception var3) {
         this.frequency_Every_HMS = OccursEvery.MINUTE;
      }

      this.frequency_Every_Num = var1.getInt("frequency_every_num", 1);
      this.frequency_Start_Time = var1.get("frequency_start_time");
      this.frequency_End_Time = var1.get("frequency_end_time");
   }

   private static Date getTimeFromString(String var0) {
      if (var0 != null && var0.length() > 0) {
         try {
            return ServerTimeUtils.fromServerFormat(var0);
         } catch (ParseException var2) {
            var2.printStackTrace();
         }
      }

      return null;
   }

   private void setOccursOnceAtDetails(StringKeyValuePairs var1) {
      this.frequency_Once_Time = var1.get("frequency_once_time");
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
         ScheduleDailyFrequency var2 = (ScheduleDailyFrequency)var1;
         if (this.frequencyType != var2.frequencyType) {
            return false;
         } else if (this.frequency_Every_Num != var2.frequency_Every_Num) {
            return false;
         } else if (this.frequency_Every_HMS != var2.frequency_Every_HMS) {
            return false;
         } else if (!StringUtils.equals(this.frequency_Once_Time, var2.frequency_Once_Time)) {
            return false;
         } else {
            return !StringUtils.equals(this.frequency_Start_Time, var2.frequency_Start_Time)
               ? false
               : StringUtils.equals(this.frequency_End_Time, var2.frequency_End_Time);
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.frequencyType;
      var1 = 31 * var1 + (this.frequency_Once_Time != null ? this.frequency_Once_Time.hashCode() : 0);
      var1 = 31 * var1 + this.frequency_Every_Num;
      var1 = 31 * var1 + this.frequency_Every_HMS.hashCode();
      var1 = 31 * var1 + (this.frequency_Start_Time != null ? this.frequency_Start_Time.hashCode() : 0);
      return 31 * var1 + (this.frequency_End_Time != null ? this.frequency_End_Time.hashCode() : 0);
   }

   @Override
   public String toString() {
      if (this.frequencyType == 1) {
         return "Occurs once at: " + (this.frequency_Once_Time != null ? this.frequency_Once_Time : "[?]");
      }

      StringBuilder var1 = new StringBuilder("Occurs every ");
      var1.append(this.frequency_Every_Num).append(" ").append(this.frequency_Every_HMS);
      var1.append(" Starting at: ").append(this.frequency_Start_Time != null ? this.frequency_Start_Time : "[?]");
      var1.append(" Ending at: ").append(this.frequency_End_Time != null ? this.frequency_End_Time : "[?]");
      return var1.toString();
   }

   public boolean isValid() {
      if (this.frequencyType == 0) {
         int var1 = this.get_Every_Num();
         if (var1 == MAGIC_VALUE) {
            return true;
         }

         OccursEvery var2 = this.get_Every_HMS();
         return var1 >= 1 && var1 <= var2.getMaxAllowedValue();
      } else {
         return true;
      }
   }
}
