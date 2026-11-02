package org.etlforge.integration.salesforce;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.etlforge.integration.salesforce.bulkload.TimeZoneId;
import org.etlforge.lang.Objects;

public final class DateConversionParams {
   public static final String US_DATE_FORMAT = "MM/dd/yyyy";
   private static final String EUROPEAN_DATE_FORMAT = "dd/MM/yyyy";
   public static final String US_DATE_TIME_FORMAT = defaultDateTimeFormat("MM/dd/yyyy");
   public final TimeZoneId timeZoneId;
   public final String dateFormat;
   public final String dateTimeFormat;

   private static String defaultDateTimeFormat(String var0) {
      return var0 + " HH:mm:ss";
   }

   public static DateConversionParams europeanFormat(TimeZoneId var0) {
      return new DateConversionParams(var0, "dd/MM/yyyy");
   }

   public static DateConversionParams usFormat(TimeZoneId var0) {
      return new DateConversionParams(var0, "MM/dd/yyyy");
   }

   private DateConversionParams(TimeZoneId var1, String var2) {
      this(var1, var2, defaultDateTimeFormat(var2));
   }

   public DateConversionParams(TimeZoneId var1, String var2, String var3) {
      this.timeZoneId = var1;
      this.dateFormat = var2;
      this.dateTimeFormat = var3;
   }

   public boolean isEuropeanFormat() {
      return "dd/MM/yyyy".equals(this.dateFormat);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.timeZoneId, this.dateFormat, this.dateTimeFormat);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof DateConversionParams)) {
         return false;
      }

      DateConversionParams var2 = (DateConversionParams)var1;
      return this.timeZoneId.equals(var2.timeZoneId)
         && Objects.equal(this.dateFormat, var2.dateFormat)
         && Objects.equal(this.dateTimeFormat, var2.dateTimeFormat);
   }

   @Override
   public String toString() {
      return String.format("Time Zone: %s, Date Fmt: %s, Date Time Fmt: %s", this.timeZoneId, this.dateFormat, this.dateTimeFormat);
   }

   public TimeZone getTimeZone() {
      return this.timeZoneId.getTimeZone();
   }

   public DateFormat getDateFormat() {
      return this.createDateFormat(this.dateFormat);
   }

   public DateFormat getDateTimeFormat() {
      return this.createDateFormat(this.dateTimeFormat);
   }

   private DateFormat createDateFormat(String var1) {
      if (var1 == null) {
         return null;
      }

      SimpleDateFormat var2 = new SimpleDateFormat(var1);
      var2.setTimeZone(this.timeZoneId.getTimeZone());
      return var2;
   }
}
