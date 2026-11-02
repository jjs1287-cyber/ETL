package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.collections.Pair;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbTables;
import org.etlforge.integration.data.structure.database.DbTablesFactory;
import org.etlforge.integration.data.structure.database.ObjectAliasGenerator;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;

public final class SelectedTables {
   private final LinkedHashMap<DatabaseObject, Integer> usage;

   public SelectedTables() {
      this.usage = Maps.newLinkedHashMap();
   }

   public SelectedTables(SelectedTables var1) {
      this.usage = Maps.newLinkedHashMap(var1.usage);
   }

   public boolean isEmpty() {
      return this.usage.isEmpty();
   }

   public boolean isSingleTable() {
      if (this.usage.size() == 1) {
         int var1 = this.usage.values().iterator().next();
         return var1 == 1;
      } else {
         return false;
      }
   }

   public void updateCount(DatabaseObject var1, int var2) {
      Preconditions.checkArgument(this.isTableIncluded(var1), "table is not included");
      Preconditions.checkArgument(var2 > 0, "count must be > 0");
      Preconditions.checkArgument(var2 <= 100, "count must be <= 100");
      this.usage.put(var1, var2);
   }

   public DatabaseObject[] getIncludedTables() {
      Set var1 = this.usage.keySet();
      return (DatabaseObject[]) var1.toArray(new DatabaseObject[var1.size()]);
   }

   public boolean isTableIncluded(DatabaseObject var1) {
      return this.usage.containsKey(var1);
   }

   public void setTableUsage(DatabaseObject var1, int var2) {
      this.usage.put(var1, var2);
   }

   public List<Pair<DatabaseObject, Integer>> getTableUsage() {
      ArrayList var1 = Lists.newArrayList();

      for (Entry var3 : this.usage.entrySet()) {
         var1.add(Pair.of(var3));
      }

      return var1;
   }

   public void applyTo(DatabaseStructure var1, BeginEndQuote var2, boolean var3) {
      Map var4 = this.getEffectiveTableUsage(var3);
      DbTables var5 = this.toDbTables(var4, var2);
      var1.setDbTables(var5);
      var1.setDatabaseObjectsUsage(var4);
   }

   private Map<DatabaseObject, Integer> getEffectiveTableUsage(boolean var1) {
      if (!var1) {
         return Maps.newHashMap(this.usage);
      }

      HashMap var2 = Maps.newHashMap();

      for (Entry var4 : this.usage.entrySet()) {
         DatabaseObject var5 = ((DatabaseObject)var4.getKey()).createCopy(((DatabaseObject)var4.getKey()).getName());
         var2.put(var5, var4.getValue());
      }

      return var2;
   }

   private DbTables toDbTables(Map<DatabaseObject, Integer> var1, BeginEndQuote var2) {
      ObjectAliasGenerator var3 = new ObjectAliasGenerator(var2, this.getIncludedTables());
      var3.setTableUsage(var1);
      DbTablesFactory var4 = new DbTablesFactory(var3);
      return var4.create(var1, var2);
   }

   public void includeTables(Iterable<DatabaseObject> var1) {
      for (DatabaseObject var3 : var1) {
         if (!this.isTableIncluded(var3)) {
            this.usage.put(var3, 1);
         }
      }
   }

   public void removeTables(Iterable<DatabaseObject> var1) {
      for (DatabaseObject var3 : var1) {
         this.usage.remove(var3);
      }
   }

   public void replaceTableInstances(DatabaseObject[] var1) {
      for (DatabaseObject var5 : var1) {
         Integer var6 = this.usage.remove(var5);
         if (var6 == 0) {
            var6 = 1;
         }

         this.usage.put(var5, var6);
      }
   }

   public int getUsageCount(DatabaseObject var1) {
      Integer var2 = this.usage.get(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("Unknown table: " + var1);
      } else {
         return var2;
      }
   }

   @Override
   public int hashCode() {
      return this.usage.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof SelectedTables ? this.usage.equals(((SelectedTables)var1).usage) : false;
      }
   }

   public DatabaseObject[] getTablesIncludingAliases(BeginEndQuote var1) {
      DatabaseObject[] var2 = this.getIncludedTables();
      ObjectAliasGenerator var3 = new ObjectAliasGenerator(var1, var2);
      ArrayList var4 = Lists.newArrayList();

      for (DatabaseObject var8 : var2) {
         var4.add(var8);
         int var9 = this.getUsageCount(var8);

         for (int var10 = 1; var10 < var9; var10++) {
            String var11 = var3.getAlias(var8);
            var4.add(var8.createCopy(var11));
         }
      }

      return (DatabaseObject[]) var4.toArray(new DatabaseObject[var4.size()]);
   }

   public SelectedTables withSchemaName(BeginEndQuote var1) {
      SelectedTables var2 = new SelectedTables();

      for (Entry var4 : this.usage.entrySet()) {
         DatabaseObject var5 = (DatabaseObject)var4.getKey();
         if (!isSchemaNameIncluded(var5, var1)) {
            String var6 = var1.getQualifiedName(var5);
            DatabaseObject var7 = var5.createCopy(var6);
            int var8 = (Integer)var4.getValue();
            var2.setTableUsage(var7, var8);
         }
      }

      return var2;
   }

   private static boolean isSchemaNameIncluded(DatabaseObject var0, BeginEndQuote var1) {
      String var2 = var0.getName();
      if (var1.isEnclosingName(var2)) {
         String var3 = var1.end + "." + var1.begin;
         if (var2.contains(var3)) {
            return true;
         }
      }

      return false;
   }

   public DatabaseObject[] getTablesWithoutColumnInformation() {
      ArrayList var1 = Lists.newArrayList();

      for (DatabaseObject var5 : this.getIncludedTables()) {
         if (var5.getNumberOfColumns() == 0) {
            var1.add(var5);
         }
      }

      return (DatabaseObject[]) var1.toArray(new DatabaseObject[0]);
   }
}
