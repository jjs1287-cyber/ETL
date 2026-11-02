package org.etlforge.util.text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadSafeDateFormat {
   private final DateFormat prototype;
   private final ThreadLocal<DateFormat> formats;

   public ThreadSafeDateFormat(String var1) {
      this(new SimpleDateFormat(var1));
   }

   public ThreadSafeDateFormat(DateFormat var1) {
      this.prototype = (DateFormat)var1.clone();
      this.formats = new ThreadLocal<DateFormat>() {
         protected DateFormat initialValue() {
            return (DateFormat)ThreadSafeDateFormat.this.prototype.clone();
         }
      };
   }

   public String format(Date var1) {
      return this.get().format(var1);
   }

   public Date parse(String var1) throws ParseException {
      return this.get().parse(var1);
   }

   public DateFormat get() {
      return this.formats.get();
   }
}
