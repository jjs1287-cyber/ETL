package org.etlforge.integration.client.database.ui.sqlbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.selection.Selection;

public class DbObjectsViewSelection implements Selection {
   private final List<DatabaseObject> tables;
   private final List<DatabaseColumn> columns;

   public DbObjectsViewSelection(List<DatabaseObject> var1, List<DatabaseColumn> var2) {
      this.tables = Collections.unmodifiableList(var1);
      this.columns = Collections.unmodifiableList(var2);
   }

   @Override
   public boolean isEmpty() {
      return this.tables.isEmpty() && this.columns.isEmpty();
   }

   @Override
   public Iterable<? extends Object> items() {
      ArrayList var1 = new ArrayList<>(this.tables);
      var1.addAll(this.columns);
      return var1;
   }

   public List<DatabaseObject> getTables() {
      return this.tables;
   }

   public List<DatabaseColumn> getColumns() {
      return this.columns;
   }
}
