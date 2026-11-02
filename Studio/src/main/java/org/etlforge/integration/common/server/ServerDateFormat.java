package org.etlforge.integration.common.server;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.etlforge.util.text.ThreadSafeDateFormat;

public class ServerDateFormat {
   private final DateFormat impl = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   public ServerDateFormat() {
      this(TimeZone.getTimeZone("UTC"));
   }

   public ServerDateFormat(TimeZone var1) {
      this.impl.setTimeZone(var1);
   }

   public Date parse(String var1) throws ParseException {
      return this.impl.parse(var1);
   }

   public String format(Date var1) {
      return this.impl.format(var1);
   }

   public ThreadSafeDateFormat getThreadSafeFormatter() {
      return new ThreadSafeDateFormat(this.impl);
   }
}
