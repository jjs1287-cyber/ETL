package org.etlforge.integration.database.info;

import java.util.List;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.driver.OdbcDriverDescriptor;

public final class InformixUtils {
   private static final String INFORMIX = "informix";
   private static final String ROWID = "rowid";

   public static boolean isInformix(DriverDescriptor var0) {
      if (var0 instanceof JdbcDriverDescriptor) {
         String var2 = ((JdbcDriverDescriptor)var0).getSubprotocol().toLowerCase();
         return var2.contains("informix");
      } else if (var0 instanceof OdbcDriverDescriptor) {
         String var1 = var0.getName().toLowerCase();
         return var1.contains("informix");
      } else {
         return false;
      }
   }

   public static void addRowIdColumn(DatabaseObject var0) {
      List var1 = var0.getAllColumns();
      if (!hasRowId(var1)) {
         var0.removeAllColumns();
         var1.add(0, createRowIdColumn(var0));

         for (Object __etlItem30 : var1) {
            DatabaseColumn var3 = (DatabaseColumn) __etlItem30;
            var0.addColumn(var3);
         }
      }
   }

   private static boolean hasRowId(List<DatabaseColumn> var0) {
      if (!var0.isEmpty()) {
         DatabaseColumn var1 = (DatabaseColumn)var0.get(0);
         if (var1.getName().equals("rowid")) {
            return true;
         }
      }

      return false;
   }

   private static DatabaseColumn createRowIdColumn(DatabaseObject var0) {
      Object var1 = null;
      boolean var2 = true;
      boolean var3 = false;
      byte var4 = 10;
      byte var5 = 0;
      return new DatabaseColumn(var0, "rowid", "int", 4, (String)var1, var2, var3, var4, var5);
   }

   public static void removeRowIdColumn(DatabaseObject var0) {
      List var1 = var0.getAllColumns();
      if (!var1.isEmpty()) {
         DatabaseColumn var2 = (DatabaseColumn)var1.get(0);
         if (var2.getName().equals("rowid")) {
            var0.removeAllColumns();

            for (Object __etlItem63 : var1.subList(1, var1.size())) {
               DatabaseColumn var4 = (DatabaseColumn) __etlItem63;
               var0.addColumn(var4);
            }
         }
      }
   }

   private InformixUtils() {
   }
}
