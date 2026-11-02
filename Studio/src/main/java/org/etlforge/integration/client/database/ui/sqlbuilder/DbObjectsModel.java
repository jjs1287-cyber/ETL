package org.etlforge.integration.client.database.ui.sqlbuilder;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

public final class DbObjectsModel {
   private final Locatable db;
   private volatile Map<DatabaseObject, List<DatabaseColumn>> objects;
   private final List<DbObjectsModelListener> listeners;
   private volatile BeginEndQuote beginEndQuotes;

   public DbObjectsModel(Locatable var1) {
      Preconditions.checkArgument(var1.getLocation() instanceof DatabaseLocation, "db must represent a DatabaseLocation");
      this.db = var1;
      this.objects = Maps.newHashMap();
      this.listeners = new CopyOnWriteArrayList<>();
   }

   public IntegrationEntity getProjectItem() {
      return (IntegrationEntity)this.db;
   }

   public DatabaseLocation getDatabase() {
      return (DatabaseLocation)this.db.getLocation();
   }

   public DriverDescriptor getDriver() {
      return this.getDatabase().getDriver();
   }

   public BeginEndQuote getBeginEndQuotes() {
      return this.beginEndQuotes != null ? this.beginEndQuotes : this.getDatabase().getBeginEndQuote();
   }

   public void setBeginEndQuotes(BeginEndQuote var1) {
      if (!Objects.equal(var1, this.beginEndQuotes)) {
         this.beginEndQuotes = var1;
         this.fireBeginEndQuotesChanged(var1);
      }
   }

   private void fireBeginEndQuotesChanged(BeginEndQuote var1) {
      for (DbObjectsModelListener var3 : this.listeners) {
         var3.beginEndQuotesChanged(this, var1);
      }
   }

   public SourceId getSourceId() {
      return this.db instanceof Source ? (SourceId)this.db.getID() : null;
   }

   public TargetId getTargetId() {
      return this.db instanceof Target ? (TargetId)this.db.getID() : null;
   }

   public Collection<DatabaseObject> getTables() {
      return Sets.newHashSet(this.objects.keySet());
   }

   public List<DatabaseColumn> getColumns(DatabaseObject var1) {
      return Lists.newArrayList(this.objects.get(var1));
   }

   public void setTables(Collection<DatabaseObject> var1) {
      this.objects = Maps.newHashMap();

      for (DatabaseObject var3 : var1) {
         this.objects.put(var3, var3.getAllColumns());
      }

      this.fireModelChanged();
   }

   private void fireModelChanged() {
      for (DbObjectsModelListener var2 : this.listeners) {
         var2.modelChanged(this);
      }
   }

   public void setColumns(DatabaseObject var1, List<DatabaseColumn> var2) {
      this.objects.put(var1, var2);
      this.fireTableChanged(var1);
   }

   private void fireTableChanged(DatabaseObject var1) {
      for (DbObjectsModelListener var3 : this.listeners) {
         var3.tableChanged(this, var1);
      }
   }

   public void addModelListener(DbObjectsModelListener var1) {
      this.listeners.add(var1);
   }

   public void removeModelListener(DbObjectsModelListener var1) {
      this.listeners.remove(var1);
   }
}
