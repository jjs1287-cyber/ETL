package org.etlforge.integration.dataelement;

import org.apache.commons.lang.StringUtils;

public final class GlobalDE {
   public static final String MARKER = "$";

   public static boolean isMarker(char var0) {
      return var0 == '$';
   }

   public static String forScript(String var0) {
      return "$" + var0;
   }

   public static String nameOf(String var0) {
      return StringUtils.removeStart(var0, "$");
   }

   public static boolean isGlobalDE(String var0) {
      return var0.startsWith("$");
   }

   private GlobalDE() {
   }
}
