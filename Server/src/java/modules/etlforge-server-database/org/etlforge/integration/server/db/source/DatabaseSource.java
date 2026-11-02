package org.etlforge.integration.server.db.source;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.Pair;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.SqlStatementParser;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.server.BackendEncoding;
import org.etlforge.integration.server.db.ExternalDatabaseException;
import org.etlforge.integration.server.db.PreparedStatementCache;
import org.etlforge.integration.server.db.ServerDbException;
import org.etlforge.integration.server.db.SourceAndTargetConnections;
import org.etlforge.integration.server.db.utils.DbUtils;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_DB;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.io.KongaIoUtils;

public final class DatabaseSource {
   private Connection sourceConnection;
   private String beginQuote = "\"";
   private String endQuote = "\"";
   private int nField;
   private String sql;
   private FileWriter debug_log;
   private final Map<CROM, List<CROM>> attributeLists;
   private final Map<CROM, PreparedStatementCache> statementCache;
   private final int fetchSize;
   private final boolean commitBeforeClose;
   private final SourceValueReader sourceValueReader;
   private final ListMultimap<CROM, CROM> childTablesMap;

   public static void createSourceTextDocumentFromJDBC(ReadSourceParams var0) throws ExternalDatabaseException {
      Writer var1 = null;

      try {
         DatabaseSource var2 = new DatabaseSource(var0);

         try {
            CROM_Tree var3 = DbUtils.createCromTree(var0.jtrFile);
            CROM var4 = var3.getRoot();
            var2.beginQuote = var3.getBeginQuote();
            var2.endQuote = var3.getEndQuote();
            writeStructureFileToDisk(var0.structureFile, var4);
            var1 = BackendEncoding.openFileForWriting(new File(var0.dataFile));
            var2.writeLog("Writing database image to " + var0.dataFile);
            var2.dumpSourceDataTree(var4, var0.adHocFilter, var1);
         } catch (IOException var15) {
            var2.writeLog(var15.getMessage());
            throw new ExternalDatabaseException(var15.getMessage(), var15);
         } finally {
            var2.doCleanup();
         }
      } catch (SQLException var17) {
         throw new ExternalDatabaseException(var17.getMessage(), var17);
      } finally {
         closeDataFileWriter(var1);
      }
   }

   private static void closeDataFileWriter(Writer var0) throws ExternalDatabaseException {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var2) {
            throw new ExternalDatabaseException(
               "The file with the source data read from the database could not be written to disk. This may indicate a problem with file permissions on the etlforge server. Please contact ETLForge support.",
               var2
            );
         }
      }
   }

   private DatabaseSource(ReadSourceParams var1) throws ExternalDatabaseException {
      SourceId var2 = var1.sourceId;
      ConnectionParams var3 = var1.connectionParameters;
      this.attributeLists = Maps.newHashMap();
      this.statementCache = Maps.newHashMap();
      this.fetchSize = var1.fetchSize;
      this.sourceValueReader = new SourceValueReader();
      this.childTablesMap = ArrayListMultimap.create();

      try {
         this.initializeDebugLog(var1);
         SourceAndTargetConnections var4 = new SourceAndTargetConnections();
         this.writeLog("About to connect to the source database.");
         this.sourceConnection = var4.getConnection(var2, var3);
         if (this.fetchSize != 0) {
            this.sourceConnection.setAutoCommit(false);
            this.commitBeforeClose = true;
         } else {
            this.commitBeforeClose = false;
         }

         this.writeLog("Connected to " + this.sourceConnection.toString());
      } catch (ServerDbException var5) {
         throw new ExternalDatabaseException(var5.getMessage(), var5);
      } catch (SQLException var6) {
         throw new ExternalDatabaseException(var6.getMessage(), var6);
      }
   }

   private void initializeDebugLog(ReadSourceParams var1) {
      if (StringUtils.isNotEmpty(var1.logFile)) {
         try {
            this.debug_log = new FileWriter(new File(var1.logFile));
            this.debug_log.write("Debug logging started\n");
            this.debug_log.flush();
         } catch (IOException var3) {
            this.debug_log = null;
         }
      }
   }

   private static void writeStructureFileToDisk(String var0, CROM var1) throws IOException {
      if (StringUtils.isNotEmpty(var0)) {
         Writer var2 = BackendEncoding.openFileForWriting(new File(var0));

         try {
            DbUtils.writeStructFile(var2, var1, var1.isFlat());
         } finally {
            KongaIoUtils.close(var2);
         }
      }
   }


   private void writeLog(String var1) {
      if (this.debug_log != null) {
         try {
            this.debug_log.write(var1);
            this.debug_log.write(10);
            this.debug_log.flush();
         } catch (IOException var3) {
         }
      }
   }

   private void doCleanup() throws SQLException {
      this.closeStatementCache();
      this.closeConnection();
      this.writeLog("Closed all statements and connections.");
      KongaIoUtils.close(this.debug_log);
      this.debug_log = null;
   }

   private void closeStatementCache() {
      for (PreparedStatementCache var2 : this.statementCache.values()) {
         try {
            var2.dispose();
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }
   }

   private void closeConnection() throws SQLException {
      if (this.sourceConnection != null) {
         if (this.commitBeforeClose) {
            this.sourceConnection.commit();
         }

         this.sourceConnection.close();
      }
   }

   private void dumpSourceDataTree(CROM var1, String var2, Writer var3) throws ExternalDatabaseException, SQLException, IOException {
      CROM var4 = var1.getChild(0);
      if (var4 == null) {
         String var22 = "No root table in the source.";
         if (this.debug_log != null) {
            this.writeLog(var22);
         }

         throw new ExternalDatabaseException(var22);
      } else {
         this.populateChildTablesMap(var4);
         if (StringUtils.isNotEmpty(var2)) {
            this.modifyRootTableFilter(var4, var2);
         }

         try {
            boolean var5 = var1.isFlat();

            for (CROM var7 : var1.getChildren()) {
               if (var7.isElementNode()) {
                  List var8 = this.childTablesMap.get(var7);
                  this.sql = this.buildSqlForTable(var7);
                  int var9 = this.nField;
                  if (this.debug_log != null) {
                     this.writeLog("SQL for table \"" + var7.getName() + "\": " + this.sql);
                  }

                  PreparedStatement var10 = null;
                  boolean var11 = true;
                  ResultSet var12 = null;

                  try {
                     PreparedStatementCache var13 = this.getStatementCacheForTable(var7);
                     var10 = var13.getPreparedStatement(this.sql);
                     var11 = var13.mustLastReturnedStatementBeClosedByCaller();
                     if (this.debug_log != null) {
                        this.writeLog("About to execute query.");
                     }

                     var12 = var10.executeQuery();
                     if (this.debug_log != null) {
                        this.writeLog("Done executing query.");
                     }

                     int var14 = 0;

                     while (var12.next()) {
                        DatabaseSource.Record var15 = this.createRowRecord(var7, var9, var12);
                        if (this.debug_log != null) {
                           this.writeLog("ADD - " + var15.toString());
                        }

                        var14++;
                        if (this.debug_log != null) {
                           this.writeLog(String.format("%8d rows of table %s added to the parent table %s\n", var14, var7.getName(), var1.getName()));
                        }

                        if (!var8.isEmpty()) {
                           ArrayList var16 = Lists.newArrayList(var15);
                           this.createSourceTreeForTable(var7, var8, var16);
                           this.checkInnerJoin(var16);
                           if (var16.size() == 0) {
                              continue;
                           }
                        }

                        var15.writeToText(var3, var5);
                     }
                  } finally {
                     KongaDbUtils.close(var12);
                     if (var11) {
                        KongaDbUtils.close(var10);
                     }
                  }
               }
            }
         } catch (SQLException var21) {
            String var6 = "SQL:" + this.sql + "\n" + var21.getMessage();
            this.writeLog(var6);
            throw new ExternalDatabaseException(var6);
         }
      }
   }

   private DatabaseSource.Record createRowRecord(CROM var1, int var2, ResultSet var3) {
      DatabaseSource.Record var4 = new DatabaseSource.Record(null, var1);

      for (int var5 = 1; var5 <= var2; var5++) {
         String var6 = null;

         try {
            var6 = this.sourceValueReader.readColumnValueAsString(var3, var5);
         } catch (Exception var8) {
            if (this.debug_log != null) {
               this.writeLog("Failed to get value for table \"" + var1.getName() + "\" field # " + Integer.toString(var5) + ": " + var8.getMessage());
            }
         }

         var4.addColumnValue(var6);
      }

      return var4;
   }

   void checkInnerJoin(List<DatabaseSource.Record> var1) {
      DatabaseSource.Record[] var2 = var1.toArray(new DatabaseSource.Record[var1.size()]);

      for (DatabaseSource.Record var6 : var2) {
         this.checkInnerJoin(var6.childRecords);

         for (CROM var9 : this.childTablesMap.get(var6.table)) {
            if (var9.isInnerJoin() && !var6.hasChildOf(var9)) {
               var1.remove(var6);
               break;
            }
         }
      }
   }

   private void populateChildTablesMap(CROM var1) {
      for (CROM var3 : var1.getChildren()) {
         if (var3.isElementNode()) {
            this.childTablesMap.put(var1, var3);
            this.populateChildTablesMap(var3);
         }
      }
   }

   private void modifyRootTableFilter(CROM var1, String var2) throws ExternalDatabaseException {
      assert StringUtils.isNotEmpty(var2);
      var2 = removeTagFromAdHocFilter(var2);
      if (this.debug_log != null) {
         this.writeLog("adHocFilter=" + var2);
      }

      String var3 = ((CROM_DB)var1.getExtension()).getManualSqlStatement();
      if (StringUtils.isNotEmpty(var3)) {
         this.applyAdHocFilterToManualSqlStatement(var1, var2, var3);
      } else {
         String var4 = "ORDER BY ";
         String var5 = var1.getSqlFilter();
         String var6 = var1.getSqlOrderBy();
         int var7 = var2.indexOf(var4);
         if (var7 >= 0) {
            var5 = var2.substring(0, var7);
            var6 = var2.substring(var7 + var4.length());
            var1.setSqlFilter(var5);
            var1.setSqlOrderBy(var6);
            if (this.debug_log != null) {
               this.writeLog("rootTable.sqlFilter=" + var5);
               this.writeLog("rootTable.sqlOrderBy=" + var6);
            }
         } else {
            var5 = var2;
            var1.setSqlFilter(var5);
            if (this.debug_log != null) {
               this.writeLog("rootTable.sqlFilter=" + var5);
            }
         }
      }
   }

   private static String removeTagFromAdHocFilter(String var0) {
      String var1 = "TransformationsTab.Source_SQL=";
      if (var0.startsWith(var1)) {
         int var2 = var0.length();
         int var3 = var1.length();
         if (var2 > var3 && var0.charAt(var3) == '"') {
            int var4 = var0.indexOf(34, var3 + 1);
            if (var4 > var3) {
               var3 = var4 + 1;
            }
         }

         var3 = var0.indexOf(58, var3);
         return var2 > var3 ? var0.substring(var3 + 1) : "";
      } else {
         return var0;
      }
   }

   private void applyAdHocFilterToManualSqlStatement(CROM var1, String var2, String var3) throws ExternalDatabaseException {
      try {
         SqlStatementParser var4 = new SqlStatementParser(var3);
         String var5 = var4.replaceFilter(var2);
         ((CROM_DB)var1.getExtension()).setManualSqlStatement(var5);
      } catch (Exception var6) {
         throw new ExternalDatabaseException("ETLForge could not modify the manual SQL statement by adding the filter \"" + var2 + "\". ", var6);
      }
   }

   private void createSourceTreeForTable(CROM var1, List<CROM> var2, List<DatabaseSource.Record> var3) throws ExternalDatabaseException, SQLException {
      try {
         List var4 = Lists.newArrayList();

         for (CROM var6 : var2) {
            PreparedStatement var7 = null;
            boolean var8 = true;

            try {
               ArrayList var9 = Lists.newArrayList();
               this.sql = this.buildSqlForTable(var6);
               if (this.debug_log != null) {
                  this.writeLog("nField= " + this.nField);
               }

               if (this.debug_log != null) {
                  this.writeLog("SQL for table \"" + var6.getName() + "\": " + this.sql);
               }

               PreparedStatementCache var10 = this.getStatementCacheForTable(var6);
               var7 = var10.getPreparedStatement(this.sql);
               var8 = var10.mustLastReturnedStatementBeClosedByCaller();
               List var11 = collectLinkKeyIndices(var6);
               if (var11.isEmpty()) {
                  this.handleMissingLinkKeys(var1, var6);
               }

               DatabaseSource.Record[] var12 = var3.toArray(new DatabaseSource.Record[var3.size()]);
               int var13 = var11.size();
               if (this.debug_log != null && var13 > 0) {
                  var4 = this.getAttrListForColumnValues(var1);
               }

               for (DatabaseSource.Record var17 : var12) {
                  ResultSet var18 = null;

                  try {
                     StringBuilder var19 = new StringBuilder();

                     for (int var20 = 0; var20 < var13; var20++) {
                        Pair var21 = (Pair)var11.get(var20);
                        int var22 = (Integer)var21.first;
                        CROM var23 = (CROM)var21.second;
                        String var24 = var17.columnValues.get(var22);
                        if (this.debug_log != null) {
                           this.writeLog(
                              MessageFormat.format(
                                 "Binding parameter {0}, col={1}, type={2}, value={3}", var20 + 1, var23.getName(), var23.getSqlDataType(), var24
                              )
                           );
                        }

                        DbUtils.bindParameter(var7, var20 + 1, var23, var24);
                        if (this.debug_log != null) {
                           if (var20 > 0) {
                              var19.append(",");
                           }

                           var19.append(String.format(" #%d %s=%s", var20 + 1, ((CROM)var4.get(var22)).getName(), var24));
                        }
                     }

                     if (this.debug_log != null && var19.length() > 0) {
                        this.writeLog("SQL - Binding params " + var19);
                     }

                     if (this.debug_log != null) {
                        this.writeLog("About to execute query.");
                     }

                     var18 = var7.executeQuery();
                     if (this.debug_log != null) {
                        this.writeLog("Done executing query.");
                     }

                     int var40;
                     for (var40 = 0; var18.next(); var40++) {
                        if (this.debug_log != null) {
                           this.writeLog("Processing next record");
                        }

                        DatabaseSource.Record var41 = new DatabaseSource.Record(var17, var6);
                        var9.add(var41);

                        for (int var42 = 1; var42 <= this.nField; var42++) {
                           if (this.debug_log != null) {
                              this.writeLog("Getting value " + var42);
                           }

                           String var43 = null;

                           try {
                              var43 = this.sourceValueReader.readColumnValueAsString(var18, var42);
                           } catch (Exception var35) {
                              if (this.debug_log != null) {
                                 this.writeLog(
                                    "Failed to get value for table \"" + var6.getName() + "\" field# " + Integer.toString(var42) + ": " + var35.getMessage()
                                 );
                              }
                           }

                           var41.addColumnValue(var43);
                        }

                        if (this.debug_log != null) {
                           this.writeLog("ADD - " + var41.toString());
                        }
                     }

                     if (this.debug_log != null) {
                        this.writeLog(String.format("%8d rows of table %s add to the parent table %s\n", var40, var6.getName(), var17.table.getName()));
                     }

                     if (var40 == 0 && var6.isInnerJoin()) {
                        var3.remove(var17);
                     }
                  } finally {
                     KongaDbUtils.close(var18);
                  }
               }

               this.createSourceTreeForTable(var6, this.childTablesMap.get(var6), var9);
            } finally {
               if (var8) {
                  KongaDbUtils.close(var7);
               }
            }
         }
      } catch (SQLException var38) {
         String var5 = "SQL: " + this.sql + "\n" + var38.getMessage();
         if (this.debug_log != null) {
            this.writeLog(var5);
         }

         throw new ExternalDatabaseException(var5);
      }
   }

   private void handleMissingLinkKeys(CROM var1, CROM var2) throws ExternalDatabaseException {
      throw new ExternalDatabaseException(
         "ETLForge could not find the link keys between the parent table \""
            + var1.getName()
            + "\" and the child table \""
            + var2.getName()
            + "\". Please edit the transformation, and make sure that the link keys are defined. Then re-deploy the transformation and try to read this database source again."
      );
   }

   private String buildSqlForTable(CROM var1) {
      String var2 = ((CROM_DB)var1.getExtension()).getManualSqlStatement();
      if (StringUtils.isNotEmpty(var2)) {
         this.nField = var1.getChildCount();
         return var2;
      } else {
         String var3 = var1.getSqlTableName();
         String var4 = this.getSelectList(var1);
         String var5 = this.getWhereClause(var1);
         if (var5 == null || var5.isEmpty()) {
            return "SELECT " + var4 + " FROM " + var3;
         } else {
            return var5.startsWith(" ORDER BY") ? "SELECT " + var4 + " FROM " + var3 + var5 : "SELECT " + var4 + " FROM " + var3 + " WHERE " + var5;
         }
      }
   }

   private String getSelectList(CROM var1) {
      StringBuilder var2 = new StringBuilder();
      this.nField = 0;

      for (CROM var4 : var1.getChildren()) {
         if (var4.isElementNode()) {
            break;
         }

         if (var2.length() > 0) {
            var2.append(",");
         }

         var2.append(this.beginQuote).append(var4.getDisplayName()).append(this.endQuote);
         this.nField++;
      }

      return var2.toString();
   }

   private String getWhereClause(CROM var1) {
      StringBuilder var2 = new StringBuilder();
      boolean var3 = false;

      for (CROM var5 : var1.getChildren()) {
         if (var5.isElementNode()) {
            break;
         }

         if (var5.isForeignKey()) {
            if (var2.length() > 0) {
               var2.append(" AND ");
            }

            var2.append("(").append(this.beginQuote).append(var5.getDisplayName()).append(this.endQuote).append("=?)");
            var3 = true;
         }
      }

      String var8 = var1.getSqlFilter();
      String var9 = var1.getSqlOrderBy();
      boolean var6 = StringUtils.isNotEmpty(var8);
      boolean var7 = StringUtils.isNotEmpty(var9);
      if (!var6 && !var7) {
         return var2.toString();
      }

      if (var6) {
         if (var2.length() > 0) {
            var2.append(" AND ");
         }

         if (!var8.startsWith("(") && var3) {
            var2.append("(").append(var8).append(")");
         } else {
            var2.append(var8);
         }
      }

      if (var7) {
         var2.append(" ORDER BY ").append(var9);
      }

      return var2.toString();
   }

   private static List<Pair<Integer, CROM>> collectLinkKeyIndices(CROM var0) throws ExternalDatabaseException {
      ArrayList var1 = Lists.newArrayList();
      CROM var2 = var0.getParent();
      if (var2 == null) {
         return var1;
      }

      for (CROM var4 : var0.getChildren()) {
         if (var4.isElementNode()) {
            break;
         }

         if (var4.isForeignKey()) {
            String var5 = var4.getLinkToPrimaryKey();
            int var6 = 0;
            boolean var7 = false;
            Iterator var8 = var2.getChildren().iterator();

            while (true) {
               if (var8.hasNext()) {
                  CROM var9 = (CROM)var8.next();
                  if (!var9.isElementNode()) {
                     if (!var9.isAttributeNode()) {
                        continue;
                     }

                     if (!var9.getName().equals(var5)) {
                        var6++;
                        continue;
                     }

                     assert var9.isPrimaryKey();
                     var1.add(Pair.of(var6, var4));
                     var7 = true;
                  }
               }

               if (!var7) {
                  throw new ExternalDatabaseException("cannot find linked primary key in the parent table. parent table name=");
               }
               break;
            }
         }
      }

      return var1;
   }

   private List<CROM> getAttrListForColumnValues(CROM var1) {
      assert var1.isElementNode();
      List<CROM> var2 = this.attributeLists.get(var1);
      if (var2 != null) {
         return var2;
      }

      var2 = Lists.newArrayList();

      for (CROM var4 : var1.getChildren()) {
         if (var4.isAttributeNode() && !var4.isPrecondition()) {
            var2.add(var4);
         }
      }

      this.attributeLists.put(var1, var2);
      return var2;
   }

   private PreparedStatementCache getStatementCacheForTable(CROM var1) {
      PreparedStatementCache var2 = this.statementCache.get(var1);
      if (var2 == null) {
         var2 = new PreparedStatementCache(this.sourceConnection);
         this.statementCache.put(var1, var2);
         var2.setFetchSize(this.fetchSize);
      }

      return var2;
   }

   private class Record {
      public final CROM table;
      public final List<String> columnValues = Lists.newArrayList();
      private final List<DatabaseSource.Record> childRecords = Lists.newArrayList();

      public Record(DatabaseSource.Record var2, CROM var3) {
         this.table = var3;
         if (var2 != null) {
            var2.childRecords.add(this);
         }
      }

      public void addColumnValue(String var1) {
         this.columnValues.add(var1);
      }

      public void writeToText(Writer var1, boolean var2) throws IOException {
         String var3 = this.table.getDisplayName();
         if (StringUtils.isNotEmpty(var3)) {
            StringBuilder var4 = new StringBuilder();
            int var5 = 0;
            if (!var2) {
               var4.append(var3);
               var5++;
            }

            for (String var7 : this.columnValues) {
               if (var5 > 0) {
                  var4.append(",");
               }

               if (var7 != null) {
                  if (var7.isEmpty()) {
                     var4.append("\"\"");
                  } else if (DbUtils.csvRequireEscape(var7)) {
                     var4.append(DbUtils.csvEscape(var7));
                  } else {
                     var4.append(var7);
                  }
               }

               var5++;
            }

            var4.append("\n");
            var1.write(var4.toString());
         }

         for (DatabaseSource.Record var9 : this.childRecords) {
            var9.writeToText(var1, var2);
         }
      }

      boolean hasChildOf(CROM var1) {
         for (DatabaseSource.Record var3 : this.childRecords) {
            if (var3.table == var1) {
               return true;
            }
         }

         return false;
      }

      @Override
      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.table.getName()).append(" {");
         List var2 = DatabaseSource.this.getAttrListForColumnValues(this.table);
         int var3 = this.columnValues.size();
         int var4 = var2.size();
         assert var3 == var4;

         for (int var5 = 0; var5 < var3; var5++) {
            if (var5 > 0) {
               var1.append(',');
            }

            CROM var6 = (CROM)var2.get(var5);
            if (var6.isAttributeNode()) {
               var1.append(var6.getName()).append("=");
            }

            var1.append(this.columnValues.get(var5));
         }

         var1.append('}');
         return var1.toString();
      }
   }
}
