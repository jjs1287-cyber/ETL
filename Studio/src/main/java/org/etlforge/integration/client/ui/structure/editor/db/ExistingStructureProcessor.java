package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbTables;
import org.etlforge.integration.data.structure.database.TableUsage;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.ManualSqlStatement;

class ExistingStructureProcessor {
   private final TableSelectionModel model;
   private final StructureToEdit toEdit;
   private final BeginEndQuote quotes;
   private boolean isSchemaNameIncluded;

   public ExistingStructureProcessor(TableSelectionModel var1, StructureToEdit var2) {
      this.model = var1;
      this.toEdit = var2;
      DatabaseLocation var3 = var2.getStructure().getDbLocation();
      this.quotes = this.deriveBeginEndQuotesForEditedStructure(var3);
      this.isSchemaNameIncluded = var2.getStructure().getIncludeSchemaName();
   }

   private BeginEndQuote deriveBeginEndQuotesForEditedStructure(DatabaseLocation var1) {
      BeginEndQuote var2 = var1.getBeginEndQuote();
      if (BeginEndQuote.BLANK.equals(var2)) {
         var2 = this.guessBeginEndQuotes();
      }

      return var2;
   }

   private BeginEndQuote guessBeginEndQuotes() {
      return BeginEndQuoteGuesser.guess(this.toEdit.getObjects());
   }

   public void process() {
      List var1 = this.getTablesToInclude();
      SelectedTables var2 = new SelectedTables();
      var2.includeTables(var1);
      this.setInitialUsageCount(var2);
      this.model.setIncludeSchemaName(this.isSchemaNameIncluded);
      this.model.setUsageCount(var2);
   }

   private List<DatabaseObject> getTablesToInclude() {
      ArrayList var1 = Lists.newArrayList();
      DbTables var2 = this.toEdit.getStructure().getDbTables();

      for (DatabaseObject var6 : this.toEdit.getObjects()) {
         if (!(var6 instanceof ManualSqlStatement)) {
            String var7 = this.quotes.getSimpleName(var6);
            if (!var7.equals(var6.getName())) {
               var6 = var6.createCopy(var7);
               this.isSchemaNameIncluded = true;
            }

            String var8 = this.isSchemaNameIncluded ? this.quotes.getQualifiedName(var6) : var7;
            if (!var2.isAlias(var8)) {
               var1.add(var6);
            }
         }
      }

      return var1;
   }

   private void setInitialUsageCount(SelectedTables var1) {
      DatabaseStructure var2 = this.toEdit.getStructure();
      if (var2.hasDatabaseObjectInformation()) {
         this.updateTableUsageFields(var2, var1);
      } else {
         this.deriveTableUsageFromTableList(var2, var1);
      }
   }

   private void updateTableUsageFields(DatabaseStructure var1, SelectedTables var2) {
      for (DatabaseObject var6 : var2.getIncludedTables()) {
         int var7;
         if (this.isSchemaNameIncluded) {
            String var8 = this.quotes.getQualifiedName(var6);
            DatabaseObject var9 = var6.createCopy(var8);
            var7 = var1.getUseCount(var9);
         } else {
            var7 = var1.getUseCount(var6);
         }

         if (var7 == 0) {
            var7 = 1;
         }

         var2.setTableUsage(var6, var7);
      }
   }

   private void deriveTableUsageFromTableList(DatabaseStructure var1, SelectedTables var2) {
      BeginEndQuote var3 = var1.getBeginEndQuote();
      TableUsage var4 = var1.getDbTables().getTableUsage(var3);

      for (DatabaseObject var8 : var2.getIncludedTables()) {
         int var9 = var4.getUsageCount(var8.getName());
         if (var9 > 0) {
            var2.setTableUsage(var8, var9);
         }
      }
   }
}
