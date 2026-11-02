package org.etlforge.db.tableinfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;

public abstract class ColumnDescriptorFactory {
   public abstract ColumnDescriptor createDescriptor() throws SQLException;

   public static ColumnDescriptorFactory newResultSetBasedFactory(ResultSet var0) {
      return new ColumnDescriptorFactory.ResultSetFactory(var0);
   }

   private static class DescriptorImpl implements ColumnDescriptor {
      private final EnumMap<ColumnDescriptorFactory.StringField, String> stringValues = new EnumMap<>(ColumnDescriptorFactory.StringField.class);
      private final EnumMap<ColumnDescriptorFactory.IntField, Integer> intValues = new EnumMap<>(ColumnDescriptorFactory.IntField.class);

      public DescriptorImpl() {
      }

      @Override
      public String catalog() {
         return this.stringValues.get(ColumnDescriptorFactory.StringField.CATALOG);
      }

      @Override
      public int charOctetLength() {
         return this.intValues.get(ColumnDescriptorFactory.IntField.OCTET_LENGTH);
      }

      @Override
      public String columnName() {
         return this.stringValues.get(ColumnDescriptorFactory.StringField.NAME);
      }

      @Override
      public int columnSize() {
         return this.intValues.get(ColumnDescriptorFactory.IntField.COLUMN_SIZE);
      }

      @Override
      public int dataType() {
         return this.intValues.get(ColumnDescriptorFactory.IntField.DATA_TYPE);
      }

      @Override
      public int decimalDigits() {
         return this.intValues.get(ColumnDescriptorFactory.IntField.DECIMAL_DIGITS);
      }

      @Override
      public String defaultValue() {
         return this.stringValues.get(ColumnDescriptorFactory.StringField.DEFAULT_VALUE);
      }

      @Override
      public ColumnIsNullableValue isNullable() {
         String var1 = this.stringValues.get(ColumnDescriptorFactory.StringField.IS_NULLABLE);
         return ColumnIsNullableValue.fromJavaSqlValue(var1);
      }

      @Override
      public ColumnNullableValue nullable() {
         int var1 = this.intValues.get(ColumnDescriptorFactory.IntField.NULLABLE);
         return ColumnNullableValue.fromJavaSqlValue(var1);
      }

      @Override
      public int ordinalPosition() {
         return this.intValues.get(ColumnDescriptorFactory.IntField.ORDINAL_POSITION);
      }

      @Override
      public int radix() {
         return this.intValues.get(ColumnDescriptorFactory.IntField.RADIX);
      }

      @Override
      public String remarks() {
         return this.stringValues.get(ColumnDescriptorFactory.StringField.REMARKS);
      }

      @Override
      public String schema() {
         return this.stringValues.get(ColumnDescriptorFactory.StringField.SCHEMA);
      }

      @Override
      public String tableName() {
         return this.stringValues.get(ColumnDescriptorFactory.StringField.TABLE);
      }

      @Override
      public String typeName() {
         return this.stringValues.get(ColumnDescriptorFactory.StringField.TYPE_NAME);
      }
   }

   private enum IntField {
      DATA_TYPE(5),
      COLUMN_SIZE(7),
      DECIMAL_DIGITS(9),
      RADIX(10),
      NULLABLE(11),
      OCTET_LENGTH(16),
      ORDINAL_POSITION(17);

      private final int index;

      IntField(int var3) {
         this.index = var3;
      }

      public Integer getValueFromDb(ResultSet var1) throws SQLException {
         return var1.getInt(this.index);
      }
   }

   private static class ResultSetFactory extends ColumnDescriptorFactory {
      private final ResultSet rs;

      public ResultSetFactory(ResultSet var1) {
         this.rs = var1;
      }

      @Override
      public ColumnDescriptor createDescriptor() throws SQLException {
         ColumnDescriptorFactory.DescriptorImpl var1 = new ColumnDescriptorFactory.DescriptorImpl();

         for (ColumnDescriptorFactory.StringField var5 : ColumnDescriptorFactory.StringField.values()) {
            var1.stringValues.put(var5, var5.getValueFromDb(this.rs));
         }

         for (ColumnDescriptorFactory.IntField var9 : ColumnDescriptorFactory.IntField.values()) {
            var1.intValues.put(var9, var9.getValueFromDb(this.rs));
         }

         return var1;
      }
   }

   private enum StringField {
      CATALOG(1),
      SCHEMA(2),
      TABLE(3),
      NAME(4),
      TYPE_NAME(6),
      REMARKS(12),
      DEFAULT_VALUE(13),
      IS_NULLABLE(18);

      private final int index;

      StringField(int var3) {
         this.index = var3;
      }

      public String getValueFromDb(ResultSet var1) throws SQLException {
         return var1.getString(this.index);
      }
   }
}
