package org.etlforge.integration.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public abstract class ColumnData<T> {
   private final String columnName;
   private T value;

   protected ColumnData(String var1) {
      if (var1 == null) {
         throw new NullPointerException("columnName");
      }

      this.columnName = var1;
   }

   public abstract void fetchValue(ResultSet var1) throws SQLException;

   public final T getValue() {
      return this.value;
   }

   protected final String getColumnName() {
      return this.columnName;
   }

   protected final void setValue(T var1) {
      this.value = (T)var1;
   }

   public static ColumnData<Integer> newIntColumn(String var0) {
      return new ColumnData<Integer>(var0) {
         @Override
         public void fetchValue(ResultSet var1) throws SQLException {
            int var2 = var1.getInt(this.getColumnName());
            if (var1.wasNull()) {
               this.setValue(null);
            } else {
               this.setValue(var2);
            }
         }
      };
   }

   public static ColumnData<Long> newLongColumn(String var0) {
      return new ColumnData<Long>(var0) {
         @Override
         public void fetchValue(ResultSet var1) throws SQLException {
            long var2 = var1.getLong(this.getColumnName());
            if (var1.wasNull()) {
               this.setValue(null);
            } else {
               this.setValue(var2);
            }
         }
      };
   }

   public static ColumnData<String> newStringColumn(String var0) {
      return new ColumnData<String>(var0) {
         @Override
         public void fetchValue(ResultSet var1) throws SQLException {
            this.setValue(var1.getString(this.getColumnName()));
         }
      };
   }

   public static ColumnData<Timestamp> newTimestampColumn(String var0) {
      return new ColumnData<Timestamp>(var0) {
         @Override
         public void fetchValue(ResultSet var1) throws SQLException {
            this.setValue(var1.getTimestamp(this.getColumnName()));
         }
      };
   }
}
