package org.etlforge.integration.client.ui.structure.editor.db;

import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectContainer;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

final class TableSelectionModel extends AbstractPropertyBasedModel {
   public static final String AVAILABLE_TABLES = "availab...";
   public static final String USAGE_COUNT = "ho ho ho";
   public static final String INCLUDE_SCHEMA = "merry";
   public static final String SEARCH_FILTER = "filter";
   private final DatabaseLocation dbLocation;
   private final SourceId sourceId;
   private final TargetId targetId;
   private final Property<DatabaseObjectContainer[]> availableObjects;
   private final Property<SelectedTables> usageCount;
   private final Property<Boolean> includeSchema;
   private final Property<TableSearchFilter> filter;
   private volatile UiJob currentServerInteraction;

   public TableSelectionModel(DatabaseLocation var1, SourceId var2, TargetId var3) {
      this.dbLocation = var1;
      this.sourceId = var2;
      this.targetId = var3;
      this.availableObjects = this.newProperty("availab...", new DatabaseObjectContainer[0]);
      this.availableObjects.setAllowsNull(false);
      this.usageCount = this.newProperty("ho ho ho", new SelectedTables());
      this.usageCount.setAllowsNull(false);
      this.includeSchema = this.newProperty("merry", this.isProbablyOracle());
      this.includeSchema.setAllowsNull(false);
      this.filter = this.newProperty("filter", TableSearchFilter.TABLE_AND_VIEWS);
      this.filter.setAllowsNull(false);
   }

   public DatabaseLocation getDbLocation() {
      return this.dbLocation;
   }

   public boolean isProbablyOracle() {
      return this.dbLocation.getDriver().isProbablyOracle();
   }

   public SourceId getSourceId() {
      return this.sourceId;
   }

   public TargetId getTargetId() {
      return this.targetId;
   }

   public DatabaseObjectContainer[] getAvailableObjects() {
      return (DatabaseObjectContainer[])this.availableObjects.get().clone();
   }

   public void setAvailableObjects(DatabaseObjectContainer[] var1) {
      this.availableObjects.set((DatabaseObjectContainer[])var1.clone());
   }

   public SelectedTables getUsageCount() {
      return new SelectedTables(this.usageCount.get());
   }

   public void setUsageCount(SelectedTables var1) {
      this.usageCount.set(new SelectedTables(var1));
   }

   public boolean isTableIncluded(DatabaseObject var1) {
      return this.usageCount.get().isTableIncluded(var1);
   }

   public void includeTables(Iterable<DatabaseObject> var1) {
      SelectedTables var2 = this.getUsageCount();
      var2.includeTables(var1);
      this.usageCount.set(var2);
   }

   public void removeTables(Iterable<DatabaseObject> var1) {
      SelectedTables var2 = this.getUsageCount();
      var2.removeTables(var1);
      this.usageCount.set(var2);
   }

   public boolean isSchemaNameIncluded() {
      return this.includeSchema.get();
   }

   public void setIncludeSchemaName(boolean var1) {
      this.includeSchema.set(var1);
   }

   public BeginEndQuote getBeginEndQuotes() {
      return this.dbLocation.getBeginEndQuote();
   }

   public String getQualifiedName(DatabaseObject var1) {
      BeginEndQuote var2 = this.getBeginEndQuotes();
      return var2.escapeName(var1.getSchema()) + "." + var2.escapeName(var1.getName());
   }

   public TableSearchFilter getSearchFilter() {
      return this.filter.get();
   }

   public void setSearchFilter(TableSearchFilter var1) {
      this.filter.set(var1);
   }

   public UiJob getCurrentServerInteraction() {
      return this.currentServerInteraction;
   }

   public void setCurrentServerInteraction(UiJob var1) {
      this.currentServerInteraction = var1;
   }
}
