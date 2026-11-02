package org.etlforge.integration.database.info.retriever;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.SqlStatementParser;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.lang.KongaStringUtils;

public final class DatabaseColumnRetriever extends DatabaseAccessor {
   private Logger logger = Logger.getAnonymousLogger();
   private BeginEndQuote beginEndQuote;

   public void setBeginEndQuote(BeginEndQuote var1) {
      this.beginEndQuote = var1;
   }

   public void setLogger(Logger var1) {
      Preconditions.checkNotNull(var1, "logger");
      this.logger = var1;
   }

   public List<DatabaseColumn> getColumnInfo(DatabaseObject var1) throws SQLException {
      Preconditions.checkState(this.getConnection() != null, "Must open the connection first");
      DatabaseColumnRetriever.Worker var2 = this.getWorkerForObject(var1);
      Map var3 = var2.collectInfo();
      ArrayList var4 = Lists.newArrayList();

      for (Object __etlItem46 : var3.values()) {
         DatabaseColumnRetriever.ColumnData var6 = (DatabaseColumnRetriever.ColumnData) __etlItem46;
         var4.add(var6.asColumn());
      }

      return var4;
   }

   private DatabaseColumnRetriever.Worker getWorkerForObject(DatabaseObject var1) throws SQLException {
      if (var1.getType() == DatabaseObjectType.MANUAL_SQL) {
         this.logger.info("Looking up column information for a manual SQL statement");
         return new DatabaseColumnRetriever.ManualSqlStatementWorker(var1);
      } else {
         this.logger.info("Looking up column information for a table that has been defined through the wizard");
         return new DatabaseColumnRetriever.MetaDataWorker(var1);
      }
   }

   public void populateColumns(DatabaseObject var1) throws SQLException {
      List var2 = this.getColumnInfo(var1);
      var1.removeAllColumns();

      for (Object __etlItem67 : var2) {
         DatabaseColumn var4 = (DatabaseColumn) __etlItem67;
         var1.addColumn(var4);
      }
   }

   private static String trim(String var0) {
      return var0 != null ? var0.trim() : var0;
   }

   private static final class ColumnData {
      public String name;
      private final int sqlType;
      private final String typeName;
      private final String defVal;
      private final boolean nullable;
      private final int size;
      private final int decimalDigits;
      public boolean pk;

      public ColumnData(ResultSet var1) throws SQLException {
         this.name = DatabaseColumnRetriever.trim(var1.getString(4));
         this.sqlType = var1.getInt(5);
         this.typeName = DatabaseColumnRetriever.trim(var1.getString(6));
         String var2 = KongaStringUtils.removeStartAndEndIfBothPresent(var1.getString(13), "'");
         this.defVal = var2;
         this.nullable = var1.getInt(11) == 1;
         this.size = var1.getInt(7);
         this.decimalDigits = var1.getInt(9);
      }

      public ColumnData(ResultSetMetaData var1, int var2) throws SQLException {
         this.name = DatabaseColumnRetriever.trim(var1.getColumnName(var2));
         this.sqlType = var1.getColumnType(var2);
         this.typeName = DatabaseColumnRetriever.trim(var1.getColumnTypeName(var2));
         this.defVal = null;
         this.nullable = var1.isNullable(var2) == 1;
         this.size = var1.getColumnDisplaySize(var2);
         this.decimalDigits = var1.getScale(var2);
      }

      public DatabaseColumn asColumn() {
         return new DatabaseColumn(null, this.name, this.typeName, this.sqlType, this.defVal, this.nullable, this.pk, this.size, this.decimalDigits);
      }

      @Override
      public String toString() {
         return this.name;
      }
   }

   private final class ManualSqlStatementWorker implements DatabaseColumnRetriever.Worker {
      private final String originalSql;
      @Nullable
      private final String modifiedSql;
      private final Map<String, DatabaseColumnRetriever.ColumnData> columnData = new LinkedHashMap<>();

      public ManualSqlStatementWorker(DatabaseObject var2) throws SQLException {
         assert var2.getType() == DatabaseObjectType.MANUAL_SQL;
         this.originalSql = SqlStatementParser.GetDESubstitudedWithDefaultString(var2.getName());
         this.modifiedSql = this.prepareSqlStatement(this.originalSql);
      }

      @Nullable
      private String prepareSqlStatement(String var1) {
         String var2 = this.subSqlWithoutUnion(var1);

         try {
            SqlStatementParser var3 = new SqlStatementParser(var2);
            String var4 = var3.insertNoneFilter();
            DatabaseColumnRetriever.this.logger.info("Original SQL: \"" + var1 + "\" -- Filtered SQL: \"" + var4 + "\"");
            return SqlStatementParser.GetDESubstitudedWithDefaultString(var4);
         } catch (Exception var5) {
            DatabaseColumnRetriever.this.logger
               .info("ETLForge could not create a test version of the following SQL statement: \"" + var1 + "\". Will use the original SQL statement instead.");
            return null;
         }
      }

      private String subSqlWithoutUnion(String var1) {
         try {
            SqlStatementParser var2 = new SqlStatementParser(var1);
            return var2.subSqlWithoutUnion();
         } catch (Exception var3) {
            var3.printStackTrace();
            return var1;
         }
      }

      @Override
      public Map<String, DatabaseColumnRetriever.ColumnData> collectInfo() throws SQLException {
         if (this.modifiedSql != null) {
            try {
               this.collectColumnInfo(this.modifiedSql);
               return this.columnData;
            } catch (SQLException var2) {
               DatabaseColumnRetriever.this.logger
                  .info("The modified SQL statement failed. Retrying with the original one. Error:\n" + Throwables.getStackTraceAsString(var2));
            }
         }

         this.collectColumnInfo(this.originalSql);
         return this.columnData;
      }

      private void collectColumnInfo(String var1) throws SQLException {
         Statement var2 = DatabaseColumnRetriever.this.getConnection().createStatement();

         try {
            ResultSet var3 = var2.executeQuery(var1);

            try {
               ResultSetMetaData var4 = var3.getMetaData();
               int var5 = 1;

               for (int var6 = var4.getColumnCount(); var5 <= var6; var5++) {
                  DatabaseColumnRetriever.ColumnData var7 = new DatabaseColumnRetriever.ColumnData(var4, var5);
                  this.makeSureColumnNameIsUnique(var7);
                  this.columnData.put(var7.name, var7);
               }
            } finally {
               var3.close();
            }
         } finally {
            var2.close();
         }
      }

      private void makeSureColumnNameIsUnique(DatabaseColumnRetriever.ColumnData var1) {
         for (int var2 = 1; this.columnData.containsKey(var1.name); var2++) {
            var1.name = var1.name + "_" + var2;
         }
      }
   }

   private final class MetaDataWorker implements DatabaseColumnRetriever.Worker {
      private final DatabaseMetaData metaData = DatabaseColumnRetriever.this.getConnection().getMetaData();
      private final String schema;
      private final String tableName;
      private final Map<String, DatabaseColumnRetriever.ColumnData> columnData;

      public MetaDataWorker(DatabaseObject var2) throws SQLException {
         this.schema = this.getSchema(var2);
         BeginEndQuote var3 = this.getBeginEndQuote(this.metaData);
         this.tableName = var3.getSimpleName(var2);
         this.columnData = Maps.newLinkedHashMap();
      }

      private String getSchema(DatabaseObject var1) {
         String var2 = var1.getSchema();
         return StringUtils.isEmpty(var2) ? null : var2;
      }

      private BeginEndQuote getBeginEndQuote(DatabaseMetaData var1) throws SQLException {
         if (DatabaseColumnRetriever.this.beginEndQuote != null) {
            return DatabaseColumnRetriever.this.beginEndQuote;
         }

         String var2 = var1.getIdentifierQuoteString();
         return new BeginEndQuote(var2, var2);
      }

      @Override
      public Map<String, DatabaseColumnRetriever.ColumnData> collectInfo() throws SQLException {
         this.collectColumnInfo();
         this.collectPrimaryKeyInfo();
         return this.columnData;
      }

      private void collectColumnInfo() throws SQLException {
         ResultSet var1 = null;

         try {
            var1 = this.metaData.getColumns(null, this.schema, this.tableName, null);

            while (var1.next()) {
               if (this.isCorrectTable(var1)) {
                  DatabaseColumnRetriever.ColumnData var2 = new DatabaseColumnRetriever.ColumnData(var1);
                  this.columnData.put(var2.name, var2);
               }
            }
         } finally {
            KongaDbUtils.close(var1);
         }
      }

      private boolean isCorrectTable(ResultSet var1) throws SQLException {
         return this.tableName.equalsIgnoreCase(var1.getString(3));
      }

      private void collectPrimaryKeyInfo() {
         ResultSet var1 = null;

         try {
            var1 = this.metaData.getPrimaryKeys(null, this.schema, this.tableName);

            while (var1.next()) {
               if (this.isCorrectTable(var1)) {
                  String var2 = var1.getString(4);
                  DatabaseColumnRetriever.ColumnData var3 = this.columnData.get(DatabaseColumnRetriever.trim(var2));
                  if (var3 != null) {
                     var3.pk = true;
                  }
               }
            }
         } catch (SQLException var7) {
         } finally {
            KongaDbUtils.close(var1);
         }
      }
   }

   private interface Worker {
      Map<String, DatabaseColumnRetriever.ColumnData> collectInfo() throws SQLException;
   }
}
