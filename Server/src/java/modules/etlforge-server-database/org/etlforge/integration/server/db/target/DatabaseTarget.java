package org.etlforge.integration.server.db.target;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.database.DbInsertMode;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.server.conf.JdbcDriversConfig;
import org.etlforge.integration.server.db.ExternalDatabaseException;
import org.etlforge.integration.server.db.PreparedStatementCache;
import org.etlforge.integration.server.db.ServerDbException;
import org.etlforge.integration.server.db.SourceAndTargetConnections;
import org.etlforge.integration.server.db.utils.DbUtils;
import org.etlforge.integration.server.db.utils.DbUtilsException;
import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.integration.structure.reader.JbNode;
import org.etlforge.integration.structure.reader.TextDocReader;
import org.etlforge.io.KongaIoUtils;

public final class DatabaseTarget {
   public static final String USER_DEFINED_SQLIDENTITY = "<SQLIDENTITY>";
   public static final String USER_DEFINED_NEXTVAL = "<NEXTVAL>";
   public static final String USER_DEFINED_CURRVAL = "<CURRVAL>";
   public static final String USER_DEFINED_UDF = "<UDF>";
   private DbInsertMode mode;
   private boolean isTransaction;
   private String beginQuote = "\"";
   private String endQuote = "\"";
   private Connection targetConnection;
   private final DatabaseTarget.SqlBuilder sqlBuilder = new DatabaseTarget.SqlBuilder();
   private String subprotocol;
   private boolean bDoInsert;
   private boolean bDoUpdate;
   private boolean bSourceUpdate;
   private FileWriter debug_log;
   private long iRecord;
   private String m_sql;
   private List<CROM> m_params;
   private Map<CROM, PreparedStatementCache> statementCache;
   private final List<String> values = Lists.newArrayList();
   private final List<Boolean> isUnmapped = Lists.newArrayList();
   private boolean bIncludeNulls;
   private final Warnings warnings = new Warnings();
   private boolean transactionTerminated;

   public static void writeToTargetDatabase(WriteTargetParams var0, Optional<StringBuilder> var1) throws ExternalDatabaseException, SQLException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      DatabaseTarget var2 = null;
      boolean var3 = false;

      try {
         CROM_Tree var4 = DbUtils.createCromTree(var0.jtrFile);
         CROM var5 = var4.getRoot();
         CROM var6 = var5;
         boolean var7 = var6.isFlat();
         if (var7) {
            var6 = var5.getChild(0);
         }

         var2 = new DatabaseTarget(var0);
         var2.writeLog("Read structure file: ", var0.jtrFile);
         var2.beginQuote = var4.getBeginQuote();
         var2.endQuote = var4.getEndQuote();
         var2.bIncludeNulls = var0.includeNulls;
         OutputStreamWriter var8 = null;
         TextDocReader var9 = null;

         try {
            var2.runPreTargetStatement(var0);
            String var10 = var0.dataFile;
            var8 = new OutputStreamWriter(new FileOutputStream(var10 + ".final"), Charsets.UTF_8);
            var9 = new TextDocReader(new File(var10), Charsets.UTF_8);
            var2.writeLog("Processing target file: ", var10);
            JbNode var55 = var9.parse_all(0, -1);

            for (JbNode var14 : var55.m_children) {
               String var15 = var14.getStr(var9);
               char var16 = var2.parseCsvValues(var15);
               if (!var7) {
                  String var17 = var2.values.get(0);
                  var6 = DbUtils.findTable(var17, var6);
               }

               var2.loadColumns(var6, var7);
               CROM var56 = var2.preWriteToDb(var6);
               if (var2.writeToDb(var6, var16) != DatabaseTarget.MODE.INSERT) {
                  var56 = null;
               }

               if (var56 != null) {
                  var2.postWriteToDb(var6, var56);
               }

               var6.writeDbRecord(var8);
            }

            var2.terminateTransaction(var0, true);
            var3 = true;
         } catch (SQLException var50) {
            StringBuilder var11 = new StringBuilder(var50.getMessage());
            var11.append("\n");
            var11.append(DbUtils.reportSqlForErrorMessage(var2.m_sql, var2.m_params));
            throw new ExternalDatabaseException(var11.toString(), var50);
         } catch (DbUtilsException var51) {
            var2.writeLog(var51.getMessage());
            throw new ExternalDatabaseException(var51.getMessage(), var51);
         } catch (IOException var52) {
            var2.writeLog(var52);
            throw new ExternalDatabaseException(var52.getMessage(), var52);
         } finally {
            KongaIoUtils.close(var8);
            KongaIoUtils.close(var9);
         }
      } finally {
         if (var2 != null) {
            try {
               var2.warnings.collect(var1);
            } finally {
               var2.cleanup(var0, var3);
            }
         }
      }
   }

   private void cleanup(WriteTargetParams var1, boolean var2) throws SQLException {
      try {
         this.terminateTransaction(var1, var2);
      } catch (Exception var64) {
         this.writeLog(var64);
      } finally {
         try {
            this.disposeStatements();
         } finally {
            try {
               this.closeConnection(var2);
            } finally {
               this.closeLogFiles();
            }
         }
      }
   }

   private void terminateTransaction(WriteTargetParams var1, boolean var2) throws SQLException {
      if (!this.transactionTerminated) {
         if (var1.isTransaction) {
            if (var2) {
               this.targetConnection.commit();
            } else {
               this.targetConnection.rollback();
            }
         }

         this.transactionTerminated = true;
      }
   }

   private void disposeStatements() {
      for (PreparedStatementCache var2 : this.statementCache.values()) {
         try {
            var2.dispose();
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }
   }

   private void closeConnection(boolean var1) {
      try {
         this.targetConnection.close();
         this.writeLog("Closed connection, process was ", var1 ? "committed successfully" : "rolled back on error");
      } catch (Exception var3) {
         this.writeLog(var3);
      }
   }

   private void closeLogFiles() {
      KongaIoUtils.close(this.debug_log);
   }

   private char parseCsvValues(String var1) throws DbUtilsException, IOException {
      char[] var2 = var1.toCharArray();
      int var10 = 0;
      int var4 = 0;
      char var6 = ' ';
      char var5;
      if (var2[0] == '<' && var2[2] == '>' && ((var5 = var2[1]) == 'U' || var5 == 'I')) {
         var6 = var5;
         var10 = 3;
      }

      this.values.clear();
      this.isUnmapped.clear();

      for (int var7 = var2.length; var10 < var7; var10++) {
         var5 = var2[var10];
         if (var10 + 2 >= var7
            || var5 != '<'
            || var2[var10 + 1] != 'u'
            || var2[var10 + 2] != '>'
            || var2.length != var10 + 3 && var2[var10 + 3] != ',' && var2[var10 + 3] != '\n' && var2[var10 + 3] != '\r') {
            if (var5 == '"') {
               this.isUnmapped.add(false);
               StringBuilder var8 = new StringBuilder();
               var10++;
               boolean var9 = false;

               for (int var11 = var10; var11 < var7; var11++) {
                  if (var2[var11] == '"') {
                     if (var11 > var10) {
                        var8.append(var1.subSequence(var10, var11));
                     }

                     var10 = ++var11;
                     if (var11 >= var7 || var2[var10] != '"') {
                        var9 = true;
                        break;
                     }
                  }
               }

               if (!var9) {
                  throw new RuntimeException("DatabaseTarget.parseCsvValues failed, cannot find closing quote.\n" + var1);
               }

               this.values.add(var8.toString());
            } else {
               this.isUnmapped.add(false);

               for (var4 = var10; var4 < var7; var4++) {
                  var5 = var2[var4];
                  if (var5 == ',' || var5 == '\n' || var5 == '\r') {
                     break;
                  }
               }

               if (var4 > var10) {
                  this.values.add(var1.substring(var10, var4));
               } else {
                  this.values.add(null);
               }

               if (var5 == '\n' || var5 == '\r') {
                  break;
               }

               var10 = var4;
            }
         } else {
            this.isUnmapped.add(true);
            this.values.add(null);
            var10 += 3;
         }
      }

      return var6;
   }

   private void runPreTargetStatement(WriteTargetParams var1) throws SQLException {
      if (!StringUtils.isEmpty(var1.preTargetSql)) {
         Statement var2 = this.targetConnection.createStatement();

         try {
            this.writeLog("Running pre-target SQL statement: " + var1.preTargetSql);
            var2.execute(var1.preTargetSql);
         } finally {
            var2.close();
         }
      }
   }

   public static boolean isSqlIdentity(CROM var0) {
      String var1 = var0.getValue();
      return var1 == null ? false : var1.equals("<SQLIDENTITY>");
   }

   private static boolean isUserDefinedNextVal(CROM var0) {
      String var1 = var0.getValue();
      return var1 == null ? false : var1.startsWith("<NEXTVAL>");
   }

   private static boolean isUserDefinedCurrVal(CROM var0) {
      String var1 = var0.getValue();
      return var1 == null ? false : var1.startsWith("<CURRVAL>");
   }

   private static boolean isUserDefined(String var0) {
      return var0 != null && !var0.isEmpty() ? var0.startsWith("<CURRVAL>") || var0.startsWith("<NEXTVAL>") || var0.equals("<SQLIDENTITY>") : false;
   }

   public DatabaseTarget(WriteTargetParams var1) throws ExternalDatabaseException, SQLException {
      boolean var2 = false;

      try {
         this.isTransaction = var1.isTransaction;
         this.mode = var1.insertMode;
         if (var1.logFile != null && !var1.logFile.isEmpty()) {
            try {
               this.debug_log = new FileWriter(new File(var1.logFile));
               this.writeLog("Debug logging started");
            } catch (IOException var8) {
               this.debug_log = null;
            }
         }

         SourceAndTargetConnections var3 = new SourceAndTargetConnections();
         this.writeLog("About to connect to the target database");
         this.targetConnection = var3.getConnection(var1.targetId, var1.connectionParameters);
         this.writeLog("Connected to", this.targetConnection.getCatalog());
         if (this.isTransaction) {
            this.writeLog("Setting auto commit to false...");
            this.targetConnection.setAutoCommit(false);
            this.writeLog("..done");
         }

         this.subprotocol = this.getSubProtocol(var1.connectionParameters);
         this.bSourceUpdate = this.mode == DbInsertMode.SOURCE_UPDATE;
         this.iRecord = 0L;
         this.statementCache = Maps.newHashMap();
         var2 = true;
      } catch (ServerDbException var9) {
         throw new ExternalDatabaseException(var9.getMessage(), var9);
      } finally {
         if (!var2 && this.targetConnection != null) {
            this.targetConnection.close();
         }
      }
   }

   private String getSubProtocol(ConnectionParams var1) throws ExternalDatabaseException {
      try {
         JdbcDriversConfig var2 = JdbcDriversConfig.getConfig();
         JdbcDriverDescriptor var3 = var2.getDriverDescriptor(var1.getDriverName());
         String var4 = var3.getSubprotocol();
         this.writeLog("subprotocol=", var4);
         return var4;
      } catch (ServerConfigurationException var5) {
         this.writeLog(var5.toString());
         throw new ExternalDatabaseException(var5.getMessage(), var5);
      }
   }

   private void loadColumns(CROM var1, boolean var2) throws ExternalDatabaseException {
      this.iRecord++;
      int var3 = var2 ? 0 : 1;
      int var4 = this.values.size();

      for (CROM var6 : DbUtils.getTableColumns(var1)) {
         if (!this.bSourceUpdate || var6.isUpdateKey() || var6.isSourceUpdateColumn()) {
            String var7 = var3 < var4 ? this.values.get(var3) : null;
            if (!this.bSourceUpdate && var6.isForeignKey()) {
               var7 = var6.getForeignKeyValue();
               if (var7 == null) {
                  String var8 = "at line " + this.iRecord + ", Find FK link fail, the corresponding PK value is missing";
                  this.writeLog(var8);
                  throw new ExternalDatabaseException(var8);
               }
            }

            var6.setValue(var7);
            var3++;
         }
      }

      if (var4 > var3) {
         String var9 = "at line "
            + this.iRecord
            + ", load columns for table \""
            + var1.getDisplayName()
            + "\" failed, number of columns is ("
            + Integer.toString(var3)
            + ") < number of values ("
            + Integer.toString(var4 - 1)
            + ")";
         this.writeLog(var9);
         throw new ExternalDatabaseException(var9);
      }
   }

   private String sqlLookupWithoutParameters(String var1) throws SQLException {
      this.storeSqlForErrorReporting(var1, null);
      PreparedStatement var2 = this.targetConnection.prepareStatement(var1);
      ResultSet var3 = null;

      try {
         var3 = var2.executeQuery();
         return var3.next() ? var3.getString(1) : "0";
      } finally {
         KongaDbUtils.close(var3);
         KongaDbUtils.close(var2);
      }
   }

   private String getCount(CROM var1, String var2, List<CROM> var3) throws SQLException {
      this.storeSqlForErrorReporting(var2, var3);
      PreparedStatementCache var4 = this.getStatementCacheForTable(var1);
      PreparedStatement var5 = var4.getPreparedStatement(var2);
      boolean var6 = var4.mustLastReturnedStatementBeClosedByCaller();
      ResultSet var7 = null;
      this.writeSqlToLog(var2, var3);

      try {
         int var8 = 1;

         for (CROM var10 : var3) {
            String var11 = var10.getValue();
            DbUtils.bindParameter(var5, var8, var10, var11);
            var8++;
         }

         var7 = var5.executeQuery();
         return var7.next() ? var7.getString(1) : "0";
      } finally {
         KongaDbUtils.close(var7);
         if (var6) {
            KongaDbUtils.close(var5);
         }
      }
   }

   private void doWriteRecord(CROM var1, String var2, List<CROM> var3) throws SQLException {
      PreparedStatementCache var4 = this.getStatementCacheForTable(var1);
      boolean var6 = true;
      PreparedStatement var5;
      if (this.sqlBuilder.hasColumnNonBind()) {
         var5 = var4.createNewStatement(var2);
      } else {
         this.storeSqlForErrorReporting(var2, var3);
         var5 = var4.getPreparedStatement(var2);
         var6 = var4.mustLastReturnedStatementBeClosedByCaller();
      }

      try {
         int var7 = 1;

         for (CROM var9 : var3) {
            String var10 = var9.getValue();

            try {
               DbUtils.bindParameter(var5, var7, var9, var10);
            } catch (Exception var15) {
               throw new SQLException("Failed to bind parameter " + var9.getName() + " to value '" + var10 + "'.  Reason: " + var15.getMessage(), var15);
            }

            var7++;
         }

         var5.execute();
      } finally {
         if (var6) {
            var5.close();
         }
      }
   }

   private void storeSqlForErrorReporting(String var1, List<CROM> var2) {
      this.m_sql = var1;
      this.m_params = var2;
   }

   private PreparedStatementCache getStatementCacheForTable(CROM var1) {
      PreparedStatementCache var2 = this.statementCache.get(var1);
      if (var2 == null) {
         var2 = new PreparedStatementCache(this.targetConnection);
         this.statementCache.put(var1, var2);
      }

      return var2;
   }

   private void addWarning(int var1, String var2) {
      this.warnings.addWarning(var1, var2);
   }

   private void writeLog(Exception var1) {
      this.writeLog(ExceptionUtils.getFullStackTrace(var1));
   }

   private void writeLog(String var1) {
      if (this.debug_log != null && var1 != null) {
         this.writeLog(var1, null);
      }
   }

   private void writeLog(String var1, String var2) {
      if (this.debug_log != null && var1 != null) {
         try {
            this.debug_log.write(var1);
            if (var2 != null) {
               this.debug_log.write(" ");
               this.debug_log.write(var2);
            }

            this.debug_log.write(10);
            this.debug_log.flush();
         } catch (IOException var4) {
         }
      }
   }

   private void writeSqlToLog(String var1, List<CROM> var2) {
      if (this.debug_log != null && var1 != null) {
         try {
            this.debug_log.write("SQL: " + var1);
            this.debug_log.write(10);
            if (var2 != null) {
               if (var2.isEmpty()) {
                  this.debug_log.write("No parameters bound\n");
               } else {
                  StringBuffer var3 = new StringBuffer();
                  var3.append("Bound paramers: ");
                  boolean var4 = false;

                  for (CROM var6 : var2) {
                     if (var4) {
                        var3.append(',');
                     } else {
                        var4 = true;
                     }

                     var3.append(var6.getName()).append('=').append(var6.getValue());
                  }

                  var3.append('\n');
                  this.debug_log.write(var3.toString());
               }
            }

            this.debug_log.flush();
         } catch (IOException var7) {
         }
      }
   }

   private DatabaseTarget.MODE writeToDb(CROM var1, char var2) throws ExternalDatabaseException, SQLException {
      this.bDoInsert = this.mode == DbInsertMode.INSERT_ONLY || this.mode == DbInsertMode.INSERT_UPDATE && var2 != 'U';
      this.bDoUpdate = this.mode == DbInsertMode.INSERT_UPDATE && var2 != 'I' || this.mode == DbInsertMode.UPDATE_ONLY || this.bSourceUpdate;
      this.sqlBuilder.buildSqlForTarget(var1);
      String var3 = this.sqlBuilder.sqlInsert;
      String var4 = this.sqlBuilder.sqlUpdate;
      String var5 = this.sqlBuilder.sqlWhere;
      if (this.bDoUpdate && this.bDoInsert) {
         boolean var10 = false;
         String var7 = this.sqlBuilder.sqlCount;
         if (!var7.isEmpty()) {
            String var8 = this.getCount(var1, var7, this.sqlBuilder.whereParams);
            if (var8 != null && !var8.equals("0")) {
               var10 = true;
            }
         }

         if (!var10) {
            if (!var3.isEmpty()) {
               if (this.debug_log != null) {
                  this.writeSqlToLog(var3, this.sqlBuilder.insertParams);
               }

               this.doWriteRecord(var1, var3, this.sqlBuilder.insertParams);
               return DatabaseTarget.MODE.INSERT;
            } else {
               String var12 = "Insert failed (for insert/update), insert sql is blank for table " + var1.getName() + ".";
               this.writeLog(var12);
               throw new SQLException(var12);
            }
         } else if (!var4.isEmpty()) {
            if (this.debug_log != null) {
               this.writeSqlToLog(var4, this.sqlBuilder.updateParams);
            }

            this.doWriteRecord(var1, var4, this.sqlBuilder.updateParams);
            return DatabaseTarget.MODE.UPDATE;
         } else if (!var5.isEmpty()) {
            this.writeLog("Update record contains only PK or FK, nothing to update for table", var1.getName());
            return DatabaseTarget.MODE.SKIP;
         } else {
            String var11 = "Update failed (for insert/update), update sql is blank for table" + var1.getName() + ".";
            throw new SQLException(var11);
         }
      } else if (this.bDoUpdate) {
         if (!var4.isEmpty()) {
            if (this.debug_log != null) {
               this.writeSqlToLog(var4, this.sqlBuilder.updateParams);
            }

            this.doWriteRecord(var1, var4, this.sqlBuilder.updateParams);
            return DatabaseTarget.MODE.UPDATE;
         } else if (!var5.isEmpty()) {
            this.writeLog("The update record contains only PK or FK, nothing to update.");
            return DatabaseTarget.MODE.SKIP;
         } else {
            String var9 = "Update failed, update sql is blank for table " + var1.getName() + ".";
            throw new SQLException(var9);
         }
      } else if (this.bDoInsert) {
         if (!var3.isEmpty()) {
            if (this.debug_log != null) {
               this.writeSqlToLog(var3, this.sqlBuilder.insertParams);
            }

            this.doWriteRecord(var1, var3, this.sqlBuilder.insertParams);
            return DatabaseTarget.MODE.INSERT;
         } else {
            String var6 = "Insert failed, insert sql is blank for table " + var1.getName() + ".";
            throw new SQLException(var6);
         }
      } else {
         return DatabaseTarget.MODE.FAILED;
      }
   }

   private CROM preWriteToDb(CROM var1) throws ExternalDatabaseException, SQLException {
      if (this.bSourceUpdate) {
         return null;
      }

      for (CROM var3 : var1.getChildren()) {
         if (var3.isAttributeNode()) {
            if (isSqlIdentity(var3) && !var3.isMapped()) {
               if (this.debug_log != null) {
                  this.writeLog("User defined identity column " + var3.getName() + " for column", var1.getName());
               }

               return var3;
            }

            if (!isUserDefinedCurrVal(var3) && (!var3.isPrimaryKey() || var3.isMapped() || var3.isFlat() || var3.isForeignKey())) {
               if (isUserDefinedNextVal(var3)) {
                  String var9 = var3.getValue();
                  String var5 = var9.substring("<NEXTVAL>".length());
                  this.writeSqlToLog(var5, null);
                  String var6 = null;

                  try {
                     var6 = this.sqlLookupWithoutParameters(var5);
                  } catch (SQLException var8) {
                     throw new SQLException("User defined nextval statement \"" + var5 + "\" failed.\n" + var8.getMessage(), var8);
                  }

                  if (var6 == null || var6.isEmpty()) {
                     var6 = "1";
                  }

                  var3.setValue(var6);
                  if (this.debug_log != null) {
                     this.writeLog("Next value for column " + var3.getName() + ", table " + var1.getName() + ":", var6);
                  }

                  return null;
               }

               String var4 = var3.getValueType();
               if (var3.isMapped() || var4 == null || var4.indexOf("identity") < 0 && var4.indexOf("serial") < 0) {
                  continue;
               }

               return var3;
            }

            if (this.debug_log != null) {
               this.writeLog("Found identity column " + var3.getName() + " for column", var1.getName());
            }

            return var3;
         }
      }

      return null;
   }

   private void postWriteToDb(CROM var1, CROM var2) throws SQLException {
      if (var2 != null) {
         String var3 = var1.getSqlTableName();
         if (!StringUtils.isEmpty(var3)) {
            if (isUserDefinedCurrVal(var2)) {
               String var11 = var2.getValue();
               String var12 = var11.substring("<CURRVAL>".length());

               try {
                  String var13 = this.sqlLookupWithoutParameters(var12);
                  var2.setValue(var13);
               } catch (SQLException var9) {
                  throw new SQLException("User defined currval statement failed. " + var9.getMessage(), var9);
               }
            } else {
               String var4 = null;

               try {
                  var4 = DbUtils.getCurrentSequenceSql(this.subprotocol, var3, var2.getDisplayName());
                  this.writeLog("Try to find Sql identity");
                  this.writeSqlToLog(var4, null);
                  String var5 = this.sqlLookupWithoutParameters(var4);
                  this.writeLog("New identity:", var5);
                  var2.setValue(var5);
               } catch (Exception var10) {
                  this.writeLog("Failed to get identity using default sequence SQL:", var10.getMessage());
                  String var6 = String.format("SELECT MAX(%s%s%s) FROM %s", this.beginQuote, var2.getDisplayName(), this.endQuote, var3);
                  this.writeLog("Try executing MAX instead");
                  this.writeSqlToLog(var6, null);
                  String var7 = this.sqlLookupWithoutParameters(var6);
                  this.writeLog("New identity (using MAX):", var7);
                  var2.setValue(var7);
                  if (!this.warnings.hasWarned(Warnings.DBTARGET_WARNING_AUTO_NEXTVAL_FAILED)) {
                     if (var4 != null) {
                        String var8 = "Failed to execute auto generated SQL statement for getting current value of sequence: "
                           + var4
                           + "\nThe error was "
                           + var10.getMessage()
                           + "\nUsing "
                           + var6
                           + " instead. This may not be safe if multiple processes or threads are accessing the table "
                           + var3;
                        this.addWarning(Warnings.DBTARGET_WARNING_AUTO_NEXTVAL_FAILED, var8);
                     } else {
                        String var14 = "Failed to auto generated SQL statement for getting current value of sequence.\nUsing "
                           + var6
                           + " instead. This may not be safe if multiple processes or threads are accessing the table "
                           + var3;
                        this.addWarning(Warnings.DBTARGET_WARNING_AUTO_NEXTVAL_FAILED, var14);
                     }
                  }
               }
            }
         }
      }
   }

   enum MODE {
      INSERT,
      UPDATE,
      DELETE,
      SKIP,
      FAILED;
   }

   private class SqlBuilder {
      private String sqlTableName;
      public String sqlInsert;
      public String sqlUpdate;
      public String sqlWhere;
      public String sqlCount;
      public List<CROM> insertParams = new ArrayList<>();
      public List<CROM> updateParams = new ArrayList<>();
      public List<CROM> whereParams = new ArrayList<>();
      private boolean hasColumnNonBind;

      private SqlBuilder() {
      }

      public boolean hasColumnNonBind() {
         return this.hasColumnNonBind;
      }

      private boolean isUDF(CROM var1) {
         boolean var2 = var1.isUDF();
         if (var2) {
            this.hasColumnNonBind = true;
         }

         return var2;
      }

      private String getUDFValue(String var1) {
         return var1.startsWith("<UDF>") ? var1.substring("<UDF>".length()).trim() : var1;
      }

      public void buildSqlForTarget(CROM var1) throws ExternalDatabaseException {
         this.hasColumnNonBind = false;
         this.sqlTableName = var1.getSqlTableName();
         this.insertParams.clear();
         this.updateParams.clear();
         this.whereParams.clear();
         if (DatabaseTarget.this.bSourceUpdate) {
            this.buildSqlForSourceUpdate(var1);
         } else {
            String var2 = "";
            String var3 = "";
            String var4 = "";
            String var5 = "";
            int var6 = var1.isFlat() ? 0 : 1;

            for (CROM var8 : DbUtils.getTableColumns(var1)) {
               String var9 = var8.getDisplayName();
               boolean var10 = var8.isPrimaryKey();
               boolean var11 = var8.isUpdateKey();
               boolean var12 = var8.isForeignKey();
               boolean var13 = false;
               if (var6 < DatabaseTarget.this.isUnmapped.size()) {
                  var13 = DatabaseTarget.this.isUnmapped.get(var6++);
               }

               if (!var13 || var12) {
                  boolean var14 = var8.isMapped();
                  String var15 = var8.getValue();
                  if (!DatabaseTarget.isUserDefined(var15) && (!var14 || var15 != null || !var10 && DatabaseTarget.this.bIncludeNulls)) {
                     if (var12) {
                        var15 = var8.getForeignKeyValue();
                     } else if (!var14) {
                        continue;
                     }

                     var9 = DatabaseTarget.this.beginQuote + var9 + DatabaseTarget.this.endQuote;
                     boolean var16 = this.isUDF(var8);
                     if (DatabaseTarget.this.bDoInsert) {
                        if (!var2.isEmpty()) {
                           var2 = var2 + ",";
                           var3 = var3 + ",";
                        }

                        var2 = var2 + var9;
                        if (var16) {
                           var3 = var3 + this.getUDFValue(var15);
                        } else {
                           var3 = var3 + "?";
                           this.insertParams.add(var8);
                        }
                     }

                     if (DatabaseTarget.this.bDoUpdate) {
                        if (var11) {
                           if (!var5.isEmpty()) {
                              var5 = var5 + " AND ";
                           }

                           if (var15 == null) {
                              var5 = var5 + "(" + var9 + " IS NULL)";
                           } else if (var16) {
                              var5 = var5 + "(" + var9 + " = " + this.getUDFValue(var15) + ")";
                           } else {
                              var5 = var5 + "(" + var9 + " = ?)";
                              this.whereParams.add(var8);
                           }
                        } else {
                           if (!var4.isEmpty()) {
                              var4 = var4 + ",";
                           }

                           if (var16) {
                              var4 = var4 + var9 + " = " + this.getUDFValue(var15);
                           } else {
                              var4 = var4 + var9 + " = ?";
                              this.updateParams.add(var8);
                           }
                        }
                     }
                  }
               }
            }

            if (DatabaseTarget.this.bDoInsert) {
               if (var2.isEmpty() && var3.isEmpty()) {
                  this.sqlInsert = "";
               } else {
                  this.sqlInsert = String.format("INSERT INTO %s (%s) VALUES (%s)", this.sqlTableName, var2, var3);
               }
            }

            if (DatabaseTarget.this.bDoUpdate) {
               if (!var5.isEmpty() && !var4.isEmpty()) {
                  this.sqlUpdate = "UPDATE " + this.sqlTableName + " SET " + var4 + " WHERE " + var5;
                  this.sqlWhere = var5;
                  if (this.whereParams.size() > 0) {
                     this.updateParams.addAll(this.whereParams);
                  }
               } else {
                  this.sqlUpdate = "";
                  this.updateParams.clear();
                  if (!var5.isEmpty()) {
                     this.sqlWhere = "WHERE " + var5 + " ";
                  } else {
                     this.sqlWhere = "";
                  }
               }
            }

            if (DatabaseTarget.this.bDoInsert && DatabaseTarget.this.bDoUpdate && !var5.isEmpty()) {
               this.sqlCount = "SELECT COUNT(*) FROM " + this.sqlTableName + " WHERE " + var5;
            } else {
               this.sqlCount = "";
            }
         }
      }

      private void buildSqlForSourceUpdate(CROM var1) throws ExternalDatabaseException {
         StringBuilder var2 = new StringBuilder();
         StringBuilder var3 = new StringBuilder();

         for (CROM var5 : var1.getChildren()) {
            if (var5.isElementNode()) {
               break;
            }

            boolean var6 = var5.isUpdateKey();
            if (var6 || var5.isSourceUpdateColumn()) {
               String var7 = var5.getDisplayName();
               String var8 = var5.getValue();
               var7 = DatabaseTarget.this.beginQuote + var7 + DatabaseTarget.this.endQuote;
               if (var6) {
                  if (var3.length() > 0) {
                     var3.append(" AND ");
                  }

                  if (var8 == null) {
                     var3.append("(").append(var7).append(" IS NULL)");
                  } else {
                     var3.append("(").append(var7).append(" = ?)");
                     this.whereParams.add(var5);
                  }
               } else {
                  if (var2.length() > 0) {
                     var2.append(",");
                  }

                  var2.append(var7).append(" = ?");
                  this.updateParams.add(var5);
               }
            }
         }

         this.sqlUpdate = "UPDATE " + this.sqlTableName + " SET " + var2 + " WHERE " + var3;
         this.sqlWhere = var3.toString();
         if (this.whereParams.size() > 0) {
            this.updateParams.addAll(this.whereParams);
         }

         if (var2.length() == 0) {
            throw new ExternalDatabaseException("Source update error: the SET clause is empty.\nUpdate statement is \"" + this.sqlUpdate + "\"");
         }

         if (var3.length() == 0) {
            throw new ExternalDatabaseException("Source update error: the WHERE clause is empty.\nUpdate statement is \"" + this.sqlUpdate + "\"");
         }
      }
   }
}
