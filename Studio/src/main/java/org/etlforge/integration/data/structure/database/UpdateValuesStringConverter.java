package org.etlforge.integration.data.structure.database;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

final class UpdateValuesStringConverter {
   private static final String VALUE_SEPARATOR = "^";

   public static String toString(DatabaseColumn[] var0, String[] var1) {
      StringBuilder var2 = new StringBuilder();

      for (int var3 = 0; var3 < var0.length; var3++) {
         var2.append(var0[var3].getQualifiedName());
         var2.append("=");
         var2.append(var1[var3]);
         var2.append("^");
      }

      return var2.toString();
   }

   public static UpdateValue[] fromString(String var0, DatabaseObject var1) {
      if (!var0.endsWith("^")) {
         var0 = var0 + "^";
      }

      ArrayList var2 = Lists.newArrayList();

      for (DatabaseColumn var4 : var1.getAllColumns()) {
         String var5 = extractValueForColumn(var4, var0);
         if (var5 != null) {
            var2.add(new UpdateValue(var4, var5));
         }
      }

      return (UpdateValue[]) var2.toArray(new UpdateValue[var2.size()]);
   }

   private static String extractValueForColumn(DatabaseColumn var0, String var1) {
      String var2 = var0.getQualifiedName() + "=";
      return StringUtils.substringBetween(var1, var2, "^");
   }

   private UpdateValuesStringConverter() {
   }
}
