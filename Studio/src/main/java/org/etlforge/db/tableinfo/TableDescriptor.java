package org.etlforge.db.tableinfo;

public interface TableDescriptor {
   String catalog();

   String schema();

   String tableName();

   String tableType();

   String remarks();

   String typesCatalog();

   String typesSchema();

   String typeName();

   String selfReferencingColumnName();

   String refGeneration();
}
