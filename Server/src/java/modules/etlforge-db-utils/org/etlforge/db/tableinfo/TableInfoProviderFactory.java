package org.etlforge.db.tableinfo;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.etlforge.db.DBConnectionInfo;
import org.etlforge.db.KongaDbUtils;

public final class TableInfoProviderFactory {
   private TableInfoProviderFactory() {
   }

   public static TableInfoProvider newProvider(DBConnectionInfo var0) {
      Preconditions.checkNotNull(var0, "connectionInfo");
      return new TableInfoProviderFactory.ProviderImpl(var0);
   }

   private static class ProviderImpl implements TableInfoProvider {
      private final DBConnectionInfo connectionInfo;

      public ProviderImpl(DBConnectionInfo var1) {
         this.connectionInfo = var1;
      }

      @Override
      public TableColumns[] getTableInfo(String var1, String var2, String var3, String[] var4) throws TableInfoProviderException {
         Connection var5 = null;

         try {
            var5 = this.connectionInfo.getConnection(DBConnectionInfo.SUGGESTED_TIMEOUT_FOR_GET_CONNECTION);
            DatabaseMetaData var6 = var5.getMetaData();
            List var7 = this.getTableDescriptors(var6, var1, var2, var3, var4);
            return this.getTableInfoImpl(var6, var7);
         } catch (SQLException var12) {
            throw new TableInfoProviderException(var12);
         } finally {
            KongaDbUtils.close(var5);
         }
      }

      private List<TableDescriptor> getTableDescriptors(DatabaseMetaData var1, String var2, String var3, String var4, String[] var5) throws SQLException {
         ArrayList var6 = new ArrayList();
         ResultSet var7 = null;

         try {
            var7 = var1.getTables(var2, var3, var4, var5);

            while (var7.next()) {
               TableDescriptorFactory var8 = TableDescriptorFactory.newResultSetBasedFactory(var7);
               var6.add(var8.createDescriptor());
            }
         } finally {
            KongaDbUtils.close(var7);
         }

         return var6;
      }

      private TableColumns[] getTableInfoImpl(DatabaseMetaData var1, Iterable<TableDescriptor> var2) throws SQLException {
         ArrayList var3 = new ArrayList();

         for (TableDescriptor var5 : var2) {
            var3.add(this.createTableColumn(var1, var5));
         }

         return (TableColumns[]) var3.toArray(new TableColumns[var3.size()]);
      }

      private TableColumns createTableColumn(DatabaseMetaData var1, TableDescriptor var2) throws SQLException {
         TableInfoProviderFactory.TableColumnsImpl var3 = new TableInfoProviderFactory.TableColumnsImpl(var2);
         this.addColumns(var1, var2, var3);
         this.addPrimaryKeys(var1, var2, var3);
         return var3;
      }

      private void addColumns(DatabaseMetaData var1, TableDescriptor var2, TableInfoProviderFactory.TableColumnsImpl var3) throws SQLException {
         ResultSet var4 = null;

         try {
            var4 = var1.getColumns(var2.catalog(), var2.schema(), var2.tableName(), "%");

            while (var4.next()) {
               ColumnDescriptorFactory var5 = ColumnDescriptorFactory.newResultSetBasedFactory(var4);
               var3.addColumn(var5.createDescriptor());
            }
         } finally {
            KongaDbUtils.close(var4);
         }
      }

      private void addPrimaryKeys(DatabaseMetaData var1, TableDescriptor var2, TableInfoProviderFactory.TableColumnsImpl var3) throws SQLException {
         ResultSet var4 = null;

         try {
            var4 = var1.getPrimaryKeys(var2.catalog(), var2.schema(), var2.tableName());

            while (var4.next()) {
               var3.addPrimaryKey(var4.getString(4));
            }
         } finally {
            KongaDbUtils.close(var4);
         }
      }
   }

   private static class TableColumnsImpl implements TableColumns {
      private final TableDescriptor table;
      private final Map<String, ColumnDescriptor> columns;
      private final Set<String> primaryKeys;

      public TableColumnsImpl(TableDescriptor var1) {
         this.table = var1;
         this.columns = Maps.newHashMap();
         this.primaryKeys = new HashSet<>();
      }

      @Override
      public int getColumnCount() {
         return this.columns.size();
      }

      @Override
      public Iterable<ColumnDescriptor> getColumns() {
         return this.columns.values();
      }

      @Override
      public int getNumberOfPrimaryKeys() {
         return this.primaryKeys.size();
      }

      @Override
      public Iterable<ColumnDescriptor> getPrimaryKeys() {
         ArrayList var1 = new ArrayList();

         for (String var3 : this.primaryKeys) {
            var1.add(this.columns.get(var3));
         }

         return var1;
      }

      @Override
      public TableDescriptor getTable() {
         return this.table;
      }

      public void addColumn(ColumnDescriptor var1) {
         this.columns.put(var1.columnName(), var1);
      }

      public void addPrimaryKey(String var1) {
         if (!this.columns.containsKey(var1)) {
            throw new IllegalArgumentException("Unknown primary key: " + var1);
         }

         this.primaryKeys.add(var1);
      }
   }
}
