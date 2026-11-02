package org.etlforge.integration.data.structure.database;

import java.util.Map;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;

public class DbTablesFactory {
   private final ObjectAliasGenerator aliasGenerator;

   public DbTablesFactory(ObjectAliasGenerator var1) {
      if (var1 == null) {
         throw new NullPointerException("aliasGenerator");
      }

      this.aliasGenerator = var1;
   }

   public DbTables create(Map<DatabaseObject, Integer> var1, BeginEndQuote var2) {
      DbTables var3 = new DbTables();
      var3.setTables(this.getTablesString(var1, var2));
      return var3;
   }

   private String getTablesString(Map<DatabaseObject, Integer> var1, BeginEndQuote var2) {
      StringBuilder var3 = new StringBuilder();

      for (DatabaseObject var5 : var1.keySet()) {
         String var6 = var5.getName();
         String var7 = DatabaseStructureUtils.escape(var6, var2);
         var3.append(var7);

         for (String var11 : this.aliasGenerator.getAllAliases(var5)) {
            var3.append(",");
            var3.append(var7);
            var3.append(" ");
            var3.append(DatabaseStructureUtils.escape(var11, var2));
         }

         var3.append(",");
      }

      return var3.toString();
   }
}
