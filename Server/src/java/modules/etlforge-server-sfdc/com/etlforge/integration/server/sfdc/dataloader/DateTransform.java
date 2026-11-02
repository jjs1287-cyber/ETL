package com.etlforge.integration.server.sfdc.dataloader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.axis.utils.StringUtils;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.bulkload.TimeZoneId;

public final class DateTransform {
   private static final String XML_DATE = "yyyy-MM-dd";
   private static final String XML_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss'Z'";
   private final DateFormat iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd");
   private final DateTimeParser iso8601DateTimeParser;
   private final DateTimeParser shortFormatDateTimeParser;
   private final DateTimeParser compactFormatDateTimeParser;
   private final DateFormat shortDateFormat;
   private final DateFormat compactFormat = createDateFormat("yyyyMMdd");
   private final DateFormat outputDateFormat = createDateFormat("yyyy-MM-dd");
   private final DateFormat outputDateTimeFormat = createDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
   private boolean returnInputValueIfInvalid;

   public DateTransform(DateConversionParams var1) {
      TimeZone var2 = var1.getTimeZone();
      this.iso8601DateTimeParser = new DateTimeParser("yyyy-MM-dd", var2);
      TimeZone var3 = TimeZoneId.UTC.getTimeZone();
      this.outputDateTimeFormat.setTimeZone(var3);
      this.shortDateFormat = createDateFormat(var1.dateFormat);
      this.shortFormatDateTimeParser = new DateTimeParser(var1.dateFormat, var2);
      this.compactFormatDateTimeParser = new DateTimeParser("yyyyMMdd", var2);
   }

   public DateTransform(String var1, String var2, String var3) throws Exception {
      TimeZone var4 = TimeZone.getTimeZone(var1);
      if (var4.getID() == "GMT" && var1 != "GMT") {
         throw new Exception("Unrecognized time zone ID: '" + var1 + "'  NOTE: Time zone IDs are case sensitive.");
      }

      this.iso8601DateTimeParser = new DateTimeParser("yyyy-MM-dd", var4);
      TimeZone var5 = TimeZone.getTimeZone(var2);
      if (var5.getID() == "GMT" && var2 != "GMT") {
         throw new Exception("Unrecognized time zone ID: '" + var2 + "'  NOTE: Time zone IDs are case sensitive.");
      }

      this.outputDateTimeFormat.setTimeZone(var5);
      this.shortDateFormat = createDateFormat(var3);
      this.shortFormatDateTimeParser = new DateTimeParser(var3, var4);
      this.compactFormatDateTimeParser = new DateTimeParser("yyyyMMdd", var4);
   }

   private static DateFormat createDateFormat(String var0) {
      SimpleDateFormat var1 = new SimpleDateFormat(var0);
      var1.setLenient(false);
      return var1;
   }

   public void setReturnInputValueIfInvalid(boolean var1) {
      boolean var2 = !var1;
      this.shortFormatDateTimeParser.setThrowParseErrorOnFailure(var2);
      this.compactFormatDateTimeParser.setThrowParseErrorOnFailure(var2);
      this.shortFormatDateTimeParser.setThrowParseErrorOnFailure(var2);
      this.iso8601DateTimeParser.setThrowParseErrorOnFailure(var2);
      this.returnInputValueIfInvalid = var1;
   }

   public String convertDate(String var1) throws ParseException {
      var1 = this.stripQuotes(var1);
      if (StringUtils.isEmpty(var1)) {
         return var1;
      }

      if (var1.length() < 3) {
         if (this.returnInputValueIfInvalid) {
            return var1;
         } else {
            throw new ParseException("Unparseable date: " + var1, var1.length());
         }
      } else {
         DateFormat var2;
         if (this.isShortFormat(var1)) {
            var2 = this.shortDateFormat;
         } else if (this.isCompactFormat(var1)) {
            var2 = this.compactFormat;
         } else {
            var2 = this.iso8601DateFormat;
         }

         try {
            Date var3 = var2.parse(var1);
            return this.outputDateFormat.format(var3);
         } catch (ParseException var4) {
            if (this.returnInputValueIfInvalid) {
               return var1;
            } else {
               throw var4;
            }
         }
      }
   }

   private String stripQuotes(String var1) {
      if (var1 == null) {
         return var1;
      }

      if (var1.length() >= 2 && var1.startsWith("\"") && var1.endsWith("\"")) {
         var1 = var1.substring(1, var1.length() - 1);
      }

      return var1.trim();
   }

   private boolean isShortFormat(String var1) {
      return var1.charAt(2) == '/' || var1.charAt(1) == '/';
   }

   private boolean isCompactFormat(String var1) {
      if (var1.length() < 8) {
         return false;
      }

      for (int var2 = 0; var2 < 8; var2++) {
         char var3 = var1.charAt(var2);
         if (!Character.isDigit(var3)) {
            return false;
         }
      }

      return true;
   }

   public String convertDateTime(String var1) throws ParseException {
      var1 = this.stripQuotes(var1);
      if (StringUtils.isEmpty(var1)) {
         return var1;
      }

      if (var1.length() < 3) {
         if (this.returnInputValueIfInvalid) {
            return var1;
         } else {
            throw new ParseException("Unparseable date: " + var1, var1.length());
         }
      } else {
         DateTimeParser var2;
         if (this.isShortFormat(var1)) {
            var2 = this.shortFormatDateTimeParser;
         } else if (this.isCompactFormat(var1)) {
            var2 = this.compactFormatDateTimeParser;
         } else {
            var2 = this.iso8601DateTimeParser;
         }

         Date var3 = var2.parse(var1);
         if (var3 == null) {
            assert this.returnInputValueIfInvalid;
            return var1;
         } else {
            return this.outputDateTimeFormat.format(var3);
         }
      }
   }
}
