package org.etlforge.integration.client.database.ui;

import com.google.common.collect.Ordering;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

public enum ColumnDisplayOrder {
   DATABASE,
   ALPHABETICAL;

   public DatabaseColumn[] getColumns(DatabaseObject var1) {
      List var2 = var1.getAllColumns();
      switch (this) {
         case ALPHABETICAL:
            Collections.sort(var2, new ColumnDisplayOrder.AlphabeticalOrder());
         case DATABASE:
            return (DatabaseColumn[]) var2.toArray(new DatabaseColumn[var2.size()]);
         default:
            throw new RuntimeException("Unexpected order: " + this);
      }
   }

   public Comparator<DatabaseColumn> getComparator(DatabaseObject var1) {
      switch (this) {
         case DATABASE:
            return Ordering.explicit(var1.getAllColumns());
         case ALPHABETICAL:
            return new ColumnDisplayOrder.AlphabeticalOrder();
         default:
            throw new RuntimeException("Unexpected order: " + this);
      }
   }

   private static class AlphabeticalOrder implements Comparator<DatabaseColumn> {
      private AlphabeticalOrder() {
      }

      public int compare(DatabaseColumn var1, DatabaseColumn var2) {
         return var1.getName().compareToIgnoreCase(var2.getName());
      }
   }
}
