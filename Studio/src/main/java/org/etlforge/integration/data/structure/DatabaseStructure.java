package org.etlforge.integration.data.structure;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.structure.database.AliasNameGenerator;
import org.etlforge.integration.data.structure.database.DbFields;
import org.etlforge.integration.data.structure.database.DbFieldsPersistor;
import org.etlforge.integration.data.structure.database.DbTables;
import org.etlforge.integration.data.structure.database.DbTranFilters;
import org.etlforge.integration.data.structure.database.DbTranFiltersPersistor;
import org.etlforge.integration.data.structure.database.DbTranRelations;
import org.etlforge.integration.data.structure.database.DbTranRelationsPersistor;
import org.etlforge.integration.data.structure.database.SqlScript;
import org.etlforge.integration.data.structure.database.SqlScriptPersistor;
import org.etlforge.integration.data.structure.database.TableUsage;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectPersistor;
import org.etlforge.system.annotations.InternalConstructor;
import org.etlforge.system.annotations.InternalMethod;
import org.etlforge.util.persist.Persistor;

public final class DatabaseStructure extends DataStructure {
   public static final String DB_TABLES = "DatabaseStructure#Tables";
   public static final String DB_FIELDS = "DatabaseStructure#Fields";
   public static final String SQL_SCRIPT = "DatabaseStructure#SqlScript";
   public static final String DB_RELATIONS = "DatabaseStructure#Relations";
   public static final String DB_FILTERS = "DatabaseStructure#Filters";
   public static final String TRUNCATE_TARGET_FIELDS = "DatabaseStructure#TruncateTargetFields";
   private static final String INCLUDE_SCHEMA_NAME = "includeSchemaName";
   private static final String INCLUDE_ROWID_IN_INFORMIX_TABLE = "informix.include_row_id";
   private DbTables dbTables = new DbTables();
   private DbFields dbFields = new DbFields();
   private SqlScript sqlScript = new SqlScript();
   private DbTranRelations relations = new DbTranRelations();
   private DbTranFilters treeFilters = new DbTranFilters();
   private DatabaseLocation location;
   private SourceId sourceId;
   private TargetId targetId;
   private Map<DatabaseObject, Integer> objects;

   @InternalConstructor
   public DatabaseStructure() {
      super(DataStructureType.Database);
      this.objects = Maps.newHashMap();
   }

   public DatabaseStructure(DatabaseLocation var1) {
      super(DataStructureType.Database);
      this.location = Preconditions.checkNotNull(var1);
      this.objects = Maps.newHashMap();
   }

   public DatabaseStructure withNewSourceLocation(Source var1) {
      DatabaseLocation var2 = (DatabaseLocation)var1.getLocation();
      synchronized (this.getDataLock()) {
         Preconditions.checkState(this.targetId == null, "This structure represents a target structure");
         if (var1.getID().equals(this.sourceId)) {
            return this;
         }

         DatabaseStructure var4 = this.withNewLocation(var2);
         var4.sourceId = var1.getID();
         return var4;
      }
   }

   private DatabaseStructure withNewLocation(DatabaseLocation var1) {
      DatabaseStructure var2 = new DatabaseStructure(var1);
      var2.setDbTables(this.dbTables);
      var2.setDbFields(this.dbFields);
      var2.setDatabaseObjectsUsage(this.objects);
      var2.setRelations(this.relations);
      var2.setSqlScript(this.sqlScript);
      var2.setHierarchicalFilters(this.treeFilters);
      var2.setIncludeSchemaName(this.getIncludeSchemaName());
      var2.setIncludeRowIdInInformixTables(this.getIncludeRowIdInInformixTables());
      var2.setTruncateTargetFields(this.isTruncatingTargetFields());
      return var2;
   }

   public DatabaseStructure withNewTargetLocation(Target var1) {
      DatabaseLocation var2 = (DatabaseLocation)var1.getLocation();
      synchronized (this.getDataLock()) {
         Preconditions.checkState(this.sourceId == null, "This structure represents a source structure");
         if (var1.getID().equals(this.targetId)) {
            return this;
         }

         DatabaseStructure var4 = this.withNewLocation(var2);
         var4.targetId = var1.getID();
         return var4;
      }
   }

   public void setSourceId(SourceId var1) {
      synchronized (this.getDataLock()) {
         this.sourceId = var1;
      }
   }

   public SourceId getSourceId() {
      synchronized (this.getDataLock()) {
         return this.sourceId;
      }
   }

   public void setTargetId(TargetId var1) {
      synchronized (this.getDataLock()) {
         this.targetId = var1;
      }
   }

   public TargetId getTargetId() {
      synchronized (this.getDataLock()) {
         return this.targetId;
      }
   }

   public final DbTables getDbTables() {
      synchronized (this.getDataLock()) {
         return this.dbTables.getCopy();
      }
   }

   public void setDbTables(DbTables var1) {
      DbTables var2;
      synchronized (this.getDataLock()) {
         var2 = this.dbTables;
         this.dbTables = var1 == null ? new DbTables() : var1.getCopy();
      }

      if (differs(var1, var2)) {
         this.firePropertyChange("DatabaseStructure#Tables", var2, var1);
      }
   }

   public final DbFields getDbFields() {
      synchronized (this.getDataLock()) {
         return this.dbFields.getCopy();
      }
   }

   public final void setDbFields(DbFields var1) {
      DbFields var2;
      synchronized (this.getDataLock()) {
         var2 = this.dbFields;
         this.dbFields = var1 == null ? new DbFields() : var1.getCopy();
      }

      if (differs(var1, var2)) {
         this.firePropertyChange("DatabaseStructure#Fields", var2, var1);
      }
   }

   public DbTranRelations getRelations() {
      synchronized (this.getDataLock()) {
         return this.relations.getCopy();
      }
   }

   public void setRelations(DbTranRelations var1) {
      DbTranRelations var2;
      synchronized (this.getDataLock()) {
         var2 = this.relations;
         this.relations = var1 == null ? new DbTranRelations() : var1.getCopy();
      }

      if (differs(var1, var2)) {
         this.firePropertyChange("DatabaseStructure#Relations", var2, var1);
      }
   }

   public DbTranFilters getHierarchicalFilters() {
      synchronized (this.getDataLock()) {
         return this.treeFilters.getCopy();
      }
   }

   public void setHierarchicalFilters(DbTranFilters var1) {
      DbTranFilters var2;
      synchronized (this.getDataLock()) {
         var2 = this.treeFilters;
         this.treeFilters = var1 == null ? new DbTranFilters() : var1.getCopy();
      }

      if (differs(var1, var2)) {
         this.firePropertyChange("DatabaseStructure#Filters", var2, var1);
      }
   }

   public SqlScript getSqlScript() {
      synchronized (this.getDataLock()) {
         return this.sqlScript.getCopy();
      }
   }

   public void setSqlScript(SqlScript var1) {
      SqlScript var2;
      synchronized (this.getDataLock()) {
         var2 = this.sqlScript;
         this.sqlScript = var1 == null ? new SqlScript() : var1.getCopy();
      }

      if (differs(var2, var1)) {
         this.firePropertyChange("DatabaseStructure#SqlScript", var2, var1);
      }
   }

   @Override
   public StructureNature getStructureNature() {
      synchronized (this.getDataLock()) {
         return this.dbTables.getNumberOfTables() < 2 ? StructureNature.Flat : StructureNature.Tree;
      }
   }

   public DatabaseLocation getDbLocation() {
      synchronized (this.getDataLock()) {
         return this.location == null ? null : new DatabaseLocation(this.location);
      }
   }

   public ConnectionParams getConnectionParameters() {
      synchronized (this.getDataLock()) {
         return this.location == null ? null : this.location.getConnectionParams();
      }
   }

   public DatabaseLocation getUnprotectedDbLocation() {
      synchronized (this.getDataLock()) {
         return this.location;
      }
   }

   @InternalMethod
   public void setDbLocation(DatabaseLocation var1) {
      synchronized (this.getDataLock()) {
         this.location = var1;
      }
   }

   @Override
   public boolean isCompatibleWith(DataLocation var1) {
      return var1 instanceof DatabaseLocation;
   }

   @Override
   public boolean equals(Object var1) {
      return this.equalsImpl(var1, true);
   }

   private boolean equalsImpl(Object var1, boolean var2) {
      if (var1 == this) {
         return true;
      }

      synchronized (this.getDataLock()) {
         if (!super.equals(var1)) {
            return false;
         }

         DatabaseStructure var4 = (DatabaseStructure)var1;
         return this.dbTables.equals(var4.dbTables)
            && this.dbFields.equals(var4.dbFields)
            && this.relations.equals(var4.relations)
            && this.treeFilters.equals(var4.treeFilters)
            && (!var2 || this.sqlScript.equals(var4.sqlScript))
            && areEqual(this.location, var4.location);
      }
   }

   private static final boolean areEqual(Object var0, Object var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   @Override
   public int hashCode() {
      synchronized (this.getDataLock()) {
         int var2 = super.hashCode();
         var2 = 37 * var2 + this.dbTables.hashCode();
         var2 = 37 * var2 + this.dbFields.hashCode();
         var2 = 37 * var2 + this.relations.hashCode();
         var2 = 37 * var2 + this.treeFilters.hashCode();
         var2 = 37 * var2 + this.sqlScript.hashCode();
         return 37 * var2 + (this.location == null ? 0 : this.location.hashCode());
      }
   }

   protected static boolean differs(Object var0, Object var1) {
      return var0 == null && var1 != null || var0 != null && !var0.equals(var1);
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("Tables: ");
      var1.append(this.dbTables.getTables());
      var1.append("\n");
      var1.append("FIELDS:\n");
      var1.append(this.dbFields);
      var1.append("SQLSCRIPT:\n");
      var1.append(this.sqlScript);
      var1.append("\n");
      var1.append("TREE FILTER:\n");
      var1.append(this.treeFilters);
      var1.append("\n");
      var1.append("RELATIONS:\n");
      var1.append(this.relations);
      var1.append("\n");
      return var1.toString();
   }

   public DatabaseStructure clone() {
      synchronized (this.getDataLock()) {
         DatabaseStructure var2 = (DatabaseStructure)super.clone();
         var2.dbTables = this.dbTables.getCopy();
         var2.dbFields = this.dbFields.getCopy();
         var2.relations = this.relations.getCopy();
         var2.sqlScript = this.sqlScript.getCopy();
         var2.treeFilters = this.treeFilters.getCopy();
         var2.objects = Maps.newHashMap(this.objects);
         return var2;
      }
   }

   @Override
   public boolean isCompatibleWith(DataStructure var1) {
      return true;
   }

   public void setDatabaseObjectsUsage(Map<DatabaseObject, Integer> var1) {
      synchronized (this.getDataLock()) {
         this.objects = Maps.newHashMap(var1);
      }
   }

   public Map<DatabaseObject, Integer> getDatabaseObjectsUsage() {
      synchronized (this.getDataLock()) {
         return Maps.newHashMap(this.objects);
      }
   }

   public DatabaseObject[] getDatabaseObjects() {
      synchronized (this.getDataLock()) {
         BeginEndQuote var2 = this.getBeginEndQuote();
         ArrayList var3 = Lists.newArrayList();

         for (Entry var5 : this.objects.entrySet()) {
            DatabaseObject var6 = (DatabaseObject)var5.getKey();
            var3.add(var6);

            for (int var7 = 2; var7 <= (Integer)var5.getValue(); var7++) {
               DatabaseObject var8 = AliasNameGenerator.createAlias(var6, var7, var2);
               var3.add(var8);
            }
         }

         return (DatabaseObject[]) var3.toArray(new DatabaseObject[var3.size()]);
      }
   }

   public int getUseCount(DatabaseObject var1) {
      synchronized (this.getDataLock()) {
         Integer var3 = this.objects.get(var1);
         return var3 != null ? var3 : 0;
      }
   }

   public boolean hasDatabaseObjectInformation() {
      synchronized (this.getDataLock()) {
         return !this.objects.isEmpty();
      }
   }

   public void setIncludeSchemaName(boolean var1) {
      this.setBooleanProperty("includeSchemaName", var1);
   }

   public boolean getIncludeSchemaName() {
      return this.getBooleanProperty("includeSchemaName", false);
   }

   public void setTruncateTargetFields(boolean var1) {
      this.setBooleanProperty("DatabaseStructure#TruncateTargetFields", var1);
   }

   public boolean isTruncatingTargetFields() {
      return this.getBooleanProperty("DatabaseStructure#TruncateTargetFields", false);
   }

   public boolean isManualSqlStatement() {
      synchronized (this.getDataLock()) {
         return this.sqlScript.isManualSqlStatement();
      }
   }

   @Override
   public void restoreFrom(Persistor var1) {
      synchronized (this.getDataLock()) {
         super.restoreFrom(var1);
         this.restoreSourceId(var1);
         this.restoreTargetId(var1);
         this.restoreObjects(var1);
         this.dbTables = new DbTables();
         this.dbTables.setTables(var1.getText("DbTables"));
         this.dbFields = new DbFieldsPersistor().restoreFrom(var1.getFirstChild("DbFields"));
         this.relations = new DbTranRelationsPersistor().restoreFrom(var1.getFirstChild("Relations"));
         this.sqlScript = new SqlScriptPersistor().restoreFrom(var1.getFirstChild("SqlScript"));
         this.treeFilters = new DbTranFiltersPersistor().restoreFrom(var1.getFirstChild("Filters"));
         this.reconstructIncludeSchemaProperty();
      }
   }

   private void restoreSourceId(Persistor var1) {
      String var2 = var1.getString("sourceId");
      if (var2 != null) {
         this.sourceId = new SourceId(var2);
      }
   }

   private void restoreTargetId(Persistor var1) {
      String var2 = var1.getString("targetId");
      if (var2 != null) {
         this.targetId = new TargetId(var2);
      }
   }

   private void restoreObjects(Persistor var1) {
      this.objects = Maps.newHashMap();
      Persistor var2 = var1.getFirstChild("Objects");
      DatabaseObjectPersistor var3 = new DatabaseObjectPersistor();

      for (Persistor var5 : var2.getChildren("Item")) {
         this.objects.put(var3.restoreFrom(var5.getFirstChild("Object")), var5.getInteger("value"));
      }
   }

   private void reconstructIncludeSchemaProperty() {
      if (!this.getData().containsKey("includeSchemaName")) {
         boolean var1;
         if (this.isManualSqlStatement()) {
            var1 = false;
         } else {
            BeginEndQuote var2 = this.getBeginEndQuote();
            TableUsage var3 = this.dbTables.getTableUsage(var2);
            var1 = var3.isSchemaIncluded();
         }

         this.setIncludeSchemaName(var1);
      }
   }

   @Override
   public void writeTo(Persistor var1) {
      synchronized (this.getDataLock()) {
         super.writeTo(var1);
         this.writeSourceId(var1);
         this.writeTargetId(var1);
         this.writeObjects(var1);
         var1.putText("DbTables", this.dbTables.getTables());
         new DbFieldsPersistor().save(this.dbFields, var1.createChild("DbFields"));
         new DbTranRelationsPersistor().save(this.relations, var1.createChild("Relations"));
         new SqlScriptPersistor().save(this.sqlScript, var1.createChild("SqlScript"));
         new DbTranFiltersPersistor().save(this.treeFilters, var1.createChild("Filters"));
      }
   }

   private void writeSourceId(Persistor var1) {
      if (this.sourceId != null) {
         var1.putString("sourceId", this.sourceId.toString());
      }
   }

   private void writeTargetId(Persistor var1) {
      if (this.targetId != null) {
         var1.putString("targetId", this.targetId.toString());
      }
   }

   private void writeObjects(Persistor var1) {
      Persistor var2 = var1.createChild("Objects");
      DatabaseObjectPersistor var3 = new DatabaseObjectPersistor();

      for (DatabaseObject var5 : this.objects.keySet()) {
         if (var5 == null) {
            return;
         }
      }

      for (Entry var8 : this.objects.entrySet()) {
         Persistor var6 = var2.createChild("Item");
         var3.save((DatabaseObject)var8.getKey(), var6.createChild("Object"));
         var6.putInteger("value", (Integer)var8.getValue());
      }
   }

   public BeginEndQuote getBeginEndQuote() {
      return this.location.getBeginEndQuote();
   }

   public void setIncludeRowIdInInformixTables(boolean var1) {
      this.setBooleanProperty("informix.include_row_id", var1);
   }

   public boolean getIncludeRowIdInInformixTables() {
      return this.getBooleanProperty("informix.include_row_id", true);
   }

   public String getManualSqlStatement() {
      return this.getSqlScript().getManualSqlStatement();
   }

   public boolean hasManualSqlStatement() {
      synchronized (this.getDataLock()) {
         return StringUtils.isNotBlank(this.getManualSqlStatement());
      }
   }
}
