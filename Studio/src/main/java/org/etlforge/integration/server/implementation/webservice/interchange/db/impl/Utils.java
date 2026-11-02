package org.etlforge.integration.server.implementation.webservice.interchange.db.impl;

import java.util.EnumSet;
import org.etlforge.integration.database.driver.DriverType;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;

final class Utils {
   public static String[] getTypeNames(TableSearchFilter var0, DriverType var1) {
      EnumSet var2 = var0.getObjectTypes();
      String[] var3 = new String[var2.size()];
      int var4 = 0;

      for (Object __etlItem14 : var2) {
         DatabaseObjectType var6 = (DatabaseObjectType) __etlItem14;
         String var7;
         if (var6 == DatabaseObjectType.SYSTEM_TABLE && var1 == DriverType.ODBC) {
            var7 = var6.toString();
         } else {
            var7 = var6.name();
         }

         var3[var4++] = var7;
      }

      return var3;
   }

   private Utils() {
   }
}
