package org.etlforge.integration.data.structure.database;

import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;

public final class DbStructureUtils {
   public static boolean isSchemaNameIncluded(DatabaseStructure var0) {
      if (var0.getIncludeSchemaName()) {
         return true;
      }

      BeginEndQuote var1 = var0.getBeginEndQuote();

      for (DatabaseObject var5 : var0.getDatabaseObjects()) {
         String var6 = var5.getName();
         if (var5.getSchema() != null && var5.getSchema().length() > 0) {
            String var7 = var1.escapeName(var5.getSchema()) + ".";
            if (var6.length() > var7.length() && var6.startsWith(var7)) {
               return true;
            }
         }
      }

      return false;
   }

   private DbStructureUtils() {
   }
}
