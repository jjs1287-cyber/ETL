package org.etlforge.integration.structure.mapping;

import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.xml.sax.Attributes;

public final class JtrXmlFormatUtils {
   public static String lookupIndexedString(Attributes var0, String var1, Map<Integer, String> var2) {
      String var3 = var0.getValue(var1);
      if (var3 != null && var3.length() > 0) {
         try {
            return (String)var2.get(Integer.valueOf(var3));
         } catch (NumberFormatException var5) {
            var5.printStackTrace();
         }
      }

      return null;
   }

   public static boolean getBool(Attributes var0, String var1, boolean var2) {
      String var3 = var0.getValue(var1);
      return var3 != null && var3.length() != 0 ? var3.equals("1") || var3.equalsIgnoreCase("true") : var2;
   }

   public static int getInt(Attributes var0, String var1, int var2) {
      String var3 = var0.getValue(var1);
      return var3 != null && var3.length() > 0 ? Integer.parseInt(var3) : var2;
   }

   public static short getShort(Attributes var0, String var1, int var2) {
      String var3 = var0.getValue(var1);
      return var3 != null && var3.length() > 0 ? Short.parseShort(var3) : (short)var2;
   }

   public static String getString(Attributes var0, String var1) {
      return var0.getValue(var1);
   }

   public static String getString(Attributes var0, String var1, String var2) {
      String var3 = var0.getValue(var1);
      if (var3 == null) {
         var3 = var2;
      }

      return var3;
   }

   public static void addHexAttribute(StringBuilder var0, String var1, long var2) {
      if (var2 != 0L) {
         var0.append(' ').append(var1).append("=\"").append(String.format("0x%x", var2)).append('"');
      }
   }

   public static void addAttribute(StringBuilder var0, String var1, boolean var2, boolean var3) {
      if (var2 != var3) {
         var0.append(' ').append(var1).append("=\"").append(var2 ? "1" : "0").append('"');
      }
   }

   public static void addAttribute(StringBuilder var0, String var1, int var2, int var3) {
      if (var2 != var3) {
         var0.append(' ').append(var1).append("=\"").append(var2).append('"');
      }
   }

   public static void addAttribute(StringBuilder var0, String var1, String var2) {
      if (var2 != null && var2.length() != 0) {
         var0.append(' ').append(var1).append("=\"").append(StringEscapeUtils.escapeXml(var2)).append('"');
      }
   }

   private JtrXmlFormatUtils() {
   }
}
