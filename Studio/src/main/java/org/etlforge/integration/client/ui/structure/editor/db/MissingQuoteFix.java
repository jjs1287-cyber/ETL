package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbFields;
import org.etlforge.integration.data.structure.database.DbTables;
import org.etlforge.integration.data.structure.database.DbTranFilters;
import org.etlforge.integration.data.structure.database.DbTranRelations;
import org.etlforge.integration.data.structure.database.SqlScript;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.ManualSqlStatement;
import org.etlforge.lang.Arrays2;

final class MissingQuoteFix {
   private final StructureToEdit toEdit;
   private final BeginEndQuote quotes;

   public MissingQuoteFix(StructureToEdit var1, BeginEndQuote var2) {
      assert var1.getStructure().getIncludeSchemaName();
      this.toEdit = var1;
      this.quotes = var2;
   }

   public boolean isApplicable() {
      if (this.quotes.begin.isEmpty()) {
         return false;
      }

      DatabaseObject[] var1 = this.toEdit.getStructure().getDatabaseObjects();
      if (var1.length == 0) {
         return false;
      }

      DatabaseObject var2 = var1[0];
      if (var2 instanceof ManualSqlStatement) {
         return false;
      }

      String var3 = var2.getSchema();
      if (StringUtils.isEmpty(var3)) {
         return false;
      }

      String var4 = var2.getName();
      return var4.startsWith(this.quotes.begin) ? false : var4.startsWith(var3 + ".");
   }

   public StructureToEdit apply() {
      DatabaseObject[] var1 = this.toEdit.getObjects();
      DatabaseObject[] var2 = Arrays2.transform(var1, DatabaseObject.class, new MissingQuoteFix.DatabaseObjectCorrector());
      MissingQuoteFix.DbStructureFix var3 = new MissingQuoteFix.DbStructureFix(this.toEdit.getStructure(), var1, var2);
      var3.apply();
      return new StructureToEdit(this.toEdit.getTransformation(), this.toEdit.getStructure(), var2, this.toEdit.getUse());
   }

   private class DatabaseObjectCorrector implements Function<DatabaseObject, DatabaseObject> {
      private DatabaseObjectCorrector() {
      }

      public DatabaseObject apply(DatabaseObject var1) {
         String var2 = var1.getSchema();
         String var3 = var1.getName();
         if (var3.startsWith(var2 + ".")) {
            var3 = StringUtils.substringAfter(var3, var2 + ".");
         }

         return var1.createCopy(MissingQuoteFix.this.quotes.getQualifiedName(var2, var3));
      }
   }

   private static class DbStructureFix {
      private final DatabaseStructure structure;
      private final List<MissingQuoteFix.NameChange> nameChanges = Lists.newArrayList();
      private final Map<String, MissingQuoteFix.NameChange> nameChangeLookup = Maps.newHashMap();

      public DbStructureFix(DatabaseStructure var1, DatabaseObject[] var2, DatabaseObject[] var3) {
         this.structure = var1;

         for (int var4 = 0; var4 < var2.length; var4++) {
            MissingQuoteFix.NameChange var5 = new MissingQuoteFix.NameChange(var2[var4], var3[var4]);
            this.nameChanges.add(var5);
            this.nameChangeLookup.put(var5.originalName, var5);
         }

         Collections.sort(this.nameChanges);
      }

      public void apply() {
         this.updateDbTables();
         this.updateRelations();
         this.updateFilters();
         this.updateFields();
         this.updateSqlScript();
      }

      private void updateDbTables() {
         DbTables var1 = this.structure.getDbTables();
         String var2 = this.applyAllNameChangesToInput(var1.getTables());
         this.structure.setDbTables(new DbTables(var1.getId(), var2));
      }

      private String applyAllNameChangesToInput(String var1) {
         String var2 = var1;

         for (MissingQuoteFix.NameChange var4 : this.nameChanges) {
            var2 = var4.apply(var2);
         }

         return var2;
      }

      private void updateRelations() {
         List var1 = this.structure.getRelations().getRelations();
         if (!var1.isEmpty()) {
            DbTranRelations var2 = new DbTranRelations();

            for (Object __etlItem124 : var1) {
               DbTranRelations.Relation var4 = (DbTranRelations.Relation) __etlItem124;
               String var5 = var4.getParentName();
               MissingQuoteFix.NameChange var6 = this.nameChangeLookup.get(var5);
               if (var6 == null) {
                  var2.addRelation(var4);
               } else {
                  String var7 = var4.getChildName();
                  MissingQuoteFix.NameChange var8 = this.nameChangeLookup.get(var7);
                  if (var8 == null) {
                     var2.addRelation(var4);
                  } else {
                     var2.addRelation(
                        new DbTranRelations.Relation(
                           var4.getId(), var6.apply(var5), var4.getParentField(), var8.apply(var7), var4.getChildField(), var4.isOuterJoin(), var4.isOneToOne()
                        )
                     );
                  }
               }
            }

            this.structure.setRelations(var2);
         }
      }

      private void updateFilters() {
         DbTranFilters var1 = this.structure.getHierarchicalFilters();
         List var2 = var1.getAllEntries();
         if (!var2.isEmpty()) {
            DbTranFilters var3 = new DbTranFilters();

            for (Object __etlItem154 : var2) {
               DbTranFilters.Entry var5 = (DbTranFilters.Entry) __etlItem154;
               String var6 = var5.getTableName();
               MissingQuoteFix.NameChange var7 = this.nameChangeLookup.get(var6);
               if (var7 == null) {
                  var3.addEntry(var5);
               } else {
                  var3.addEntry(
                     new DbTranFilters.Entry(
                        var5.getId(), var7.apply(var6), var5.getColumnName(), this.applyAllNameChangesToInput(var5.getCondition()), var5.getSortOrder()
                     )
                  );
               }
            }

            this.structure.setHierarchicalFilters(var3);
         }
      }

      private void updateFields() {
         DbFields.Entry[] var1 = this.structure.getDbFields().getAllEntries();
         if (var1.length != 0) {
            DbFields var2 = new DbFields();

            for (DbFields.Entry var6 : var1) {
               var2.addEntry(new DbFields.Entry(var6.getId(), this.applyAllNameChangesToInput(var6.getKeyFields()), var6.getType()));
            }

            this.structure.setDbFields(var2);
         }
      }

      private void updateSqlScript() {
         SqlScript.Entry[] var1 = this.structure.getSqlScript().getAllEntries();
         if (var1.length != 0) {
            SqlScript var2 = new SqlScript();

            for (SqlScript.Entry var6 : var1) {
               if (var6.getType() == 3) {
                  var2.addEntry(var6);
               } else {
                  var2.addEntry(
                     new SqlScript.Entry(
                        var6.getId(), var6.getType(), this.applyAllNameChangesToInput(var6.getScript()), this.applyAllNameChangesToInput(var6.getWhereClause())
                     )
                  );
               }
            }

            this.structure.setSqlScript(var2);
         }
      }
   }

   private static class NameChange implements Comparable<MissingQuoteFix.NameChange> {
      public final String originalName;
      public final String correctedName;

      public NameChange(DatabaseObject var1, DatabaseObject var2) {
         this.originalName = var1.getName();
         this.correctedName = var2.getName();
      }

      public String apply(String var1) {
         return var1 == null ? var1 : var1.replace(this.originalName, this.correctedName);
      }

      public int compareTo(MissingQuoteFix.NameChange var1) {
         return var1.originalName.length() - this.originalName.length();
      }

      @Override
      public String toString() {
         return this.originalName + " -> " + this.correctedName;
      }
   }
}
