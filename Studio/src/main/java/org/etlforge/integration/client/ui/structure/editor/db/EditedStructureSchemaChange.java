package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbFields;
import org.etlforge.integration.data.structure.database.DbTranFilters;
import org.etlforge.integration.data.structure.database.DbTranRelations;
import org.etlforge.integration.data.structure.database.FieldRelationship;
import org.etlforge.integration.data.structure.database.TableRelationship;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

abstract class EditedStructureSchemaChange {
   private final DatabaseStructure dbStruct;
   private final DatabaseObject[] tables;
   private final BeginEndQuote quotes;
   private LinkedHashMap<String, DatabaseObject> translatedObjects;

   public EditedStructureSchemaChange(DatabaseStructure var1, DatabaseObject[] var2) {
      this.dbStruct = var1;
      this.tables = var2;
      this.quotes = var1.getBeginEndQuote();
   }

   public final void process() {
      this.processTables();
      this.processRelationships();
      this.processFilters();
      this.processDbFields();
   }

   private void processTables() {
      this.translatedObjects = Maps.newLinkedHashMap();

      for (int var1 = 0; var1 < this.tables.length; var1++) {
         DatabaseObject var2 = this.tables[var1];
         DatabaseObject var3 = this.translate(var2, this.quotes);
         this.translatedObjects.put(var2.getName(), var3);
         this.tables[var1] = var3;
      }
   }

   protected abstract DatabaseObject translate(DatabaseObject var1, BeginEndQuote var2);

   private void processRelationships() {
      DbTranRelations var1 = this.dbStruct.getRelations();
      ArrayList var2 = Lists.newArrayList();

      for (DbTranRelations.Relation var4 : var1.getRelations()) {
         DatabaseObject var5 = this.translatedObjects.get(var4.getParentName());
         DatabaseObject var6 = this.translatedObjects.get(var4.getChildName());
         if (var5 != null && var6 != null) {
            DatabaseColumn var7 = var5.findColumn(var4.getParentField());
            DatabaseColumn var8 = var6.findColumn(var4.getChildField());
            if (var7 != null && var8 != null) {
               TableRelationship var9 = new TableRelationship(var5, var6);
               var9.setRelationshipNature(var4.getCardinality());
               FieldRelationship var10 = new FieldRelationship(var7, var8);
               var2.add(new DbTranRelations.Relation(var9, var10));
            }
         }
      }

      DbTranRelations var11 = new DbTranRelations();

      for (Object __etlItem70 : var2) {
         DbTranRelations.Relation var13 = (DbTranRelations.Relation) __etlItem70;
         var11.addRelation(var13);
      }

      this.dbStruct.setRelations(var11);
   }

   private void processFilters() {
      DbTranFilters var1 = this.dbStruct.getHierarchicalFilters();
      ArrayList var2 = Lists.newArrayList();

      for (DbTranFilters.Entry var4 : var1.getAllEntries()) {
         DatabaseObject var5 = this.translatedObjects.get(var4.getTableName());
         if (var5 != null) {
            DatabaseColumn var6 = var5.getColumn(var4.getColumnName());
            if (var6 != null) {
               var2.add(new DbTranFilters.Entry(var5.getName(), var6.getName(), var4.getCondition(), var4.getSortOrder()));
            }
         }
      }

      DbTranFilters var7 = new DbTranFilters();

      for (Object __etlItem93 : var2) {
         DbTranFilters.Entry var9 = (DbTranFilters.Entry) __etlItem93;
         var7.addEntry(var9);
      }

      this.dbStruct.setHierarchicalFilters(var7);
   }

   private void processDbFields() {
      DbFields var1 = this.dbStruct.getDbFields();
      DbFields var2 = new DbFields();

      for (DbFields.Entry var6 : var1.getAllEntries()) {
         if (var6.getType() == 3) {
            ArrayList var7 = Lists.newArrayList();

            for (String var11 : var6.getKeyFields().split(",")) {
               int var12 = var11.lastIndexOf(46);
               String var13 = var11.substring(0, var12);
               String var14 = var11.substring(var12 + 1);
               DatabaseObject var15 = this.translatedObjects.get(var13);
               if (var15 != null) {
                  DatabaseColumn var16 = var15.findColumn(var14);
                  if (var16 != null) {
                     var7.add(var16);
                  }
               }
            }

            var2.addUpdateKeys(var7);
         }
      }

      this.dbStruct.setDbFields(var2);
   }
}
