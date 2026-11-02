package org.etlforge.string;

import java.util.regex.Pattern;

public final class RegexUtils {
   public static String guidRegex() {
      String var0 = "[0-9a-fA-F]";
      return String.format("%s{8}-%s{4}-%s{4}-%s{4}-%s{12}", var0, var0, var0, var0, var0);
   }

   public static Pattern guidPattern() {
      String var0 = guidRegex();
      return Pattern.compile(var0, 2);
   }

   private RegexUtils() {
   }
}
