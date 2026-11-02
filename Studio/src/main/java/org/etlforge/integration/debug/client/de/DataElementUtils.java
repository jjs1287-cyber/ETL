package org.etlforge.integration.debug.client.de;

import com.google.common.base.Predicate;
import org.etlforge.lang.KongaStringUtils;

public final class DataElementUtils {
   public static String toNameValuePairString(DataElement[] var0, Predicate<DataElement> var1) {
      StringBuilder var2 = new StringBuilder();

      for (DataElement var6 : var0) {
         if (var1.apply(var6)) {
            var2.append(var6.getDisplayName()).append("=");
            String var7 = var6.getValue();
            if (var7 == null) {
               var2.append("NULL;");
            } else {
               if (var7.isEmpty()) {
                  var7 = "\"\"";
               } else {
                  char var8 = 0;
                  char var9 = 0;
                  if (var7.length() > 2) {
                     var8 = var7.charAt(0);
                     var9 = var7.charAt(var7.length() - 1);
                  }

                  if ((var8 != '{' || var9 != '}' || var6.getName().indexOf("#.") <= 0)
                     && (var8 != '"' || var9 != '"')
                     && KongaStringUtils.CSV_RequireQualifier(var7, "{};,\"")) {
                     var7 = "\"" + KongaStringUtils.CSV_EscapeDoubleQuote(var7) + "\"";
                  }
               }

               var2.append(var7).append(";");
            }
         }
      }

      return var2.toString();
   }

   public static String getValueAsString(DataElement var0) {
      return var0.isArray() ? getArrayValueString(var0.getArrValues()) : var0.getValue();
   }

   public static String getArrayValueString(DataElement[] var0) {
      StringBuilder var1 = new StringBuilder("{");
      boolean var2 = true;

      for (DataElement var6 : var0) {
         if (var2) {
            var2 = false;
         } else {
            var1.append(", ");
         }

         if (var6.isArray()) {
            var1.append(getArrayValueString(var6.getArrValues()));
         } else {
            var1.append(var6.getValue());
         }
      }

      return var1.append("}").toString();
   }

   private DataElementUtils() {
   }
}
