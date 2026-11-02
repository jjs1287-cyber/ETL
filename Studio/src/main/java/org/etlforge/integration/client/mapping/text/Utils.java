package org.etlforge.integration.client.mapping.text;

import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.FieldDelimiter;

final class Utils {
   public static String getDelimiterString(TextStructure var0) {
      FieldDelimiter var1 = var0.getDelimiter();
      if (var1 == FieldDelimiter.TAB) {
         return "\\t";
      } else if (var1 == FieldDelimiter.ASCII_ONE) {
         return "\\1";
      } else if (var1 == FieldDelimiter.NEW_LINE) {
         return "\\n";
      } else {
         return var1 != null ? var1.toString() : "";
      }
   }

   private Utils() {
   }
}
