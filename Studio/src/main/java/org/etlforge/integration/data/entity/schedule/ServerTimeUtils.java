package org.etlforge.integration.data.entity.schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class ServerTimeUtils {
   private static final TimeZone LOCAL_TIMEZONE = TimeZone.getDefault();
   private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

   public static synchronized Date fromServerFormat(String var0) throws ParseException {
      return TIME_FORMAT.parse(var0);
   }

   public static synchronized String toServerFormat(Date var0) {
      return TIME_FORMAT.format(var0);
   }

   private ServerTimeUtils() {
   }

   static {
      TIME_FORMAT.setTimeZone(LOCAL_TIMEZONE);
   }
}
