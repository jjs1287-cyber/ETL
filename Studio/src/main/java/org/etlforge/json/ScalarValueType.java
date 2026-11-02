package org.etlforge.json;

import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.JsonStringEncoder;
import org.etlforge.xml.KongaXmlUtils;

public enum ScalarValueType {
   STRING {
      @Override
      public String format(Object var1) {
         if (var1 == null) {
            return "null";
         }

         char[] var2 = JsonStringEncoder.getInstance().quoteAsString((String)var1);
         return "\"" + new String(var2) + "\"";
      }
   },
   NUMBER {
      @Override
      public String format(Object var1) {
         return var1 != null && !"".equals(var1) ? var1.toString() : "null";
      }
   },
   BOOLEAN {
      @Override
      public String format(Object var1) {
         if (var1 instanceof Boolean) {
            return this.formatBoolean((Boolean)var1);
         } else if (var1 instanceof String) {
            return this.formatBoolean(KongaXmlUtils.isTrue((String)var1));
         } else {
            throw new RuntimeException("Unexpected value: " + (var1 == null ? "null" : var1.getClass().getName()));
         }
      }

      private String formatBoolean(boolean var1) {
         return var1 ? "true" : "false";
      }
   },
   NULL {
      @Override
      public String format(Object var1) {
         return "null";
      }
   };

   ScalarValueType() {
   }

   public abstract String format(Object var1);

   public static ScalarValueType fromJsonToken(JsonToken var0) {
      switch (var0) {
         case VALUE_STRING:
            return STRING;
         case VALUE_NUMBER_INT:
         case VALUE_NUMBER_FLOAT:
            return NUMBER;
         case VALUE_FALSE:
         case VALUE_TRUE:
            return BOOLEAN;
         case VALUE_NULL:
            return NULL;
         default:
            throw new RuntimeException("Not a scalar value token: " + var0);
      }
   }
}
