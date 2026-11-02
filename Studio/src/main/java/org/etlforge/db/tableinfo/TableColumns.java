package org.etlforge.db.tableinfo;

public interface TableColumns {
   TableDescriptor getTable();

   Iterable<ColumnDescriptor> getColumns();

   int getColumnCount();

   Iterable<ColumnDescriptor> getPrimaryKeys();

   int getNumberOfPrimaryKeys();
}
