package com.etlforge.integration.data.download.structure;

import com.google.common.base.Preconditions;
import com.etlforge.integration.data.download.InvalidDownloadedDataException;
import java.util.Collection;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbFields;
import org.etlforge.integration.data.structure.database.DbTables;
import org.etlforge.integration.data.structure.database.DbTranFilters;
import org.etlforge.integration.data.structure.database.DbTranRelations;
import org.etlforge.integration.data.structure.database.SqlScript;

public class DatabaseStructureFactory {
   private final DatabaseLocation location;
   private DbFields dbFields;
   private DbTables dbTables;
   private SqlScript sqlScript;
   private DbTranRelations relations;
   private DbTranFilters treeFilters;

   public DatabaseStructureFactory(DatabaseLocation var1) {
      Preconditions.checkNotNull(var1, "location");
      this.location = var1;
      this.dbFields = new DbFields();
      this.dbTables = new DbTables();
      this.sqlScript = new SqlScript();
      this.relations = new DbTranRelations();
      this.treeFilters = new DbTranFilters();
   }

   public DatabaseStructure buildStructure() {
      DatabaseStructure var1 = new DatabaseStructure(this.location);
      var1.setDbFields(this.dbFields);
      var1.setDbTables(this.dbTables);
      var1.setSqlScript(this.sqlScript);
      var1.setRelations(this.relations);
      var1.setHierarchicalFilters(this.treeFilters);
      return var1;
   }

   public void buildDbFields(Collection<StringKeyValuePairs> var1) throws InvalidDownloadedDataException {
      try {
         for (StringKeyValuePairs var3 : var1) {
            DbFields.Entry var4 = DbFields.Entry.fromColumnValues(var3);
            this.dbFields.addEntry(var4);
         }
      } catch (Exception var5) {
         throw new InvalidDownloadedDataException("Invalid DB Fields data", var5);
      }
   }

   public void buildDbTables(StringKeyValuePairs var1) throws InvalidDownloadedDataException {
      try {
         this.dbTables = DbTables.fromColumnsValues(var1);
      } catch (Exception var3) {
         throw new InvalidDownloadedDataException("Invalid DB Tables data", var3);
      }
   }

   public void buildSqlScript(Collection<StringKeyValuePairs> var1) throws InvalidDownloadedDataException {
      try {
         for (StringKeyValuePairs var3 : var1) {
            SqlScript.Entry var4 = SqlScript.Entry.fromColumnValues(var3);
            this.sqlScript.addEntry(var4);
         }
      } catch (Exception var5) {
         throw new InvalidDownloadedDataException("Invalid SQL Script data", var5);
      }
   }

   public void buildDbTranRelations(Collection<StringKeyValuePairs> var1) throws InvalidDownloadedDataException {
      try {
         for (StringKeyValuePairs var3 : var1) {
            DbTranRelations.Relation.addFromColumnValues(this.relations, var3);
         }
      } catch (Exception var4) {
         throw new InvalidDownloadedDataException("Invalid DB Relations data", var4);
      }
   }

   public void buildTreeFilters(Collection<StringKeyValuePairs> var1) throws InvalidDownloadedDataException {
      try {
         for (StringKeyValuePairs var3 : var1) {
            DbTranFilters.Entry var4 = DbTranFilters.Entry.fromColumnValues(var3);
            this.treeFilters.addEntry(var4);
         }
      } catch (Exception var5) {
         throw new InvalidDownloadedDataException("Invalid DB Tree Filters data", var5);
      }
   }
}
