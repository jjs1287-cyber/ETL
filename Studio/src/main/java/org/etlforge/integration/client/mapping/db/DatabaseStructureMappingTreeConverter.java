package org.etlforge.integration.client.mapping.db;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.BuildTree;
import org.etlforge.collections.TreeItem;
import org.etlforge.integration.client.database.DbColumnInfoCreator;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbFields;
import org.etlforge.integration.data.structure.database.DbTranFilters;
import org.etlforge.integration.data.structure.database.DbTranRelations;
import org.etlforge.integration.data.structure.database.SqlScript;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.InformixUtils;
import org.etlforge.integration.database.info.ManualSqlStatement;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROMHeader;
import org.etlforge.integration.structure.crom.CROM_DB;
import org.etlforge.integration.structure.crom.CROM_ext;
import org.etlforge.integration.structure.crom.CromType;
import org.etlforge.integration.structure.mapping.CromBasedMappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

final class DatabaseStructureMappingTreeConverter {
   private String beginQuote;
   private String endQuote;
   private final Map<String, List<String>> map_UpdateKeys = Maps.newHashMap();
   private final Map<String, String> map_TableAlias = Maps.newHashMap();
   private final Map<String, String> map_Schema = Maps.newHashMap();
   private final Map<String, String> map_Table = Maps.newHashMap();
   private final Map<String, String> map_sqlTableName = Maps.newHashMap();
   private final Set<String> tableNames = Sets.newHashSet();
   private final Logger logger;
   private boolean bInitialized;

   public DatabaseStructureMappingTreeConverter(Logger var1) {
      this.logger = var1;
   }

   private void log(String var1) {
      this.logger.info(var1);
   }

   public MappingTreeStructure convert(DatabaseStructure var1) throws MappingTreeStructureException {
      try {
         BeginEndQuote var2 = var1.getBeginEndQuote();
         this.log("Begin/End quotes: " + var2);
         this.beginQuote = var2.begin;
         this.endQuote = var2.end;
         this.map_UpdateKeys.clear();
         boolean var3 = var1.hasManualSqlStatement();
         if (var3) {
            this.log("Manual SQL statement: " + var1.getManualSqlStatement());
         }

         DatabaseObject[] var4 = this.getDatabaseObjectsFromStructure(var1);
         boolean var5 = var4.length == 0;
         if (var5) {
            this.log("Old-style version - it has either been downloaded from a server, or was created in an earlier version.");
            DbFields var6 = var1.getDbFields();
            DbFields.Entry var7 = var6.getEntry(3);
            if (var7 != null) {
               String var8 = var7.getKeyFields();
               StringTokenizer var9 = new StringTokenizer(var8, ",", false);

               while (var9.hasMoreTokens()) {
                  String var10 = var9.nextToken();
                  String[] var11 = this.getOwnerTableColumnNames(var10);
                  String var12 = var11[1];
                  String var13 = var11[2];
                  List var14 = this.map_UpdateKeys.get(var12);
                  if (var14 == null) {
                     var14 = new ArrayList();
                     this.map_UpdateKeys.put(var12, var14);
                  }

                  var14.add(var13);
               }
            }
         } else {
            for (DatabaseObject var30 : var4) {
               DatabaseColumn[] var35 = var1.getDbFields().getUpdateKeys(var30);
               if (var35 != null && var35.length > 0) {
                  String[] var39 = this.getOwnerTableName(var30.getName());
                  String var45 = var39[2];
                  List var49 = this.map_UpdateKeys.get(var45);
                  if (var49 == null) {
                     var49 = new ArrayList();
                     this.map_UpdateKeys.put(var45, var49);
                  }

                  for (DatabaseColumn var17 : var35) {
                     var49.add(var17.getName());
                  }
               }
            }
         }

         this.initMaps(var1);
         BuildTree var24 = new BuildTree();
         DbTranRelations var26 = var1.getRelations();

         for (DbTranRelations.Relation var31 : var26.getRelations()) {
            String var36;
            String var40;
            if (var5) {
               var36 = this.getOwnerTableName_old(var31.getParentName())[0];
               var40 = this.getOwnerTableName_old(var31.getChildName())[0];
            } else {
               var36 = this.getOwnerTableName(var31.getParentName())[0];
               var40 = this.getOwnerTableName(var31.getChildName())[0];
            }

            TreeItem var46 = var24.addRelation(var36, var40);
            var46.add(var31);
         }

         boolean var29 = false;
         if (var26.getRelations().size() == 0 && this.tableNames.size() == 1) {
            var29 = true;
            String var33 = this.tableNames.iterator().next();
            var24.addRelation(var33, null);
         } else {
            for (String var37 : this.tableNames) {
               boolean var41 = false;

               for (DbTranRelations.Relation var50 : var26.getRelations()) {
                  if (var50.getChildName().equals(var37)) {
                     var41 = true;
                     break;
                  }
               }

               if (!var41) {
                  var24.addRelation(var37, null);
               }
            }
         }

         DbColumnInfoCreator var34 = new DbColumnInfoCreator(var1.getConnectionParameters());
         DbColumnInfoCreator.ColumnInfo var38;
         if (var5) {
            String[][] var42 = this.getOwnerTableNames_old(this.tableNames);
            var38 = var34.createColumnInfo(var42, var1.getSourceId(), var1.getTargetId(), true, false);
         } else {
            DatabaseObject[] var43 = var3 ? var4 : this.constructDatabaseObjectsToSendToServer(var1);
            this.logParamsToColumnInfoFetcher(var43);
            var38 = var34.createColumnInfo(var43, var1.getSourceId(), var1.getTargetId(), true, false);
         }

         int var44 = var38.getStatus();
         if (var44 == 1) {
            this.log("Column info creator was cancelled");
            return null;
         }

         if (var44 != 2 && var44 != 4) {
            if (this.shouldRowIdsBeRemovedFromInformixTables(var1)) {
               var38.removeInformixRowIdColumn();
            }

            TreeItem var48 = var24.getRoot();
            CROM var51 = new CROM(null, true, "", 0, CromType.CROM_t_DB);
            CROM var53 = null;
            var51.addType(32768L);

            for (Object __etlItem181 : var48.getChildren()) {
               TreeItem var55 = (TreeItem) __etlItem181;
               var53 = this.createCromTreeForTable(var51, var53, var55, var29, var38, var3);
            }

            return this.updateProperties(var1, var51);
         } else {
            this.log("Column info failed: " + var38.getException());
            throw new MappingTreeStructureException(var38.getException());
         }
      } catch (Exception var21) {
         Throwables.propagateIfInstanceOf(var21, MappingTreeStructureException.class);
         throw new MappingTreeStructureException(var21);
      } finally {
         this.clean();
      }
   }

   private DatabaseObject[] getDatabaseObjectsFromStructure(DatabaseStructure var1) {
      return var1.hasManualSqlStatement() ? new DatabaseObject[]{new ManualSqlStatement(var1.getManualSqlStatement())} : var1.getDatabaseObjects();
   }

   private void logParamsToColumnInfoFetcher(DatabaseObject[] var1) {
      this.log("createColumnInfo: dbObject.length=" + var1.length + " ");
      String var2 = "[";
      StringBuilder var3 = new StringBuilder();

      for (DatabaseObject var7 : var1) {
         var3.append(var2).append(var7.getSchema()).append("/").append(var7.getName());
         var2 = ",";
      }

      if (var2.equals("[")) {
         this.log("[]");
      } else {
         var3.append("]");
         this.log(var3.toString());
      }
   }

   private boolean shouldRowIdsBeRemovedFromInformixTables(DatabaseStructure var1) {
      return this.isInformix(var1) && !var1.hasManualSqlStatement() && !var1.getIncludeRowIdInInformixTables();
   }

   private boolean isInformix(DatabaseStructure var1) {
      ConnectionParams var2 = var1.getConnectionParameters();
      DriverDescriptor var3 = var2.getDriver();
      return InformixUtils.isInformix(var3);
   }

   private void clean() {
      this.map_Schema.clear();
      this.map_TableAlias.clear();
      this.map_UpdateKeys.clear();
   }

   private DatabaseObject[] constructDatabaseObjectsToSendToServer(DatabaseStructure var1) {
      assert !var1.hasManualSqlStatement();
      DatabaseObject[] var2 = this.getDatabaseObjectsFromStructure(var1);
      ArrayList var3 = new ArrayList();

      for (DatabaseObject var7 : var2) {
         String[] var8 = this.getOwnerTableName(var7.getName());
         String var9 = var8[1];
         if (StringUtils.isEmpty(var9)) {
            var9 = var7.getSchema();
         }

         String var10 = var8[2];
         if (this.map_TableAlias.get(var10) == null) {
            var3.add(var7.createCopy(var9, var10));
         }
      }

      return (DatabaseObject[]) var3.toArray(new DatabaseObject[var3.size()]);
   }

   private void addToTableNames(String var1, String var2) {
      if (var1.length() > 0) {
         String[] var3 = this.getOwnerTableName(var1);
         String var4 = var3[0];
         String var5 = var3[1];
         String var6 = var3[2];
         String var7 = var3[3];
         this.tableNames.add(var4);
         if (var5.length() > 0) {
            this.map_Schema.put(var4, var5);
         }

         this.map_sqlTableName.put(var4, var7);
         this.map_Table.put(var4, var6);
         if (var2.length() > 0) {
            String[] var8 = this.getOwnerTableName(var2);
            String var9 = var8[0];
            this.map_TableAlias.put(var9, var4);
            this.map_sqlTableName.put(var9, var7);
            if (var5.length() > 0) {
               this.map_Schema.put(var9, var5);
            }
         }
      }
   }

   private void processTableNames(String var1) {
      this.map_TableAlias.clear();
      this.map_Schema.clear();
      String var2 = " ," + this.beginQuote + (this.beginQuote.equals(this.endQuote) ? "" : this.endQuote);
      if (!this.beginQuote.equals("\"")) {
         var2 = var2 + "\"";
      }

      StringTokenizer var3 = new StringTokenizer(var1, var2, true);
      boolean var4 = false;
      String var5 = "";
      String var6 = "";

      while (var3.hasMoreTokens()) {
         String var7 = var3.nextToken();
         if (var7.equals(",")) {
            this.addToTableNames(var5, var6);
            var4 = false;
            var5 = "";
            var6 = "";
         } else if (var7.equals(" ")) {
            var4 = true;
         } else {
            if (var7.equals(this.beginQuote)) {
               StringBuilder var8 = new StringBuilder(var7);

               while (var3.hasMoreTokens()) {
                  String var9 = var3.nextToken();
                  var8.append(var9);
                  if (var9.equals(this.endQuote)) {
                     break;
                  }
               }

               var7 = var8.toString();
            } else if (var7.equals("\"")) {
               StringBuilder var10 = new StringBuilder();

               while (var3.hasMoreTokens()) {
                  String var11 = var3.nextToken();
                  if (var11.equals("\"")) {
                     break;
                  }

                  var10.append(var11);
               }

               var7 = var10.toString();
            }

            if (var4) {
               var6 = var6 + var7;
            } else {
               var5 = var5 + var7;
            }
         }
      }

      this.addToTableNames(var5, var6);
   }

   private String[] getOwnerTableName_old(String var1) {
      String var2 = "";
      String var3 = "";
      String var4 = ".:" + this.beginQuote + (this.beginQuote.equals(this.endQuote) ? "" : this.endQuote);
      StringTokenizer var5 = new StringTokenizer(var1, var4, true);

      while (var5.hasMoreTokens()) {
         String var6 = var5.nextToken();
         if (!var6.equals(".") && !var6.equals(":")) {
            if (var6.equals(this.beginQuote)) {
               StringBuilder var7 = new StringBuilder();

               while (var5.hasMoreTokens()) {
                  String var8 = var5.nextToken();
                  if (var8.equals(this.endQuote)) {
                     break;
                  }

                  var7.append(var8);
               }

               var6 = var7.toString();
            }

            var3 = var6;
         } else {
            var2 = var3;
         }
      }

      return new String[]{
         (var2.length() > 0 ? var2 + ":" : "") + var3,
         var2,
         var3,
         (var2.length() > 0 ? this.beginQuote + var2 + this.endQuote + "." : "") + this.beginQuote + var3 + this.endQuote
      };
   }

   private String[] getOwnerTableColumnNames(String var1) {
      String var2 = "";
      String var3 = "";
      String var4 = "";
      String var5 = ".:" + this.beginQuote + (this.beginQuote.equals(this.endQuote) ? "" : this.endQuote);
      StringTokenizer var6 = new StringTokenizer(var1, var5, true);

      while (var6.hasMoreTokens()) {
         String var7 = var6.nextToken();
         if (!var7.equals(".") && !var7.equals(":")) {
            if (var7.equals(this.beginQuote)) {
               StringBuilder var8 = new StringBuilder();

               while (var6.hasMoreTokens()) {
                  String var9 = var6.nextToken();
                  if (var9.equals(this.endQuote)) {
                     break;
                  }

                  var8.append(var9);
               }

               var7 = var8.toString();
            }

            var4 = var7;
         } else {
            var2 = var3;
            var3 = var4;
         }
      }

      return new String[]{var2, var3, var4};
   }

   private String[] getOwnerTableName(String var1) {
      String var2 = "";
      String var3 = "";
      String var4 = "." + this.beginQuote + this.endQuote;
      StringTokenizer var5 = new StringTokenizer(var1, var4, true);

      while (var5.hasMoreTokens()) {
         String var6 = var5.nextToken();
         if (var6.equals(".")) {
            var2 = var3;
            var3 = "";
         } else {
            if (var6.equals(this.beginQuote)) {
               StringBuilder var7 = new StringBuilder();

               while (var5.hasMoreTokens()) {
                  String var8 = var5.nextToken();
                  if (var8.equals(this.endQuote)) {
                     break;
                  }

                  var7.append(var8);
               }

               var6 = var7.toString();
            }

            var3 = var6;
         }
      }

      return new String[]{
         (var2.length() > 0 ? var2 + ":" : "") + var3,
         var2,
         var3,
         (var2.length() > 0 ? this.beginQuote + var2 + this.endQuote + "." : "") + this.beginQuote + var3 + this.endQuote
      };
   }

   private CROM createCromTreeForTable(
      CROM var1, CROM var2, TreeItem<DbTranRelations.Relation> var3, boolean var4, DbColumnInfoCreator.ColumnInfo var5, boolean var6
   ) {
      CROM var7 = null;
      String var8 = var3.getName();
      boolean var9 = false;
      List var10 = var3.getData();
      DbTranRelations.Relation var11 = null;
      if (var10.size() > 0) {
         var11 = (DbTranRelations.Relation)var10.get(0);
      }

      if (var8 != null) {
         boolean var12 = true;
         var7 = new CROM(var1, var12, var8, -1, CromType.CROM_t_DB);
         String var13 = this.map_Schema.get(var8);
         if (var13 != null) {
            var7.setDBSchema(var13);
         }

         String var14 = this.map_TableAlias.get(var8);
         if (var14 == null) {
            var14 = var8;
         }

         var7.setSqlTableName(this.map_sqlTableName.get(var14));
         String var15 = var13 != null && var13.length() > 0 ? var14.substring(var13.length() + 1) : var14;
         List var16 = this.map_UpdateKeys.get(var15);
         boolean var17 = var16 != null && var16.size() > 0;
         boolean var18 = false;
         boolean var19 = true;
         if (var11 != null) {
            var18 = var11.isOneToOne();
            var19 = var11.isOuterJoin();
         }

         if (var18) {
            if (var19) {
               var7.setOptionalNode();
            }
         } else if (var19) {
            var7.setCollectionNode();
         } else {
            var7.setAtLeastOneNode();
         }

         ArrayList var20 = new ArrayList();

         for (TreeItem var22 : var3.getChildren()) {
            for (Object __etlItem505 : var22.getData()) {
               DbTranRelations.Relation var25 = (DbTranRelations.Relation) __etlItem505;
               var20.add(var25.getParentField());
            }
         }

         List var45 = var5.getTableColumnInfo(var14, var6);
         this.log("Table \"" + var14 + "\" has " + (var45 == null ? 0 : var45.size()) + " columns.");
         if (var45 != null) {
            for (Object __etlItem513 : var45) {
               DatabaseColumn var47 = (DatabaseColumn) __etlItem513;
               boolean var49 = false;
               boolean var26 = false;
               String var27 = "";
               String var28 = null;
               String var29 = var47.getName();
               StringBuilder var30 = new StringBuilder(var47.getDataTypeString());
               int var31 = var47.getSqlDataType();
               if (!var9) {
                  var27 = var47.getDefaultValueString();
               }

               boolean var32 = var47.isNullable();
               boolean var33 = false;
               int var34 = var47.getColumnSize();
               int var35 = var47.getDecimalDigits();
               String var36 = var29;

               for (Object __etlItem531 : var20) {
                  String var38 = (String) __etlItem531;
                  if (var29.equals(var38)) {
                     var33 = true;
                     break;
                  }
               }

               boolean var50 = checkIfUpdateKey(var16, var17, var29, var33);

               for (Object __etlItem540 : var10) {
                  DbTranRelations.Relation var39 = (DbTranRelations.Relation) __etlItem540;
                  String var40 = var39.getChildField();
                  String var41 = var39.getParentField();
                  if (this.beginQuote != null && this.endQuote != null && !this.beginQuote.isEmpty() && !this.endQuote.isEmpty()) {
                     if (var40.startsWith(this.beginQuote) && var40.endsWith(this.endQuote) && var40.length() > 2) {
                        var40 = var40.substring(1, var40.length() - 1);
                     }

                     if (var41.startsWith(this.beginQuote) && var41.endsWith(this.endQuote) && var41.length() > 2) {
                        var41 = var41.substring(1, var41.length() - 1);
                     }
                  }

                  if (var29.equals(var40)) {
                     var26 = true;
                     var30.append(" [Join on ").append(var41).append("]");
                     var28 = var41;
                  }
               }

               CROM var48 = new CROM(var7, var49, var36, -1, CromType.CROM_t_DB);
               if (var32) {
                  var48.setOptionalNode();
               }

               if (var33) {
                  var48.setPrimaryKey();
               }

               if (var50) {
                  var48.setUpdateKey();
               }

               CROM_ext var52 = var48.getExtension();
               if (var26) {
                  var48.setLinkKey();
                  var48.setLinkToPrimaryKey(var28);
               }

               var52.setValueType(var30.toString());
               var52.setDbColType(var31);
               var52.setDbColSize(var34);
               var52.setDbDecimalDigits(var35);
               var52.setDefaultValue(var27);
            }
         }
      }

      CROM var42 = null;

      for (TreeItem var44 : var3.getChildren()) {
         var42 = this.createCromTreeForTable(var7, var42, var44, var4, var5, var6);
      }

      return var7;
   }

   private static boolean checkIfUpdateKey(List<String> var0, boolean var1, String var2, boolean var3) {
      boolean var4 = false;
      if (!var1) {
         var4 = var3;
      } else {
         for (String var6 : var0) {
            if (var6.equals(var2)) {
               var4 = true;
               break;
            }
         }
      }

      return var4;
   }

   private void addFilterToCrom(CROM var1, Map<String, String> var2, Map<String, String> var3) {
      String var4 = var1.getDisplayName();
      String var5 = (String)var2.get(var4);
      String var6 = (String)var3.get(var4);
      if (var5 != null || var6 != null) {
         if (var5 != null) {
            var1.setSqlFilter(var5);
         }

         if (var6 != null) {
            var1.setSqlOrderBy(var6);
         }
      }

      for (CROM var8 : var1.getChildren()) {
         if (!var8.isLeaf()) {
            this.addFilterToCrom(var8, var2, var3);
         }
      }
   }

   private String[][] getOwnerTableNames_old(Set<String> var1) {
      int var2 = var1.size();
      String[] var3 = var1.toArray(new String[var1.size()]);
      String[][] var4 = new String[var2][2];
      int var5 = 0;

      for (String var9 : var3) {
         String var10 = this.map_Schema.get(var9);
         if (var10 != null && var10.length() != 0) {
            var4[var5][0] = var10;
         } else {
            var4[var5][0] = null;
         }

         var4[var5][1] = this.map_Table.get(var9);
         var5++;
      }

      return var4;
   }

   private void initMaps(DatabaseStructure var1) {
      if (!this.bInitialized) {
         if (var1.hasManualSqlStatement()) {
            this.tableNames.add(var1.getManualSqlStatement());
         } else {
            String var2 = var1.getDbTables().getTables();
            this.processTableNames(var2);
         }

         this.bInitialized = true;
      }
   }

   private String getSqlTableName(String var1, String var2) {
      return var2 != null && !var2.isEmpty() && var1.startsWith(var2)
         ? this.beginQuote + var2 + this.endQuote + "." + this.beginQuote + var1.substring(var2.length() + 1) + this.endQuote
         : this.beginQuote + var1 + this.endQuote;
   }

   private MappingTreeStructure updateProperties(DatabaseStructure var1, CROM var2) {
      BeginEndQuote var3 = var1.getBeginEndQuote();
      this.beginQuote = var3.begin;
      this.endQuote = var3.end;
      this.initMaps(var1);
      if (var2.hasChildren() && var1.hasManualSqlStatement()) {
         CROM var4 = var2.getChild(0);
         var4.setName("Manual SQL");
         var4.setDisplayName("Manual SQL");
         ((CROM_DB)var4.getExtension()).setManualSqlStatement(var1.getManualSqlStatement());
      }

      HashMap var15 = Maps.newHashMap();
      HashMap var5 = Maps.newHashMap();
      var15.clear();
      var5.clear();

      for (DbTranFilters.Entry var7 : var1.getHierarchicalFilters().getAllEntries()) {
         String[] var8 = this.getOwnerTableName(var7.getTableName());
         String var9 = var8[0];
         String var10 = this.map_TableAlias.get(var9);
         if (var10 == null) {
            var10 = var9;
         }

         var10 = this.getSqlTableName(var10, var8[1]);
         String var11 = var10 + "." + this.beginQuote + var7.getColumnName() + this.endQuote;
         String var12 = var7.getCondition();
         String var13 = var7.getSortOrder().getAbbreviation();
         if (var13.equals("NONE")) {
            var13 = null;
         }

         if (var12 != null && !var12.isEmpty()) {
            String var14 = (String)var15.get(var9);
            var14 = var14 != null ? var14 + " AND " : "";
            var15.put(var9, var14 + "(" + var11 + " " + var12 + ")");
         }

         if (var13 != null && !var13.isEmpty()) {
            String var24 = (String)var5.get(var9);
            var24 = var24 != null ? var24 + ", " : "";
            var5.put(var9, var24 + var11 + " " + var13);
         }
      }

      this.addFilterToCrom(var2, var15, var5);
      if (var1.getRelations().getRelations().size() == 0 && this.tableNames.size() == 1) {
         SqlScript var16 = var1.getSqlScript();
         String var17 = var16.getWhereClause();
         if (StringUtils.isNotEmpty(var17)) {
            CROM var18 = var2.getChild(0);
            if (var18 != null) {
               String var19 = "ORDER BY ";
               int var21 = var17.indexOf(var19);
               if (var21 >= 0) {
                  String var22 = var17.substring(var21 + var19.length());
                  var18.setSqlOrderBy(var22);
                  if (var21 > 0) {
                     var18.setSqlFilter(var17.substring(0, var21));
                  }
               } else {
                  var18.setSqlFilter(var17);
               }
            }
         }
      }

      return new CromBasedMappingTreeStructure(var2, new CROMHeader("DB", this.beginQuote, this.endQuote));
   }
}
