package org.etlforge.db.tableinfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;

public abstract class TableDescriptorFactory {
   public abstract TableDescriptor createDescriptor() throws SQLException;

   public static TableDescriptorFactory newResultSetBasedFactory(ResultSet var0) {
      return new TableDescriptorFactory.ResultSetFactory(var0);
   }

   private static class DescriptorImpl implements TableDescriptor {
      private final EnumMap<TableDescriptorFactory.Entry, String> values = new EnumMap<>(TableDescriptorFactory.Entry.class);

      private DescriptorImpl() {
      }

      @Override
      public String catalog() {
         return this.values.get(TableDescriptorFactory.Entry.CATALOG);
      }

      @Override
      public String typesCatalog() {
         return this.values.get(TableDescriptorFactory.Entry.TYPES_CATALOG);
      }

      @Override
      public String refGeneration() {
         return this.values.get(TableDescriptorFactory.Entry.REF_GENERATION);
      }

      @Override
      public String remarks() {
         return this.values.get(TableDescriptorFactory.Entry.REMARKS);
      }

      @Override
      public String schema() {
         return this.values.get(TableDescriptorFactory.Entry.SCHEMA);
      }

      @Override
      public String typesSchema() {
         return this.values.get(TableDescriptorFactory.Entry.TYPES_SCHEMA);
      }

      @Override
      public String selfReferencingColumnName() {
         return this.values.get(TableDescriptorFactory.Entry.SELF_REFERENCING_COL_NAME);
      }

      @Override
      public String tableName() {
         return this.values.get(TableDescriptorFactory.Entry.TABLE_NAME);
      }

      @Override
      public String tableType() {
         return this.values.get(TableDescriptorFactory.Entry.TABLE_TYPE);
      }

      @Override
      public String typeName() {
         return this.values.get(TableDescriptorFactory.Entry.TYPE_NAME);
      }
   }

   private enum Entry {
      CATALOG(1),
      SCHEMA(2),
      TABLE_NAME(3),
      TABLE_TYPE(4),
      REMARKS(5),
      TYPES_CATALOG(6),
      TYPES_SCHEMA(7),
      TYPE_NAME(8),
      SELF_REFERENCING_COL_NAME(9),
      REF_GENERATION(10);

      private final int index;

      Entry(int var3) {
         this.index = var3;
      }

      public String getValueFromDb(ResultSet var1) throws SQLException {
         return var1.getString(this.index);
      }
   }

   private static class ResultSetFactory extends TableDescriptorFactory {
      private final ResultSet rs;

      public ResultSetFactory(ResultSet var1) {
         this.rs = var1;
      }

      @Override
      public TableDescriptor createDescriptor() throws SQLException {
         TableDescriptorFactory.DescriptorImpl var1 = new TableDescriptorFactory.DescriptorImpl();
         int var2 = this.rs.getMetaData().getColumnCount();
         int var3 = 1;

         for (TableDescriptorFactory.Entry var7 : TableDescriptorFactory.Entry.values()) {
            var1.values.put(var7, var7.getValueFromDb(this.rs));
            if (++var3 > var2) {
               break;
            }
         }

         return var1;
      }
   }
}
