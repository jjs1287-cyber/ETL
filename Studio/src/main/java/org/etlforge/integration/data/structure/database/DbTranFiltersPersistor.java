package org.etlforge.integration.data.structure.database;

import org.etlforge.util.SortOrder;
import org.etlforge.util.persist.Persistor;

public class DbTranFiltersPersistor {
   private static final String ENTRY = "Entry";
   private static final String TABLE = "table";
   private static final String COLUMN = "column";
   private static final String CONDITION = "condition";
   private static final String SORT_ORDER = "sortOrder";

   public void save(DbTranFilters var1, Persistor var2) {
      for (DbTranFilters.Entry var4 : var1.getAllEntries()) {
         this.saveEntry(var4, var2.createChild("Entry"));
      }
   }

   private void saveEntry(DbTranFilters.Entry var1, Persistor var2) {
      var2.putString("table", var1.getTableName());
      var2.putString("column", var1.getColumnName());
      var2.putString("condition", var1.getCondition());
      var2.putString("sortOrder", var1.getSortOrder().name());
   }

   public DbTranFilters restoreFrom(Persistor var1) {
      DbTranFilters var2 = new DbTranFilters();

      for (Persistor var4 : var1.getChildren("Entry")) {
         var2.addEntry(this.restoreEntry(var4));
      }

      return var2;
   }

   private DbTranFilters.Entry restoreEntry(Persistor var1) {
      return new DbTranFilters.Entry(
         var1.getString("table"), var1.getString("column"), var1.getString("condition"), SortOrder.valueOf(var1.getString("sortOrder"))
      );
   }
}
