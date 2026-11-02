package org.etlforge.integration.data.structure.text;

import org.etlforge.util.persist.Persistor;

public final class FieldDelimiter {
   private final String stringValue;
   private final char charValue;
   public static final FieldDelimiter COMMA = new FieldDelimiter(',');
   public static final FieldDelimiter TAB = new FieldDelimiter('\t');
   public static final FieldDelimiter ASCII_ONE = new FieldDelimiter('\u0001');
   public static final FieldDelimiter NEW_LINE = new FieldDelimiter('\n');

   public static FieldDelimiter fromString(String var0) {
      FieldDelimiter var1 = null;
      if (var0.length() == 1) {
         var1 = fromSingleChar(var0.charAt(0));
      } else if (var0.length() > 1 && var0.charAt(0) == '\\') {
         var1 = fromEscapedString(var0);
      }

      if (var1 == null) {
         var1 = new FieldDelimiter(var0);
      }

      return var1;
   }

   private static FieldDelimiter fromSingleChar(char var0) {
      if (var0 >= ' ') {
         return new FieldDelimiter(var0);
      } else if (var0 == '\t') {
         return TAB;
      } else if (var0 == 1) {
         return ASCII_ONE;
      } else {
         return var0 == '\n' ? NEW_LINE : null;
      }
   }

   private static FieldDelimiter fromEscapedString(String var0) {
      switch (var0.charAt(1)) {
         case '1':
            return ASCII_ONE;
         case 'n':
            return NEW_LINE;
         case 't':
            return TAB;
         default:
            return null;
      }
   }

   private FieldDelimiter(char var1) {
      this.charValue = var1;
      this.stringValue = String.valueOf(var1);
   }

   private FieldDelimiter(String var1) {
      this.charValue = 0;
      this.stringValue = var1;
   }

   public boolean isValid() {
      return this.charValue > 0;
   }

   public char asChar() {
      return this.charValue;
   }

   @Override
   public String toString() {
      return this.stringValue;
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof FieldDelimiter && ((FieldDelimiter)var1).stringValue.equals(this.stringValue);
   }

   @Override
   public int hashCode() {
      return this.stringValue.hashCode();
   }

   public void writeTo(Persistor var1) {
      String var2 = "value";
      if (this == ASCII_ONE) {
         var1.putString(var2, "ascii one");
      } else {
         var1.putString(var2, this.toString());
      }
   }

   public static FieldDelimiter fromPersistor(Persistor var0) {
      String var1 = var0.getString("value");
      if (var1 == null) {
         throw new IllegalArgumentException("Invalid persistor: the value key is missing");
      } else {
         return var1.equals("ascii one") ? ASCII_ONE : fromString(var1);
      }
   }

   public String toEscapedString() {
      if (this == TAB) {
         return "\\t";
      } else if (this == ASCII_ONE) {
         return "\\1";
      } else {
         return this == NEW_LINE ? "\\n" : this.toString();
      }
   }
}
