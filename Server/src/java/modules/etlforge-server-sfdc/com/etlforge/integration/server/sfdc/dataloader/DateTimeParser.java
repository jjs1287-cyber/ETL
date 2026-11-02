package com.etlforge.integration.server.sfdc.dataloader;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimeZone;
import org.etlforge.system.ArgCheck;

final class DateTimeParser {
   private final LinkedList<String> timeFormats = Lists.newLinkedList();
   private final String dateFormat;
   private final TimeZone timeZone;
   private final Map<String, DateFormat> knownFormats = Maps.newHashMap();
   private int attempts;
   private ParseException lastError;
   private final TimeZone utc = TimeZone.getTimeZone("UTC");
   private SimpleDateFormat dateWithoutTimeFormat;
   private boolean throwParseErrorOnFailure = true;

   public DateTimeParser(String var1, TimeZone var2) {
      this.dateFormat = ArgCheck.checkNotEmpty(var1);
      this.timeZone = Preconditions.checkNotNull(var2);
      this.timeFormats
         .addAll(
            Arrays.asList(
               "'T'HH:mm:ss'Z'",
               "'T'HH:mm:ss.SSS'Z'",
               "'T'HH:mm:ss z",
               "'T'HH:mm:ss.SSS z",
               "'T'HH:mm:ssz",
               "'T'HH:mm:ss.SSSz",
               "'T'HH:mm:ss Z",
               "'T'HH:mm:ss.SSS Z",
               "'T'HH:mm:ssZ",
               "'T'HH:mm:ss.SSSZ",
               "'T'HH:mm:ss",
               " HH:mm:ss",
               " HH:mm",
               "'T'HH:mm'Z'",
               "'T'HH:mm z",
               "'T'HH:mm Z",
               "'T'HH:mmZ",
               "'T'HH:mm"
            )
         );
   }

   public void setThrowParseErrorOnFailure(boolean var1) {
      this.throwParseErrorOnFailure = var1;
   }

   public Date parse(String var1) throws ParseException {
      this.lastError = null;
      this.attempts = 1;

      for (Iterator var2 = this.timeFormats.iterator(); var2.hasNext(); this.attempts++) {
         Date var3 = this.tryTimePart(var1, var2);
         if (var3 != null) {
            return var3;
         }
      }

      if (this.isDateWithoutTime(var1)) {
         Date var4 = this.parseDateWithoutTimeAsMidnight(var1);
         if (var4 != null) {
            return var4;
         }
      }

      this.throwParseError();
      return null;
   }

   private Date tryTimePart(String var1, Iterator<String> var2) {
      String var3 = (String)var2.next();
      DateFormat var4 = this.getDateFormatForTimePart(var3);

      try {
         Date var5 = var4.parse(var1);
         if (this.attempts > 1) {
            this.makePreferredTimePart(var2, var3);
         }

         return var5;
      } catch (ParseException var6) {
         this.lastError = var6;
         return null;
      }
   }

   private DateFormat getDateFormatForTimePart(String var1) {
      DateFormat var2 = this.knownFormats.get(var1);
      if (var2 == null) {
         var2 = new SimpleDateFormat(this.dateFormat + var1);
         if (var1.endsWith("'Z'")) {
            var2.setTimeZone(this.utc);
         } else {
            var2.setTimeZone(this.timeZone);
         }

         this.knownFormats.put(var1, var2);
      }

      return var2;
   }

   private void makePreferredTimePart(Iterator<String> var1, String var2) {
      var1.remove();
      this.timeFormats.addFirst(var2);
   }

   private boolean isDateWithoutTime(String var1) {
      try {
         SimpleDateFormat var2 = new SimpleDateFormat(this.dateFormat);
         var2.parse(var1);
         return true;
      } catch (ParseException var3) {
         return false;
      }
   }

   private Date parseDateWithoutTimeAsMidnight(String var1) {
      try {
         if (this.dateWithoutTimeFormat == null) {
            this.dateWithoutTimeFormat = this.createDateWithoutTimeFormat();
         }

         String var2 = var1 + " 00:00:00";
         return this.dateWithoutTimeFormat.parse(var2);
      } catch (ParseException var3) {
         return null;
      }
   }

   private SimpleDateFormat createDateWithoutTimeFormat() {
      SimpleDateFormat var1 = new SimpleDateFormat(this.dateFormat + " HH:mm:ss");
      var1.setLenient(false);
      var1.setTimeZone(this.timeZone);
      return var1;
   }

   private void throwParseError() throws ParseException {
      if (this.throwParseErrorOnFailure) {
         if (this.lastError == null) {
            throw new RuntimeException("This should be impossible.");
         } else {
            throw this.lastError;
         }
      }
   }

   @VisibleForTesting
   public int getNumberOfAttempts() {
      return this.attempts;
   }
}
