package org.etlforge.integration.data.structure.database;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.system.annotations.InternalConstructor;
import org.etlforge.util.SortOrder;

public final class DbTranFilters {
   public static final String TRANS_FILTER_ID = "trans_filter_id";
   public static final String GROUP_ID = "group_id";
   public static final String TNAME = "tname";
   public static final String CNAME = "cname";
   public static final String CONDITION = "condition";
   public static final String SORT_BY = "sortby";
   private Set<DbTranFilters.Entry> entries = Sets.newHashSet();

   public List<DbTranFilters.Entry> getAllEntries() {
      return Lists.newArrayList(this.entries);
   }

   public void addEntry(DbTranFilters.Entry var1) {
      Preconditions.checkNotNull(var1, "entry");
      this.entries.add(var1);
   }

   public void addEntry(DatabaseColumn var1, String var2, SortOrder var3) {
      this.entries.add(new DbTranFilters.Entry(var1.getDatabaseObject().getName(), var1.getName(), var2, var3));
   }

   public DbTranFilters getCopy() {
      DbTranFilters var1 = new DbTranFilters();
      HashSet var2 = Sets.newHashSet();

      for (DbTranFilters.Entry var4 : this.entries) {
         var2.add(var4.getCopy());
      }

      var1.entries = var2;
      return var1;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof DbTranFilters) {
         DbTranFilters var2 = (DbTranFilters)var1;
         return this.entries.equals(var2.entries);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      byte var1 = 17;
      return 37 * var1 + this.entries.hashCode();
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;

      for (DbTranFilters.Entry var4 : this.entries) {
         if (var2 > 0) {
            var1.append("\n");
         }

         var1.append(var4);
         var2++;
      }

      return var1.toString();
   }

   public void removeAllEntries() {
      this.entries.clear();
   }

   public static final class Entry implements Cloneable, Comparable<DbTranFilters.Entry> {
      private final long id;
      private final String table;
      private final String column;
      private final String condition;
      private final SortOrder sortOrder;

      public Entry(long var1, String var3, String var4, String var5, SortOrder var6) {
         if (var3 == null || var4 == null || var5 == null) {
            throw new IllegalArgumentException("table, column, and condition may not be null.");
         }

         if (var3.length() != 0 && var4.length() != 0) {
            this.id = var1;
            this.table = var3;
            this.column = var4;
            this.condition = var5;
            this.sortOrder = var6 != null ? var6 : SortOrder.NONE;
         } else {
            throw new IllegalArgumentException("table and column may not be empty.");
         }
      }

      public Entry(String var1, String var2, String var3, SortOrder var4) {
         this(0L, var1, var2, var3, var4);
      }

      @InternalConstructor
      public Entry(long var1, String var3, String var4, String var5, String var6) {
         this(var1, var3, var4, var5, fromString(var6));
      }

      private static SortOrder fromString(String var0) {
         try {
            return SortOrder.valueOf(var0);
         } catch (RuntimeException var2) {
            return SortOrder.NONE;
         }
      }

      public long getId() {
         return this.id;
      }

      public String getTableName() {
         return this.table;
      }

      public String getColumnName() {
         return this.column;
      }

      public String getCondition() {
         return this.condition;
      }

      public SortOrder getSortOrder() {
         return this.sortOrder;
      }

      public DbTranFilters.Entry getCopy() {
         try {
            return (DbTranFilters.Entry)this.clone();
         } catch (CloneNotSupportedException var2) {
            throw new RuntimeException(var2);
         }
      }

      public static String[] getColumnNames() {
         return new String[]{"transformation_id", "group_id", "tname", "cname", "condition", "sortby"};
      }

      public static DbTranFilters.Entry fromColumnValues(StringKeyValuePairs var0) {
         return new DbTranFilters.Entry(var0.get("tname"), var0.get("cname"), var0.get("condition"), SortOrder.fromAbbreviation(var0.get("sortby")));
      }

      public String[] getColumnValues(String var1, DbTranFilters var2) {
         String[] var3 = new String[]{var1, Integer.toString(0), this.getTableName(), this.getColumnName(), this.getCondition(), null};
         SortOrder var4 = this.getSortOrder();
         var3[5] = var4 != null ? var4.getAbbreviation() : "";
         return var3;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof DbTranFilters.Entry)) {
            return false;
         }

         DbTranFilters.Entry var2 = (DbTranFilters.Entry)var1;
         return this.sortOrder == var2.sortOrder && this.table.equals(var2.table) && this.column.equals(var2.column) && this.condition.equals(var2.condition);
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 37 * var1 + this.table.hashCode();
         var1 = 37 * var1 + this.column.hashCode();
         var1 = 37 * var1 + this.condition.hashCode();
         return 37 * var1 + (this.sortOrder == null ? 0 : this.sortOrder.hashCode());
      }

      public int compareTo(DbTranFilters.Entry var1) {
         return Longs.compare(this.id, var1.id);
      }

      @Override
      public String toString() {
         StringBuilder var1 = new StringBuilder(this.column);
         var1.append("   ");
         var1.append(this.condition);
         if (this.sortOrder != null) {
            var1.append("   ");
            var1.append(this.sortOrder);
         }

         return var1.toString();
      }
   }
}
