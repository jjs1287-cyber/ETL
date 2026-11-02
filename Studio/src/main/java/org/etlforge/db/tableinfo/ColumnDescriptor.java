package org.etlforge.db.tableinfo;

public interface ColumnDescriptor {
   String catalog();

   String schema();

   String tableName();

   String columnName();

   int dataType();

   String typeName();

   int columnSize();

   int decimalDigits();

   int radix();

   ColumnNullableValue nullable();

   String remarks();

   String defaultValue();

   int charOctetLength();

   int ordinalPosition();

   ColumnIsNullableValue isNullable();
}
