package org.etlforge.integration.common.server;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.lang.StringUtils;
import org.etlforge.util.text.ThreadSafeDateFormat;

public final class ServerConversionUtils {
   private static final ThreadSafeDateFormat formatter = new ThreadSafeDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

   public static String toServerDateFormat(Date var0) {
      return var0 != null ? toString(var0, true) : "";
   }

   public static Date fromServerDateFormat(String var0) {
      return StringUtils.isNotEmpty(var0) ? valueOf(var0, false) : null;
   }

   private static String toString(Date var0, boolean var1) {
      if (var0 == null) {
         return "";
      }

      long var2 = var0.getTime();
      int var4 = TimeZone.getDefault().getOffset(var2);
      if (var1) {
         var4 = TimeZone.getDefault().getOffset(var2 - var4);
      }

      Date var5 = new Date(var2 + (var1 ? -var4 : var4));
      return formatter.get().format(var5);
   }

   private static Date valueOf(String var0, boolean var1) {
      ParsePosition var2 = new ParsePosition(0);
      Date var3 = formatter.get().parse(var0, var2);
      if (var3 == null) {
         return null;
      }

      long var4 = var3.getTime();
      int var6 = TimeZone.getDefault().getOffset(var4);
      if (var1) {
         var6 = TimeZone.getDefault().getOffset(var4 - var6);
      }

      return new Date(var4 + (var1 ? -var6 : var6));
   }

   private ServerConversionUtils() {
   }
}
