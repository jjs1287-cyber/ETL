package org.etlforge.integration.data.script;

public final class StringLiterals {
   public static String doubleQuoted(String var0) {
      return create(var0, "\"");
   }

   public static String singleQuoted(String var0) {
      return create(var0, "'");
   }

   private static String create(String var0, String var1) {
      String var2 = var0.isEmpty() ? var0 : escape(var0, var1);
      return var1 + var2 + var1;
   }

   private static String escape(String var0, String var1) {
      String var2 = var0.replace("\\", "\\\\");
      var2 = var2.replace(var1, "\\" + var1);
      return var2.replace("\t", "\\t");
   }

   private StringLiterals() {
   }
}
