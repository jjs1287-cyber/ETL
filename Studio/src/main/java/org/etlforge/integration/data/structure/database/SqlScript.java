package org.etlforge.integration.data.structure.database;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.system.annotations.InternalMethod;

public final class SqlScript {
   public static final int CONSTRUCTED_SQL_STATEMENT = 1;
   public static final int PRIMARY_KEY_LIST = 2;
   public static final int MANUAL_SQL_STATEMENT = 3;
   public static final String PRIMARY_KEY_SEPARATOR = ",";
   public static final String SQL_ID = "sqlid";
   public static final String SQL_SCRIPT = "sqlscript";
   public static final String SQL_SCRIPT_TYPE = "sqlscripttype";
   public static final String SQL_WHERE = "sqlwhere";
   private SqlScript.Entry[] entries = new SqlScript.Entry[3];

   public SqlScript.Entry[] getAllEntries() {
      ArrayList var1 = Lists.newArrayList();

      for (SqlScript.Entry var5 : this.entries) {
         if (var5 != null) {
            var1.add(var5);
         }
      }

      return (SqlScript.Entry[]) var1.toArray(new SqlScript.Entry[var1.size()]);
   }

   @InternalMethod
   public void addEntry(SqlScript.Entry var1) {
      this.entries[var1.getType() - 1] = var1;
   }

   private SqlScript.Entry removeEntry(int var1) {
      SqlScript.Entry var2 = this.entries[var1 - 1];
      this.entries[var1 - 1] = null;
      return var2;
   }

   public SqlScript.Entry setPrimaryKeysEntry(DatabaseObject var1) {
      SqlScript.Entry var2 = null;
      if (var1.hasPrimaryKeys()) {
         SqlScript.Entry var3 = this.entries[1];
         this.entries[1] = new SqlScript.Entry(2, getPrimaryKeysString(var1), null);
         var2 = var3;
      } else {
         var2 = this.removeEntry(2);
      }

      return var2;
   }

   public void setSqlStatement(DatabaseObject var1, BeginEndQuote var2, String var3) {
      String var4 = "SELECT * FROM " + getEscapedDatabaseName(var1.getName(), var2);
      if (StringUtils.isNotEmpty(var3)) {
         var4 = var4 + " WHERE " + var3;
      }

      this.entries[0] = new SqlScript.Entry(1, var4, var3);
   }

   public String getConstructedSqlStatement() {
      SqlScript.Entry var1 = this.entries[0];
      return var1 != null ? var1.getScript() : null;
   }

   public void setManualSqlStatement(String var1) {
      this.entries[2] = new SqlScript.Entry(3, var1, null);
   }

   public String getManualSqlStatement() {
      SqlScript.Entry var1 = this.entries[2];
      return var1 != null ? var1.getScript() : null;
   }

   public boolean isManualSqlStatement() {
      SqlScript.Entry var1 = this.entries[2];
      return var1 != null;
   }

   public String getWhereClause() {
      SqlScript.Entry var1 = this.entries[0];
      if (var1 == null) {
         return "";
      }

      String var2 = var1.whereClause;
      return var2 == null ? "" : var2;
   }

   public void updateWhereClause(String var1) {
      SqlScript.Entry var2 = this.entries[0];
      if (var2 == null) {
         throw new IllegalStateException();
      }

      String var3 = this.getTrimmedWhereClause(var1);
      var2.setNewWhereClause(var3);
   }

   private String getTrimmedWhereClause(String var1) {
      return StringUtils.removeStartIgnoreCase(var1, "where ");
   }

   public SqlScript getCopy() {
      SqlScript var1 = new SqlScript();
      var1.entries = new SqlScript.Entry[this.entries.length];

      for (int var2 = 0; var2 < this.entries.length; var2++) {
         var1.entries[var2] = this.entries[var2] == null ? null : this.entries[var2].getCopy();
      }

      return var1;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof SqlScript ? Arrays.equals(this.entries, ((SqlScript)var1).entries) : false;
      }
   }

   @Override
   public int hashCode() {
      int var1 = 17;

      for (SqlScript.Entry var5 : this.entries) {
         var1 = 37 * var1 + (var5 == null ? 0 : var5.hashCode());
      }

      return var1;
   }

   private static String getEscapedDatabaseName(String var0, BeginEndQuote var1) {
      if (var1 == null) {
         return var0;
      }

      String var2 = var1.begin;
      String var3 = var1.end;
      return var0.startsWith(var2) && var0.endsWith(var3) ? var0 : var1.begin + var0 + var1.end;
   }

   private static String getPrimaryKeysString(DatabaseObject var0) {
      StringBuilder var1 = new StringBuilder();

      for (DatabaseColumn var3 : var0.getPrimaryKeys()) {
         var1.append(var3.getQualifiedName());
         var1.append(",");
      }

      return var1.toString();
   }

   private static void validateType(int var0) {
      if (var0 != 1 && var0 != 2 && var0 != 3) {
         throw new IllegalArgumentException("type must be one of SQL_STATEMENT or PRIMARY_KEY_LIST.");
      }
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("Constructed SQL Statement:\n");
      var1.append("   ");
      var1.append(this.entries[0]);
      var1.append("\n");
      var1.append("Manual SQL Statement:\n");
      var1.append("   ");
      var1.append(this.entries[2]);
      var1.append("\n");
      var1.append("Primary Keys:\n");
      var1.append("   ");
      var1.append(this.entries[1]);
      var1.append("\n");
      return var1.toString();
   }

   public static final class Entry implements Cloneable {
      private static long TEMPORARY_ID_SEED = -System.currentTimeMillis();
      private final long id;
      private final int type;
      private String script;
      private String whereClause;

      public Entry(long var1, int var3, String var4, String var5) {
         SqlScript.validateType(var3);
         this.id = var1;
         this.type = var3;
         this.script = var4;
         this.whereClause = var5;
      }

      public Entry(int var1, String var2, String var3) {
         this(getNextTemporaryId(), var1, var2, var3);
      }

      private void setNewWhereClause(String var1) {
         String var2 = this.getScriptWithoutWhereClause();
         this.whereClause = var1;
         if (StringUtils.isEmpty(var1)) {
            this.script = var2;
         } else {
            this.script = var2 + " WHERE " + var1;
         }
      }

      private String getScriptWithoutWhereClause() {
         if (StringUtils.isNotEmpty(this.whereClause)) {
            String var1 = "where " + this.whereClause;
            return StringUtils.removeEndIgnoreCase(this.script, var1).trim();
         } else {
            return this.script;
         }
      }

      public long getId() {
         return this.id;
      }

      public int getType() {
         return this.type;
      }

      public String getScript() {
         return this.script;
      }

      public String getWhereClause() {
         return this.whereClause;
      }

      public SqlScript.Entry getCopy() {
         try {
            return (SqlScript.Entry)this.clone();
         } catch (CloneNotSupportedException var2) {
            throw new RuntimeException(var2);
         }
      }

      public static String[] getColumnNames() {
         return new String[]{"transformation_id", "sqlscript", "sqlscripttype", "sqlwhere"};
      }

      public static SqlScript.Entry fromColumnValues(StringKeyValuePairs var0) {
         int var1 = var0.getInt("sqlscripttype");
         String var2 = var0.get("sqlscript");
         String var3 = var0.get("sqlwhere");
         return new SqlScript.Entry(var1, var2, var3);
      }

      public String[] getColumnValues(String var1) {
         return new String[]{var1, this.getScript(), Integer.toString(this.getType()), this.getWhereClause()};
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof SqlScript.Entry)) {
            return false;
         }

         SqlScript.Entry var2 = (SqlScript.Entry)var1;
         return this.id == var2.id && this.type == var2.type && Objects.equal(this.script, var2.script) && Objects.equal(this.whereClause, var2.whereClause);
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 37 * var1 + (int)(this.id ^ this.id >>> 32);
         var1 = 37 * var1 + this.type;
         var1 = 37 * var1 + (this.script == null ? 0 : this.script.hashCode());
         var1 = 37 * var1 + (this.whereClause == null ? 0 : this.whereClause.hashCode());
         return super.hashCode();
      }

      @Override
      public String toString() {
         StringBuilder var1 = new StringBuilder("Type: ");
         var1.append(this.type);
         var1.append("    Script: ");
         var1.append(this.script);
         var1.append("    WHERE: ");
         var1.append(this.whereClause);
         return var1.toString();
      }

      private static synchronized long getNextTemporaryId() {
         return --TEMPORARY_ID_SEED;
      }
   }
}
