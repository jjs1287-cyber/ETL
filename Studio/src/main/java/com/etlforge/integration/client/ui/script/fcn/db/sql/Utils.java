package com.etlforge.integration.client.ui.script.fcn.db.sql;

import java.util.List;
import org.etlforge.integration.client.database.ui.sqlbuilder.NameGenerator;
import org.etlforge.integration.database.info.DatabaseColumn;

final class Utils {
   public static String concatenateColumnNames(NameGenerator var0, List<DatabaseColumn> var1) {
      StringBuilder var2 = new StringBuilder();

      for (DatabaseColumn var4 : var1) {
         if (var2.length() > 0) {
            var2.append(", ");
         }

         var2.append(var0.getColumnName(var4));
      }

      return var2.toString();
   }

   private Utils() {
   }
}
