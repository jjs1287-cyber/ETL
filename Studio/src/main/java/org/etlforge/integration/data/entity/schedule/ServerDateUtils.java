package org.etlforge.integration.data.entity.schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class ServerDateUtils {
   private static final TimeZone LOCAL_TIMEZONE = TimeZone.getDefault();
   private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

   public static synchronized Date fromServerFormat(String var0) throws ParseException {
      return DATE_FORMAT.parse(var0);
   }

   public static synchronized String toServerFormat(Date var0) {
      return DATE_FORMAT.format(var0);
   }

   private ServerDateUtils() {
   }

   static {
      DATE_FORMAT.setTimeZone(LOCAL_TIMEZONE);
   }
}
